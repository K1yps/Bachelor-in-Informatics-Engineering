package FileServerGateway.Workers;

import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class CyclicClock extends TimerTask implements Runnable {
    private final Consumer<Integer> task;
    private final Lock lock = new ReentrantLock();
    private int clock;

    public int getClock() {
        try {
            lock.lock();
            return clock;
        } finally {
            lock.unlock();
        }
    }

    public CyclicClock(Consumer<Integer> task) {
        this.task = task;
        this.clock = 0;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            task.accept(clock++);
            if (this.clock > 255) this.clock = 0;
        } finally {
            lock.unlock();
        }
    }

    public void rollBack() {
        try {
            lock.lock();
            this.clock--;
            if (this.clock < 0) this.clock = 255;
        } finally {
            lock.unlock();
        }
    }
}
