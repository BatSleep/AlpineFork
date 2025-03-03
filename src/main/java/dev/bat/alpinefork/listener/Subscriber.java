package dev.bat.alpinefork.listener;

import dev.bat.alpinefork.bus.EventBus;

/**
 * An interface that must be implemented by a class in order for it to be subscribed to an {@link EventBus}. It does
 * not require any methods to be implemented, the only purpose is to make types containing {@link Listener}s explicit.
 *
 * @author Brady
 * @since 1.8
 */
public interface Subscriber {}
