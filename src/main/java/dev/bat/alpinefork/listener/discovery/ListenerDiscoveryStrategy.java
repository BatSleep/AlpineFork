package dev.bat.alpinefork.listener.discovery;

import dev.bat.alpinefork.bus.EventManager;
import dev.bat.alpinefork.exception.ListenerDiscoveryException;
import dev.bat.alpinefork.listener.Listener;
import dev.bat.alpinefork.listener.Subscribe;
import dev.bat.alpinefork.listener.Subscriber;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Used by {@link EventManager} to discover listener candidates in {@link Subscriber} classes.
 *
 * @author Brady
 * @since 3.0.0
 */
@FunctionalInterface
public interface ListenerDiscoveryStrategy {

    /**
     * Locates all the listener candidates that are provided by the specified {@link Subscriber} class, according to
     * this strategy.
     *
     * @param cls The class to search
     * @return A stream of candidates
     * @throws ListenerDiscoveryException If an error is detected in Listener definition
     * @since 3.0.0
     */
    Stream<ListenerCandidate<?>> findAll(Class<? extends Subscriber> cls);

    /**
     * Returns the built-in discovery strategy for {@link Listener} fields annotated with {@link Subscribe}
     *
     * @since 3.0.0
     */
    static @NotNull ListenerDiscoveryStrategy subscribeFields() {
        return ListenerFieldDiscoveryStrategy.INSTANCE;
    }

    /**
     * Returns the built-in discovery strategy for event callback methods annotated with {@link Subscribe}
     * @since 3.0.0
     */
    static @NotNull ListenerDiscoveryStrategy subscribeMethods() {
        return ListenerMethodDiscoveryStrategy.INSTANCE;
    }
}
