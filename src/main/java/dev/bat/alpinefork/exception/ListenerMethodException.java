package dev.bat.alpinefork.exception;

import dev.bat.alpinefork.listener.Subscribe;

/**
 * Thrown when a method annotated with {@link Subscribe} has an invalid number of parameters.
 *
 * @author Brady
 * @since 3.0.0
 */
public class ListenerMethodException extends ListenerDiscoveryException {

    public ListenerMethodException(String message) {
        super(message);
    }
}
