import dev.bat.alpinefork.bus.EventBus;
import dev.bat.alpinefork.bus.EventManager;
import dev.bat.alpinefork.listener.Listener;
import dev.bat.alpinefork.listener.Subscribe;
import dev.bat.alpinefork.listener.Subscriber;

import java.util.function.Predicate;

public class JavaApplication implements Subscriber {

    public static final EventBus EVENT_BUS = EventManager.builder()
        .setName("my_application/root")
        .setSuperListeners()
        .build();

    public static void main(String[] args) {
        JavaApplication app = new JavaApplication();
        EVENT_BUS.subscribe(app);
        EVENT_BUS.post("Test"); // CharSequence listener prints "Test"
        EVENT_BUS.post("123");  // Both listeners print "123"
    }

    @Subscribe
    private final Listener<String> stringListener = new Listener<>(s -> {
        System.out.println("String: " + s);
    }, new LengthOf3Filter());

    @Subscribe
    private void onCharSequence(CharSequence s) {
        System.out.println("CharSequence: " + s);
    }

    public static class LengthOf3Filter implements Predicate<CharSequence> {

        @Override
        public boolean test(CharSequence t) {
            return t.length() == 3;
        }
    }
}
