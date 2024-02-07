package Utils;

import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class BoundedQueue<T> {

    Lock lock = new ReentrantLock();
    Condition cond = lock.newCondition();
    Queue<T> queue;

    public BoundedQueue() {
        queue = new ArrayDeque<>();
    }

    public BoundedQueue(int size) {
        queue = new ArrayDeque<>(size);
    }

    public boolean add(T t) {
        try {
            lock.lock();
            return queue.add(t);
        } finally {
            cond.signalAll();
            lock.unlock();
        }
    }

    public boolean add(T t, Runnable preFreeOperation) {
        try {
            lock.lock();
            return queue.add(t);
        } finally {
            cond.signalAll();
            try {
                preFreeOperation.run();
            } catch (Exception ignored) {
            }
            lock.unlock();
        }
    }

    public boolean offer(T t) {
        try {
            lock.lock();
            return queue.offer(t);
        } finally {
            cond.signalAll();
            lock.unlock();
        }
    }

    public T remove() throws NoSuchElementException {
        try {
            lock.lock();
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }

    public List<T> removeAll() {
        List<T> res = new ArrayList<>();
        try {
            lock.lock();
            T r;
            while ((r = queue.poll()) != null)
                res.add(r);

        } finally {
            lock.unlock();
        }
        return res;
    }


    public T poll() throws InterruptedException {
        try {
            lock.lock();
            while (queue.isEmpty())
                cond.await();
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }

    public T pollNanos(long nanos) throws InterruptedException, TimeoutException {
        try {
            lock.lock();
            while (queue.isEmpty()) {
                if (cond.awaitNanos(nanos) <= 0)
                    throw new TimeoutException("Condition TimedOut");
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }

    public T element() throws NoSuchElementException {
        try {
            lock.lock();
            return queue.element();
        } finally {
            lock.unlock();
        }
    }

    public T peek() throws InterruptedException {
        try {
            lock.lock();
            while (queue.isEmpty())
                cond.await();
            return queue.peek();
        } finally {
            lock.unlock();
        }
    }


    public T peekNanos(long nanos) throws InterruptedException, TimeoutException {
        try {
            lock.lock();
            while (queue.isEmpty()) {
                if (cond.awaitNanos(nanos) <= 0)
                    throw new TimeoutException("Condition TimedOut");
            }
            return queue.peek();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        try {
            lock.lock();
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        try {
            lock.lock();
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        try {
            lock.lock();
            queue.clear();
        } finally {
            lock.unlock();
        }
    }

    public void forEach(Consumer<? super T> action) {
        try {
            lock.lock();
            queue.forEach(action);
        } finally {
            lock.unlock();
        }
    }

    public void forEachAndClear(Consumer<? super T> action) {
        try {
            lock.lock();
            queue.forEach(action);
            queue.clear();
        } finally {
            lock.unlock();
        }
    }
}
