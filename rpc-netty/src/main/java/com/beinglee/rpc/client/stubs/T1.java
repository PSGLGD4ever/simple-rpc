package com.beinglee.rpc.client.stubs;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class T1 {

    public CompletableFuture<String> test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        System.out.println(Thread.currentThread());
        return CompletableFuture.completedFuture("name");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        T1 t1 = new T1();
        t1.test().thenRun(() -> System.out.println(Thread.currentThread()));
    }
}
