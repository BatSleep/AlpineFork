package dev.bat.alpinefork.listener;

import dev.bat.alpinefork.listener.concurrent.CopyOnWriteListenerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Brady
 * @since 3.0.0
 */
enum DefaultListenerListFactory implements ListenerListFactory {
    INSTANCE;

    @Override
    public @NotNull <T> ListenerList<T> create(Class<T> eventType) {
        return new CopyOnWriteListenerList<>();
    }
}
