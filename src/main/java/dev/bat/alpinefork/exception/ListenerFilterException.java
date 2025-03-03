package dev.bat.alpinefork.exception;

import dev.bat.alpinefork.listener.Subscribe;

/**
 * Thrown when any of the filter(s) specified by a {@link Subscribe} annotation are invalid.
 *
 * @author Brady
 * @since 3.1.0
 */
public class ListenerFilterException extends ListenerDiscoveryException {

    public ListenerFilterException(String message, Throwable cause) {
        super(message, cause);
    }
}
