<div align="center">

# AlpineFork
A lightweight event system for Java 8+

MODIFIED by Bat

Every class will have an author tag as to who made it

</div>

## Setup

You need the [Impact Development Maven](https://impactdevelopment.github.io/maven) repo.

### Gradle

```gradle
dependencies {
    compile 'com.github.ZeroMemes:Alpine:3.1.0'
}
```

## Usage

See the original repo to setup the event bus. The main thing added is an extendable ``Event`` class that is used to create listeners.

```java
public class EventRender2D extends Event {
    private final ScaledResolution sr;
    private float partialTicks;
}
```

All you need to do now is post the event, and you have all the information you need. The Event's Direction, and Phase;
