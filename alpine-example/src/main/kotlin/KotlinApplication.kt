import dev.bat.alpinefork.bus.EventManager
import dev.bat.alpinefork.listener.Listener
import dev.bat.alpinefork.listener.Subscribe
import dev.bat.alpinefork.listener.Subscriber
import java.util.function.Predicate

class KotlinApplication : Subscriber {

    @Subscribe
    private val stringListener = Listener<String>({
        println("String: $it")
    }, LengthOf3Filter())

    @Subscribe
    private fun onCharSequence(s: CharSequence) {
        println("CharSequence: $s")
    }

    class LengthOf3Filter : Predicate<CharSequence> {
        override fun test(t: CharSequence): Boolean {
            return t.length == 3
        }
    }
}

private val eventBus = EventManager.builder()
        .setName("my_application/root")
        .setSuperListeners()
        .build()

fun main() {
    val app = KotlinApplication()
    eventBus.subscribe(app)
    eventBus.post("Test") // CharSequence listener prints "Test"
    eventBus.post("123") // Both listeners print "123"
}
