package io.github.townyadvanced.setting;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a node with specific type of value in a configuration file.
 *
 * @param <T> The type of the node value.
 */
public interface TypedNode<T> extends Node {
    /**
     * Gets the class type {@link T} of the node value.
     *
     * @return The class type of the node value.
     */
    @NotNull Class<T> getType();

    /**
     * Gets the default value with type {@link T} of the node.
     *
     * @return The default value of the node.
     */
    @Nullable T getDefaultValue();

    /**
     * Gets the validator with type {@link T} of the node.
     *
     * @return The validator of the node, or null if there is no validator.
     */
    @Nullable Predicate<T> getTypedValidator();
}