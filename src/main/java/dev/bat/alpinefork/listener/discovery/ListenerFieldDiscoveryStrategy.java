package dev.bat.alpinefork.listener.discovery;

import dev.bat.alpinefork.event.Events;
import dev.bat.alpinefork.exception.ListenerBindException;
import dev.bat.alpinefork.exception.ListenerDiscoveryException;
import dev.bat.alpinefork.exception.ListenerFieldException;
import dev.bat.alpinefork.listener.Listener;
import dev.bat.alpinefork.listener.Subscribe;
import dev.bat.alpinefork.listener.Subscriber;
import dev.bat.alpinefork.util.Util;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Brady
 * @since 3.0.0
 */
enum ListenerFieldDiscoveryStrategy implements ListenerDiscoveryStrategy {
    INSTANCE;

    @Override
    public Stream<ListenerCandidate<?>> findAll(Class<? extends Subscriber> cls) {
        return getListenerFields(cls).map(ListenerFieldDiscoveryStrategy::asListener);
    }

    private static Stream<Field> getListenerFields(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields()).filter(ListenerFieldDiscoveryStrategy::isListenerField);
    }

    private static boolean isListenerField(Field field) {
        return field.isAnnotationPresent(Subscribe.class) // The field is allowed to be automatically subscribed
            && field.getType().equals(Listener.class)     // The field is of the correct type
            && !Modifier.isStatic(field.getModifiers());  // The field is an instance member
    }

    @SuppressWarnings("unchecked")
    private static <T> ListenerCandidate<T> asListener(Field field) {
        final Class<?> owner = field.getDeclaringClass();

        if (!(field.getGenericType() instanceof ParameterizedType)) {
            throw new ListenerFieldException("Listener fields must have a specified type parameter");
        }

        final Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

        // Validate the event type. If an exception is thrown, wrap it in ListenerDiscoveryException and rethrow it.
        final Class<T> target = (Class<T>) Util.catchAndRethrow(
            () -> Events.validateEventType(type),
            cause -> new ListenerDiscoveryException("Couldn't validate event type", cause)
        );

        return ListenerCandidate.single(instance -> {
            try {
                // Create a lookup in the owner class
                final MethodHandles.Lookup lookup = Util.getLookup().in(owner);
                // Read the field using the trusted lookup
                // (This should avoid setAccessible issues in future Java versions)
                final Listener<T> listener = (Listener<T>) Objects.requireNonNull(lookup.unreflectGetter(field).invoke(instance));
                listener.setTarget(target);
                return listener;
            } catch (Throwable e) {
                throw new ListenerBindException("Unable to bind Listener field", e);
            }
        });
    }
}
