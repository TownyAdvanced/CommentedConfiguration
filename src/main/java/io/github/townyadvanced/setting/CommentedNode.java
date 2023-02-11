package io.github.townyadvanced.setting;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a node in a configuration file.
 *
 * @param <T> The type of the node value.
 */
public interface CommentedNode<T> {
    /**
     * Gets the YAML path of the node.
     *
     * @return The YAML path of the node.
     */
    @NotNull String getPath();

    /**
     * Gets the class type of the node value.
     *
     * @return The class type of the node value.
     */
    @NotNull Class<T> getType();

    /**
     * Gets the default value of the node.
     *
     * @return The default value of the node.
     */
    @Nullable T getDefaultValue();

    /**
     * Gets the comment of the node.
     *
     * @return The comment of the node.
     */
    @NotNull String[] getComments();

    /**
     * Gets the validator of the node.
     *
     * @return The validator of the node, or null if there is no validator.
     */
    @Nullable Predicate<T> getValidator();
}
