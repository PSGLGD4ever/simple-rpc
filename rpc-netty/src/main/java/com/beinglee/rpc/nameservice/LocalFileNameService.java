package com.beinglee.rpc.nameservice;

import com.beinglee.rpc.NameService;
import com.beinglee.rpc.serialize.SerializeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zhanglu
 * @date 2020/6/29 9:46
 */

public class LocalFileNameService implements NameService {

    private static final Logger log = LoggerFactory.getLogger(LocalFileNameService.class);

    private File file = null;

    @Override
    public Collection<String> supportedSchemes() {
        return Collections.singleton("file");
    }

    @Override
    public void connect(URI nameServiceUri) {
        if (supportedSchemes().contains(nameServiceUri.getScheme())) {
            file = new File(nameServiceUri);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public synchronized void registerService(String serviceName, URI uri) throws IOException {
        log.info("Register service: {}, uri:{}", serviceName, uri);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                int fileLength = (int) raf.length();
                Metadata metadata;
                byte[] bytes;
                if (fileLength > 0) {
                    bytes = new byte[fileLength];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }
                    metadata = SerializeSupport.parse(bytes);
                } else {
                    metadata = new Metadata();
                }
                List<URI> uris = metadata.computeIfAbsent(serviceName, (k) -> new ArrayList<>());
                if (!uris.contains(uri)) {
                    uris.add(uri);
                }
                bytes = SerializeSupport.serialize(metadata);
                fileChannel.truncate(bytes.length);
                fileChannel.position(0);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);
            } finally {
                lock.release();
            }
        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        Metadata metadata;
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                byte[] bytes = new byte[(int) raf.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }
                metadata = bytes.length == 0 ? new Metadata() : SerializeSupport.parse(bytes);
            } finally {
                lock.release();
            }
        }
        List<URI> uris = metadata.get(serviceName);
        if (uris == null || uris.isEmpty()) {
            return null;
        }
        return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
    }
}
