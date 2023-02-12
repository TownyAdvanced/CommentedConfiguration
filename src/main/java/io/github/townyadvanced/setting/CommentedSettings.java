package io.github.townyadvanced.setting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import io.github.townyadvanced.CommentedConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class that handles loading and saving of a CommentedConfiguration with {@link TypedNode}s.
 */
public class CommentedSettings {
    private final CommentedConfiguration config;
    private final Path configPath;
    private final List<Node> defaultNodes;

    /**
     * Creates a new CommentedSettings instance that makes use of CommentedConfiguration.
     *
     * @param configPath    The path to the configuration file.
     * @param plugin        The Plugin to get the logger from.
     * @param defaultNodes  The default node values to add to the configuration.
     */
    public CommentedSettings(@NotNull Path configPath,
                             @NotNull Plugin plugin,
                             @Nullable List<Node> defaultNodes
    ) {
        this.config = new CommentedConfiguration(configPath, plugin);
        this.configPath = configPath;
        this.defaultNodes = defaultNodes;
    }

    /**
     * Creates a new CommentedSettings instance that makes use of CommentedConfiguration.
     *
     * @param configPath    The path to the configuration file.
     * @param logger        The Logger to use for error messages.
     * @param defaultNodes  The default node values to add to the configuration.
     */
    public CommentedSettings(@NotNull Path configPath,
                             @Nullable Logger logger,
                             @Nullable List<Node> defaultNodes
    ) {
        this.config = new CommentedConfiguration(configPath, logger);
        this.configPath = configPath;
        this.defaultNodes = defaultNodes;
    }

    /**
     * Loads the configuration.
     *
     * @return True if the configuration was loaded successfully, false otherwise.
     */
    public boolean load() {
        if (!createConfigFile()) {
            return false;
        }
        if (!config.load()) {
            return false;
        }
        addDefaultNodes();
        return true;
    }

    /**
     * Create a new config file if file does not exist
     *
     * @return True if file exist or created successfully, otherwise false.
     */
    private boolean createConfigFile() {
        File configFile = configPath.toFile();
        if (configFile.exists()) {
            return true;
        }
        try {
            if (!configFile.createNewFile()) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Adds default node values to the configuration if they are not already present.
     */
    private void addDefaultNodes() {
        if (defaultNodes == null || defaultNodes.isEmpty()) {
            return;
        }
        for (Node node : defaultNodes) {
            Object nodeValue = config.get(node.getPath());
            if (nodeValue == null) {
                config.set(node.getPath(), node.getDefaultValue());
            }
            if (node.getComments().length > 0) {
                config.addComment(node.getPath(), node.getComments());
            }
        }
    }

    /**
     * Saves the configuration.
     */
    public void save() {
        config.save();
    }

    /**
     * Gets the value of a node, if the node has a default value, it will be returned if the node is not found.
     *
     * @param node  The node to get the value of.
     * @return The value of the node.
     */
    public Object get(@NotNull Node node) {
        return config.get(node.getPath(), node.getDefaultValue());
    }

    /**
     * Gets the value of a node, if the node has a default value, it will be returned if the node is not found.
     *
     * @param node  The node to get the value of.
     * @param type  The type of the node value.
     * @return The value of the node.
     * @param <T> The type of the node value.
     */
    public <T> T get(@NotNull Node node, Class<T> type) {
        return config.getObject(node.getPath(), type, (T) node.getDefaultValue());
    }

    /**
     * Gets the value of a node, if the node has a default value, it will be returned if the node is not found.
     *
     * @param node  The node to get the value of.
     * @return The value of the node.
     * @param <T> The type of the node value.
     */
    public <T> T get(@NotNull TypedNode<T> node) {
        return config.getObject(node.getPath(), node.getType(), node.getDefaultValue());
    }

    /**
     * Sets the value of a node, if the validator is not null, it will be tested first.
     *
     * @param node  The node to set the value of.
     * @param value The value to set.
     */
    public void set(@NotNull Node node, Object value) {
        config.set(node.getPath(), value);
    }

    /**
     * Sets the value of a node, if the validator is not null, it will be tested first.
     *
     * @param node  The node to set the value of.
     * @param value The value to set.
     * @param <T> The type of the node value.
     */
    public <T> void set(@NotNull TypedNode<T> node, T value) {
        config.set(node.getPath(), value);
    }

    /**
     * Gets the configuration object.
     *
     * @return The configuration object.
     */
    public @NotNull CommentedConfiguration getConfig() {
        return config;
    }
}
