package dev.bat.alpinefork.listener;

import dev.bat.alpinefork.bus.EventManager;
import dev.bat.alpinefork.event.EventPriority;
import dev.bat.alpinefork.event.Events;
import dev.bat.alpinefork.exception.EventTypeException;
import dev.bat.alpinefork.exception.ListenerTargetException;
import dev.bat.alpinefork.util.Util;
import lombok.Getter;
import net.jodah.typetools.TypeResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A Listener is an event callback wrapper that links event callbacks to their respective target types.
 * <p>
 * When using a method reference for the callback function, explicitly specifying the target event class may be
 * required. Consider the following example:
 * <pre>
 * public class EventHandler implements Subscriber {
 *   public EventHandler() {
 *     // Incorrect Usage
 *     App.EVENT_BUS.subscribe(new Listener&lt;String&gt;(this::doSomething));
 *   }
 *
 *   public void doSomething(Object object) { ... }
 * }
 * </pre>
 * Despite the explicit type parameter, the Listener target will be resolved as Object. (This is not an issue when the
 * Listener is a field, as the type parameter can be extracted from the generic signature). The solution to this is to
 * explicitly specify the target via a constructor parameter:
 * <pre>
 * public class EventHandler implements Subscriber {
 *   public EventHandler() {
 *     // Correct Usage
 *     App.EVENT_BUS.subscribe(new Listener&lt;&gt;(String.class, this::doSomething));
 *   }
 *
 *   public void doSomething(Object object) { ... }
 * }
 * </pre>
 *
 * @param <T> Target event type
 * @author Brady
 * @since 1.2
 *
 * Modified most methods
 * @author Bat
 *
 */
/*
 Even though IntelliJ warns about placing @NotNull on a type parameter here, it is actually sufficient for Kotlin to
 deduce a non-null type for all usages of T.
 */
public final class Listener<@NotNull T> implements Consumer<T>, Comparable<Listener<?>> {

    private static final Predicate<Object>[] EMPTY_FILTERS = new Predicate[0];

    /**
     * The type of the target event.
     */
    private Class<T> target;

    /**
     * The body of this {@link Listener}, called when all filters, if any, pass.
     */
    private final Consumer<T> callback;

    /**
     * Priority of this {@link Listener}.
     *
     *
     * -- GETTER --
     *  Returns the priority of this
     * . See
     *  for a description of this value.
     *
     @see EventPriority
     *
     */
    @Getter
    private final int priority;

    public Listener(@NotNull Consumer<T> callback) {
        this(null, callback, emptyFilters());
    }

    public Listener(@NotNull Consumer<T> callback, int priority) {
        this(null, callback, priority, emptyFilters());
    }

    public Listener(@Nullable Class<T> target, @NotNull Consumer<T> callback) {
        this(target, callback, emptyFilters());
    }

    public Listener(@Nullable Class<T> target, @NotNull Consumer<T> callback, int priority) {
        this(target, callback, priority, emptyFilters());
    }

    @SafeVarargs
    public Listener(@NotNull Consumer<T> callback, @NotNull Predicate<? super T>... filters) {
        this(null, callback, filters);
    }

    @SafeVarargs
    public Listener(@NotNull Consumer<T> callback, int priority, @NotNull Predicate<? super T>... filters) {
        this(null, callback, priority, filters);
    }

    @SafeVarargs
    public Listener(@Nullable Class<T> target, @NotNull Consumer<T> callback, @NotNull Predicate<? super T>... filters) {
        this(target, callback, EventPriority.DEFAULT, filters);
    }

    /**
     * Creates a new {@link Listener} instance.
     *
     * @param target   The target event type. If {@code null}, an attempt will be made to automatically resolve the target.
     * @param callback The event callback function.
     * @param priority The priority value. See {@link EventPriority}.
     * @param filters  Checks used to validate the event object before the {@code callback} is invoked.
     * @throws EventTypeException    If the event target isn't a {@link Events#validateEventType(Type) valid event type}.
     *                               This could happen unexpectedly if the target isn't specified, and the TypeResolver
     *                               resolves a generic superclass instead of the intended target. In this case, the event
     *                               target should be explicitly specified.
     * @throws IllegalStateException If the event target can't be automatically resolved from the callback
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public Listener(@Nullable Class<T> target, @NotNull Consumer<T> callback, int priority, @NotNull Predicate<? super T>... filters) {
        this.callback = Util.predicated(callback, filters);
        this.priority = priority;
        if (target != null) {
            this.target = target;
        } else {
            /*
              Ensures that resolved is actually a Class<?> before casting
             */
            final Type resolved = TypeResolver.resolveRawArgument(Consumer.class, callback.getClass());
            if (resolved instanceof Class<?>) {
                this.target = (Class<T>) resolved;
            } else {
                throw new IllegalStateException("Unable to resolve target type from callback: " + callback.getClass().getName());
            }
        }
    }

    /**
     * Sets the target event type of this {@link Listener}. Used by {@link EventManager} to correct the target type
     * by resolving directly from the field's type parameter, preventing the need to explicitly specify the target
     * type when using a method reference to a method whose parameter isn't the exact event type.
     *
     * @param target The new target
     * @throws EventTypeException      If the new target isn't a valid event type
     * @throws ListenerTargetException If the existing target isn't assignable from the new target
     */
    public void setTarget(@NotNull Class<T> target) {
        /*
        Ensures target is never null.
         */
        
	    Objects.requireNonNull(target, "Target type cannot be null.");
        Events.validateEventType(target);
        
        if (this.target != null && !this.target.isAssignableFrom(target)) {
            throw new ListenerTargetException("Current target type (" + this.target.getName() +
                    ") must be assignable from new target type (" + target.getName() + ")");
        }
        
        this.target = target;
    }

    /**
     * Returns the type of the event that is targeted by this {@link Listener}.
     *
     * @return The target event type
     */
    public @NotNull Class<T> getTarget() {
        return this.target;
    }
	
	/**
     * Called during the event posting sequence. Verifies that the event can be accepted by testing it against this
     * {@link Listener}'s filters, and if so, proceeds with passing the event to this {@link Listener}'s body function.
     *
     * @param event Event being posted
     */
    @Override
    public void accept(T event) {
        this.callback.accept(event);
    }

    @Override
    public int compareTo(@NotNull Listener<?> o) {
        /*
        Handles nulls safely
         */
        return Integer.compare(o.getPriority(), this.priority);
    }
    
    /*
    Used an unchecked cast...
     */
    private static <T> Predicate<? super T>[] emptyFilters() {
        return (Predicate<? super T>[]) EMPTY_FILTERS;
    }
}
