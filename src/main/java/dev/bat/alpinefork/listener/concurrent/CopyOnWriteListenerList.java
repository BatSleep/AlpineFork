package dev.bat.alpinefork.listener.concurrent;

import dev.bat.alpinefork.bus.EventManager;
import dev.bat.alpinefork.event.dispatch.EventDispatcher;
import dev.bat.alpinefork.listener.Listener;
import dev.bat.alpinefork.listener.ListenerList;
import dev.bat.alpinefork.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A {@link ListenerList} which uses copy-on-write semantics to support thread safety. However, this may cause a race
 * condition in applications that rely on cleanup code after calling {@link EventManager#unsubscribe}, since it will
 * not wait for {@link EventManager#post} to complete, and the old Listeners array may be dispatched to.
 *
 * @author Brady
 * @since 3.0.0
 */
public final class CopyOnWriteListenerList<T> implements ListenerList<T> {

    private volatile Listener<T>[] listeners;
    private final Object lock;

    public CopyOnWriteListenerList() {
        this.listeners = newListenerArray(0);
        this.lock = new Object();
    }

    @Override
    public void post(@NotNull T event, @NotNull EventDispatcher dispatcher) {
        dispatcher.dispatch(event, Util.arrayIterator(this.listeners));
    }

    @Override
    public boolean add(@NotNull Listener<T> listener) {
        synchronized (this.lock) {
            // TODO: Double-check to avoid always locking
            Listener<T>[] arr = this.listeners;
            if (Arrays.asList(arr).contains(listener)) {
                return false;
            }

            int index = Arrays.binarySearch(arr, listener);
            if (index < 0) {
                index = -index - 1;
            }

            int len = arr.length;
            Listener<T>[] newArr = newListenerArray(len + 1);
            System.arraycopy(arr, 0, newArr, 0, index);
            System.arraycopy(arr, index, newArr, index + 1, len - index);
            newArr[index] = listener;
            this.listeners = newArr;
            return true;
        }
    }

    @Override
    public boolean remove(@NotNull Listener<T> listener) {
        synchronized (this.lock) {
            // TODO: Double-check to avoid always locking
            Listener<T>[] arr = this.listeners;
            int index = Arrays.asList(arr).indexOf(listener);
            if (index < 0) {
                return false;
            }

            int len = arr.length;
            Listener<T>[] newArr = newListenerArray(len - 1);
            System.arraycopy(arr, 0, newArr, 0, index);
            System.arraycopy(arr, index + 1, newArr, index, len - index - 1);
            this.listeners = newArr;
            return true;
        }
    }

    private static final Listener<?>[] EMPTY_LISTENERS = new Listener<?>[0];

    @SuppressWarnings("unchecked")
    private static <T> Listener<T>[] newListenerArray(int size) {
        return (Listener<T>[]) (size == 0 ? EMPTY_LISTENERS : new Listener[size]);
    }
}
