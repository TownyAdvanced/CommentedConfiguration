package io.github.townyadvanced.commentedconfiguration.setting;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Basic implementation of {@link TypedValueNode} with a builder.
 *
 * @param <T> The type of the node value.
 */
public class SimpleNode<T> implements TypedValueNode<T> {

    /**
     * A builder for {@link SimpleNode}.
     *
     * @param path  The path of the node.
     * @param type  The class type of the node value.
     * @return A new builder.
     * @param <T> The type of the node value.
     */
    public static <T> Builder<T> builder(@NotNull String path, @NotNull Class<T> type) {
        return new Builder<>(path, type);
    }

    private final String path;
    private final Class<T> type;
    private final T defaultValue;
    private final String[] comments;

    /**
     * Creates a new node with the given path, type, default value, comment, and validator.
     *
     * @param path          The path of the node.
     * @param type          The class type of the node value.
     * @param defaultValue  The default value of the node.
     * @param comments      The comments of the node.
     */
    public SimpleNode(@NotNull String path, @NotNull Class<T> type, @Nullable T defaultValue, @NotNull String[] comments) {
        this.path = path;
        this.type = type;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getPath() {
        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Class<T> getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable T getDefaultValue() {
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String[] getComments() {
        return comments;
    }

    /**
     * A builder for {@link SimpleNode}s.
     *
     * @param <T> The type of the node value.
     */
    public static class Builder<T> {
        private final String path;
        private final Class<T> type;
        private T defaultValue;
        private final List<String> comments;

        /**
         * Creates a new builder with the given path and type.
         *
         * @param path  The path of the node.
         * @param type  The class type of the node value.
         */
        protected Builder(@NotNull String path, @NotNull Class<T> type) {
            this.path = path;
            this.type = type;
            this.comments = new ArrayList<>();
        }

        /**
         * Sets the default value of the node.
         *
         * @param defaultValue  The default value of the node.
         * @return This builder.
         */
        public Builder<T> defaultValue(@Nullable T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        /**
         * Adds a comment line to the node.
         * Automatically adds a comment prefix "#" if the comment doesn't start with one.
         *
         * @param comment  The comment to add.
         * @return This builder.
         */
        public Builder<T> comment(@NotNull String comment) {
            if (!comment.isEmpty() && !comment.startsWith("#")) {
                // Automatically add a comment prefix if the comment doesn't start with one.
                comment = "# " + comment;
            }
            this.comments.add(comment);
            return this;
        }

        /**
         * Builds the node.
         *
         * @return The node.
         */
        public SimpleNode<T> build() {
            return new SimpleNode<>(path, type, defaultValue, comments.toArray(new String[0]));
        }
    }
}
