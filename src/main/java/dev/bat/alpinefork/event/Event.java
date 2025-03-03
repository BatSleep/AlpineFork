package dev.bat.alpinefork.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Event extends CancellableEvent{
    
    private EventPhase phase;
    private EventDirection dir;
    
    public boolean isPre() {
        if (phase == null) return false;
        return phase == EventPhase.PRE;
    }
    
    public boolean isPost() {
        if (phase == null) return false;
        return phase == EventPhase.POST;
    }
    
    public boolean isOn() {
        if (phase == null) return false;
        return phase == EventPhase.ON;
    }
    
    public boolean isIncoming() {
        if (dir == null) return false;
        return dir == EventDirection.INCOMING;
    }
    
    public boolean isOutgoing() {
        if (dir == null) return false;
        return dir == EventDirection.OUTGOING;
    }


}
