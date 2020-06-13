package com.beinglee.rpc.transport;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 1 增加一个PRC的背压机制(Back Pressure) 通过信号量限制在途请求个数
 * 2 兜底的超时机制来保证所有情况下ResponseFuture都能结束
 *
 * @author zhanglu
 * @date 2020/6/11 20:06
 */
public class InFlightRequests implements Closeable {

    private static final long TIMEOUT_SEC = 10L;
    private static final long MAX_DURATION_TIME = 1_000_000_000L;
    private final Semaphore semaphore = new Semaphore(10);
    private final Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledFuture<?> scheduledFuture;

    public InFlightRequests() {
        scheduledFuture = scheduledExecutor.scheduleAtFixedRate(this::removeTimeoutFutures, TIMEOUT_SEC, TIMEOUT_SEC, TimeUnit.SECONDS);
    }

    private void removeTimeoutFutures() {
        futureMap.entrySet().removeIf(entry -> {
            if (System.nanoTime() - entry.getValue().getTimestamp() > MAX_DURATION_TIME) {
                semaphore.release();
                return true;
            }
            return false;
        });
    }

    public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
        if (semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    public ResponseFuture remove(int requestId) {
        ResponseFuture future = futureMap.remove(requestId);
        if (future != null) {
            semaphore.release();
        }
        return future;
    }

    @Override
    public void close() {
        scheduledFuture.cancel(true);
        scheduledExecutor.shutdown();
    }
}
