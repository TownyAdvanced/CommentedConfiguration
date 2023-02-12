package io.github.townyadvanced.setting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a node that holds value in a configuration file.
 */
public interface ValueNode extends CommentedNode {
    /**
     * Gets the default value of the node.
     *
     * @return The default value of the node.
     */
    @Nullable Object getDefaultValue();
}
