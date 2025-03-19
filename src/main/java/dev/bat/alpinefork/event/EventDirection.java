package dev.bat.alpinefork.event;

import lombok.AllArgsConstructor;

/**
 * Defines event direction.
 *
 * @author Bat
 */
@AllArgsConstructor
public enum EventDirection {
    INCOMING("Incoming Event"),
    OUTGOING("Outgoing Event");
    
    /**
     * Just for better human readability
     */
    private final String debug;
    public String getDebug() {return debug;}
}
