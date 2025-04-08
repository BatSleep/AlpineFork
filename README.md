<div align="center">

# AlpineFork
A lightweight event system for Java 8+

modernized by Bat

Every class will have an author tag as to who made it

</div>

## Setup

Download the latest release from `Releases`, and add to your project.

## Usage

See the original repo to setup the event bus. The main thing added is an extendable ``Event`` class that is used to create listeners.

```java
//Im using lombok with this example

@Getter
@Setter
@AllArgsConstructor
public class EventRender2D extends Event {
    private final ScaledResolution sr;
    private float partialTicks;
}
```

All you need to do now is post the event, and you have all the information you need + The Event's Direction, and Phase.
