package dev.bat.alpinefork.exception;

import dev.bat.alpinefork.event.Events;

/**
 * Thrown by {@link Events#validateEventType} when the specified type is not a valid event type.
 *
 * @author Brady
 * @since 3.0.0
 */
public class EventTypeException extends RuntimeException {

    public EventTypeException(String message) {
        super(message);
    }
}
