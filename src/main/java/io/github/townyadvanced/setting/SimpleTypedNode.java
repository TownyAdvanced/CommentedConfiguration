package io.github.townyadvanced.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Basic implementation of {@link TypedNode} with a builder.
 *
 * @param <T> The type of the node value.
 */
public class SimpleTypedNode<T> implements TypedNode<T> {

    /**
     * A builder for {@link SimpleTypedNode}.
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
    private final Predicate<T> validator;

    /**
     * Creates a new node with the given path, type, default value, comment, and validator.
     *
     * @param path          The path of the node.
     * @param type          The class type of the node value.
     * @param defaultValue  The default value of the node.
     * @param comments      The comments of the node.
     * @param validator     The validator of the node, or null if there is no validator.
     */
    public SimpleTypedNode(@NotNull String path,
                           @NotNull Class<T> type,
                           @Nullable T defaultValue,
                           @NotNull String[] comments,
                           @Nullable Predicate<T> validator
    ) {
        this.path = path;
        this.type = type;
        this.defaultValue = defaultValue;
        this.comments = comments;
        this.validator = validator;
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

    @Override
    public @Nullable Predicate<Object> getValidator() {
        // Using typed validator instead
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Predicate<T> getTypedValidator() {
        return validator;
    }

    /**
     * A builder for {@link SimpleTypedNode}s.
     *
     * @param <T> The type of the node value.
     */
    public static class Builder<T> {
        private final String path;
        private final Class<T> type;
        private T defaultValue;
        private final List<String> comments;
        private Predicate<T> validator;

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
         * Sets the validator of the node.
         *
         * @param validator The validator of the node.
         * @return This builder.
         */
        public Builder<T> validator(Predicate<T> validator) {
            this.validator = validator;
            return this;
        }

        /**
         * Builds the node.
         *
         * @return The node.
         */
        public SimpleTypedNode<T> build() {
            return new SimpleTypedNode<>(path, type, defaultValue, comments.toArray(new String[0]), validator);
        }
    }
}