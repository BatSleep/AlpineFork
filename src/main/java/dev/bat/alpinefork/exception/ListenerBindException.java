package dev.bat.alpinefork.exception;

import dev.bat.alpinefork.listener.discovery.ListenerCandidate;

/**
 * Thrown by {@link ListenerCandidate#bind} when a candidate is unable to be bound.
 *
 * @author Brady
 * @since 3.0.0
 */
public class ListenerBindException extends RuntimeException {

    public ListenerBindException() {}

    public ListenerBindException(String message) {
        super(message);
    }

    public ListenerBindException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListenerBindException(Throwable cause) {
        super(cause);
    }
}
