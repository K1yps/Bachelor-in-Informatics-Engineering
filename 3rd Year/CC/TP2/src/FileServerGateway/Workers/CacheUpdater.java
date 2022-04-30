package FileServerGateway.Workers;

import FileServerProtocol.FileServerChunk.FSCache;
import Utils.BoundedQueue;

import java.util.function.Consumer;

public class CacheUpdater implements Runnable {
    BoundedQueue<FSCache> buffer;
    Consumer<FSCache> consumer;

    public CacheUpdater(BoundedQueue<FSCache> buffer, Consumer<FSCache> cacheUpdater) {
        this.buffer = buffer;
        this.consumer = cacheUpdater;
    }

    @Override
    public void run() {
        while (true) {
            try {
                consumer.accept(buffer.poll());
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
