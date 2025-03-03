package dev.bat.alpinefork.listener.concurrent;

import dev.bat.alpinefork.event.dispatch.EventDispatcher;
import dev.bat.alpinefork.listener.Listener;
import dev.bat.alpinefork.listener.ListenerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A wrapper implementation of {@link ListenerList} which uses a {@link ReadWriteLock} for synchronization.
 *
 * @author Brady
 * @see ListenerList#readWriteLock(ListenerList)
 * @since 3.0.0
 */
public final class ReadWriteLockListenerList<T> implements ListenerList<T> {

    private final ListenerList<T> backing;
    private final ReentrantReadWriteLock.ReadLock r;
    private final ReentrantReadWriteLock.WriteLock w;

    public ReadWriteLockListenerList(ListenerList<T> backing) {
        this.backing = Objects.requireNonNull(backing);
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.r = lock.readLock();
        this.w = lock.writeLock();
    }

    @Override
    public void post(@NotNull T event, @NotNull EventDispatcher dispatcher) {
        this.r.lock();
        try {
            this.backing.post(event, dispatcher);
        } finally {
            this.r.unlock();
        }
    }

    @Override
    public boolean add(@NotNull Listener<T> listener) {
        this.w.lock();
        try {
            return this.backing.add(listener);
        } finally {
            this.w.unlock();
        }
    }

    @Override
    public boolean remove(@NotNull Listener<T> listener) {
        this.w.lock();
        try {
            return this.backing.remove(listener);
        } finally {
            this.w.unlock();
        }
    }
}
