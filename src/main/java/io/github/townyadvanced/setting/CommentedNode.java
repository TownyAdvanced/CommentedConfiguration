package io.github.townyadvanced.setting;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a node that has comments in a configuration file.
 */
public interface CommentedNode {
    /**
     * Gets the YAML path of the node.
     *
     * @return The YAML path of the node.
     */
    @NotNull String getPath();

    /**
     * Gets the comment of the node.
     *
     * @return The comment of the node.
     */
    @NotNull String[] getComments();
}
