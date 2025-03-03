package dev.bat.alpinefork.bus;

import dev.bat.alpinefork.listener.*;
import dev.bat.alpinefork.listener.discovery.ListenerDiscoveryStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 * A builder class for {@link EventManager} and {@link AttachableEventManager}. A new instance of this class is created
 * and returned by {@link EventManager#builder()}. May be used to construct an {@link EventManager} with
 * {@link #build()}, or get passed to {@link EventManager#EventManager(EventBusBuilder)} when constructing a new
 * instance or defining a subclass.
 *
 * @author Brady
 * @since 2.0.0
 */
public final class EventBusBuilder<T extends EventBus> {

    // Default Settings
    private String name = null;
    private boolean parentDiscovery = false;
    private boolean superListeners = false;
    private ListenerExceptionHandler exceptionHandler = ListenerExceptionHandler.defaultHandler();
    private ListenerListFactory listenerListFactory = ListenerListFactory.defaultFactory();
    private final List<ListenerDiscoveryStrategy> discoveryStrategies = new ArrayList<>();
    private boolean attachable = false;

    EventBusBuilder() {
        this.discoveryStrategies.add(ListenerDiscoveryStrategy.subscribeFields());
        this.discoveryStrategies.add(ListenerDiscoveryStrategy.subscribeMethods());
    }

    /**
     * Sets the name of the {@link EventBus}.
     *
     * @param name The name
     * @return This builder
     * @see EventBus#name()
     * @since 2.0.0
     */
    public @NotNull EventBusBuilder<T> setName(@NotNull String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }

    /**
     * Enables parent {@link Listener} discovery, allowing superclasses of a {@link Subscriber} implementation (which
     * must also implement {@link Subscriber}) to be passed to the listener discovery strategies.
     *
     * @return This builder
     * @since 2.0.0
     */
    public @NotNull EventBusBuilder<T> setParentDiscovery() {
        this.parentDiscovery = true;
        return this;
    }

    /**
     * Enables {@link Listener} targeting an event's supertype to be invoked with the event in addition to
     * {@link Listener}s directly targeting the event type.
     *
     * @return This builder
     * @since 2.0.0
     */
    public @NotNull EventBusBuilder<T> setSuperListeners() {
        this.superListeners = true;
        return this;
    }

    /**
     * Sets the exception handler that will be invoked when an exception is thrown by a Listener. The specified
     * exception handler may be {@code null}, indicating that no explicit exception handling is to occur, and
     * exceptions should just propagate upwards.
     *
     * @param exceptionHandler The exception handler
     * @return This builder
     * @see ListenerExceptionHandler#defaultHandler()
     * @since 3.0.0
     */
    public @NotNull EventBusBuilder<T> setExceptionHandler(@Nullable ListenerExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * Disables exception handling. Equivalent to {@code setExceptionHandler(null)}.
     *
     * @return This builder
     * @since 3.0.0
     */
    public @NotNull EventBusBuilder<T> noExceptionHandler() {
        return this.setExceptionHandler(null);
    }

    /**
     * Sets the factory that will be used to create {@link ListenerList} objects for each event type.
     *
     * @param factory The factory
     * @return This builder
     * @see ListenerListFactory#defaultFactory()
     * @since 3.0.0
     */
    public @NotNull EventBusBuilder<T> setListenerListFactory(@NotNull ListenerListFactory factory) {
        Objects.requireNonNull(factory);
        this.listenerListFactory = factory;
        return this;
    }

    /**
     * Replaces the current list of discovery strategies with the specified strategies.
     *
     * @param strategies The new strategies
     * @return This builder
     * @see EventBusBuilder#addDiscoveryStrategies
     * @since 3.0.0
     */
    public @NotNull EventBusBuilder<T> setDiscoveryStrategies(@NotNull ListenerDiscoveryStrategy... strategies) {
        this.discoveryStrategies.clear();
        return this.addDiscoveryStrategies(strategies);
    }

    /**
     * Adds the specified strategies to the list of discovery strategies. The strategies that are included by default
     * are {@link ListenerDiscoveryStrategy#subscribeFields()} and {@link ListenerDiscoveryStrategy#subscribeMethods()}.
     *
     * @param strategies The strategies
     * @return This builder
     * @since 3.0.0
     */
    public @NotNull EventBusBuilder<T> addDiscoveryStrategies(@NotNull ListenerDiscoveryStrategy... strategies) {
        Collections.addAll(this.discoveryStrategies, strategies);
        return this;
    }

    /**
     * Causes this builder to create an {@link EventBus} which implements {@link AttachableEventBus}.
     *
     * @return This builder
     * @since 2.0.0
     */
    @SuppressWarnings("unchecked")
    public @NotNull EventBusBuilder<AttachableEventBus> setAttachable() {
        this.attachable = true;
        // Illegal? Maybe.
        return (EventBusBuilder<AttachableEventBus>) this;
    }

    /**
     * Returns a newly constructed {@link EventBus} instance using this builder.
     *
     * @since 2.0.0
     */
    @SuppressWarnings("unchecked")
    public @NotNull T build() {
        Objects.requireNonNull(this.name);
        return this.attachable
            ? (T) new AttachableEventManager(this)
            : (T) new EventManager(this);
    }

    /**
     * Returns the name
     * @since 3.0.0
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns {@code true} if parent class discovery is enabled
     * @since 3.0.0
     */
    public boolean isParentDiscovery() {
        return this.parentDiscovery;
    }

    /**
     * Returns {@code true} if super listeners are enabled
     * @since 3.0.0
     */
    public boolean isSuperListeners() {
        return this.superListeners;
    }

    /**
     * Returns an optional containing the exception handler, or {@link Optional#empty()} if none
     * @since 3.0.0
     */
    public Optional<ListenerExceptionHandler> getExceptionHandler() {
        return Optional.ofNullable(this.exceptionHandler);
    }

    /**
     * Returns the listener list factory
     * @since 3.0.0
     */
    public @NotNull ListenerListFactory getListenerListFactory() {
        return this.listenerListFactory;
    }

    /**
     * Returns the discovery strategies
     * @since 3.0.0
     */
    public @NotNull @UnmodifiableView List<ListenerDiscoveryStrategy> getDiscoveryStrategies() {
        return Collections.unmodifiableList(this.discoveryStrategies);
    }
}
