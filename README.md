<div align="center">

# AlpineFork
A lightweight event system for Java 8+

MODIFIED by Bat

Every class will have an author tag as to who made it

</div>

## Setup

You need the Jitpack repo.

### Gradle

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

Adding the repository.

```gradle
dependencies {
    implementation 'com.github.BatSleep:AlpineFork:master'
}
```

## Usage

See the original repo to setup the event bus. The main thing added is an extendable ``Event`` class that is used to create listeners.

```java
@Getter
@Setter
@AllArgsConstructor
public class EventRender2D extends Event {
    private final ScaledResolution sr;
    private float partialTicks;
}
```

All you need to do now is post the event, and you have all the information you need. The Event's Direction, and Phase;
