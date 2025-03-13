package dev.bat.alpinefork.event;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Extendable class for listeners.
 *
 * @author Bat
 */

@Getter
@Setter
public abstract class Event extends CancellableEvent{
    
    private volatile EventPhase phase;
    private volatile EventDirection dir;
    
    private boolean isPhase(EventPhase expectedPhase) {
        return Optional.ofNullable(phase).map(
                p -> p == expectedPhase).orElse(false
        );
    }
    
    private boolean isDirection(EventDirection expectedDir) {
        return Optional.ofNullable(dir).map(
                d -> d == expectedDir).orElse(false
        );
    }
    
    public boolean isPre() {
        return isPhase(EventPhase.PRE);}
    
    public boolean isPost() {
        return isPhase(EventPhase.POST);}
    
    public boolean isOn() {
        return isPhase(EventPhase.ON);}
    
    public boolean isIncoming() {
        return isDirection(EventDirection.INCOMING);}
    
    public boolean isOutgoing() {
        return isDirection(EventDirection.OUTGOING);}
    
}
