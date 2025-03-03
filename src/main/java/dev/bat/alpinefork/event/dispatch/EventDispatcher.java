package dev.bat.alpinefork.event.dispatch;

import dev.bat.alpinefork.listener.Listener;
import dev.bat.alpinefork.listener.ListenerExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

/**
 * Dispatches an event to all Listeners of a given iterator.
 *
 * @author Brady
 * @since 3.0.0
 */
@FunctionalInterface
public interface EventDispatcher {

    /**
     * Dispatches the specified event to all Listeners in the specified {@link Iterator} via {@link Listener#accept}.
     * Whether this is done synchronously or asynchronously, as well as the routine for handling exceptions is entirely
     * implementation-dependent.
     *
     * @param event     The event
     * @param listeners The listeners to dispatch the event to
     * @param <T>       The event type
     * @since 3.0.0
     */
    <T> void dispatch(@NotNull T event, @NotNull Iterator<Listener<T>> listeners);

    /**
     * Returns an optimized implementation of {@link EventDispatcher} which has no exception handling.
     *
     * @return The dispatcher
     * @since 3.0.0
     */
    static @NotNull EventDispatcher fastEventDispatcher() {
        return FastEventDispatcher.INSTANCE;
    }

    /**
     * Returns a new {@link EventDispatcher} which invokes the specified {@link ListenerExceptionHandler} when a
     * Listener callback throws an exception. When an exception is thrown, regardless of the exception handler's
     * outcome (whether to propagate the exception upwards), none of the remaining Listeners will receive the current
     * event.
     *
     * @param exceptionHandler The exception handler
     * @return The dispatcher
     * @since 3.0.0
     */
    static @NotNull EventDispatcher withExceptionHandler(@NotNull ListenerExceptionHandler exceptionHandler) {
        return new ExceptionHandlingDispatcher(Objects.requireNonNull(exceptionHandler));
    }
}

