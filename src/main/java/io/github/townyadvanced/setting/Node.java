package io.github.townyadvanced.setting;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a node in a configuration file.
 */
public interface Node {
    /**
     * Gets the YAML path of the node.
     *
     * @return The YAML path of the node.
     */
    @NotNull String getPath();
    /**
     * Gets the default value of the node.
     *
     * @return The default value of the node.
     */
    @Nullable Object getDefaultValue();

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
    @Nullable Predicate<Object> getValidator();
}
