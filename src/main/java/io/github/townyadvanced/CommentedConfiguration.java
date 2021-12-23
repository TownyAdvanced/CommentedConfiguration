package io.github.townyadvanced;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

/**
 * @author dumptruckman
 * @author Articdive
 * @author LlmDl
 */
public class CommentedConfiguration extends YamlConfiguration {
	private final HashMap<String, String> comments = new HashMap<>();
	private final Path path;
	private final Plugin plugin;

	private final DumperOptions yamlOptions = new DumperOptions();
	private final Representer yamlRepresenter = new YamlRepresenter();
	private final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);

	public CommentedConfiguration(Plugin plugin, Path path) {
		super();
		this.plugin = plugin;
		this.path = path;
		
		try {
			// Spigot 1.18.1 added SnakeYaml's ability to use Comments in yaml.
			// They have it enabled by default, we need to stop it happening.
			yamlOptions.setProcessComments(false);
		} catch (NoSuchMethodError ignored) {}
	}

	/**
	 * Load the yaml configuration file into memory.
	 * @return true if file is able to load.
	 */
	public boolean load() {
		return loadFile();
	}

	private boolean loadFile() {
		try {
			this.load(path.toFile());
			return true;
		} catch (InvalidConfigurationException | IOException e) {
			plugin.getLogger().warning(String.format("Loading error: Failed to load file %s (does it pass a yaml parser?).", path));
			plugin.getLogger().warning("https://jsonformatter.org/yaml-parser");
			plugin.getLogger().warning(e.getMessage());
			return false;
		}
	}

	/**
	 * Save the yaml configuration file from memory to file.
	 */
	public void save() {

		// Save the config just like normal
		boolean saved = saveFile();

		// if there's comments to add and it saved fine, we need to add comments
		if (!comments.isEmpty() && saved) {

			// String list of each line in the config file
			List<String> yamlContents;
			try {
				yamlContents = Files.readAllLines(path, StandardCharsets.UTF_8);
			} catch (IOException e) {
				plugin.getLogger().warning(String.format("Failed to read file %s.", path));
				plugin.getLogger().warning(e.getMessage());
				yamlContents = new ArrayList<>();
			}

			// This will hold the newly formatted line
			StringBuilder newContents = readConfigToString(yamlContents);

			// Write to file
			try {
				Files.write(path, newContents.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE);
			} catch (IOException e) {
				plugin.getLogger().warning(String.format("Saving error: Failed to write to file %s.", path));
				plugin.getLogger().warning(e.getMessage());
			}
		}
	}

	private boolean saveFile() {
		try {
			this.save(path.toFile());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private StringBuilder readConfigToString(List<String> yamlContents) {
		// This will hold the newly formatted line
		StringBuilder newContents = new StringBuilder();
		// This holds the current path the lines are at in the config
		String currentPath = "";
		// This flags if the line is a node or unknown text.
		boolean node;
		// The depth of the path. (number of words separated by periods - 1)
		int depth = 0;

		// Loop through the config lines
		for (String line : yamlContents) {
			// If the line is a node (and not something like a list value)
			if (line.contains(": ") || (line.length() > 1 && line.charAt(line.length() - 1) == ':')) {

				// This is a node so flag it as one
				node = true;

				// Grab the index of the end of the node name
				int index;
				index = line.indexOf(": ");
				if (index < 0) {
					index = line.length() - 1;
				}
				// If currentPath is empty, store the node name as the currentPath.
				if (currentPath.isEmpty()) {
					currentPath = line.substring(0, index);
				} else {
					// Calculate the whitespace preceding the node name
					int whiteSpace = 0;
					for (int n = 0; n < line.length(); n++) {
						if (line.charAt(n) == ' ') {
							whiteSpace++;
						} else {
							break;
						}
					}
					// Find out if the current depth (whitespace * 2) is greater/lesser/equal to the previous depth
					if (whiteSpace / 2 > depth) {
						// Path is deeper.  Add a . and the node name
						currentPath += "." + line.substring(whiteSpace, index);
						depth++;
					} else if (whiteSpace / 2 < depth) {
						// Path is shallower, calculate current depth from whitespace (whitespace / 2) and subtract that many levels from the currentPath
						int newDepth = whiteSpace / 2;
						for (int i = 0; i < depth - newDepth; i++) {
							currentPath = currentPath.replace(currentPath.substring(currentPath.lastIndexOf(".")), "");
						}
						// Grab the index of the final period
						int lastIndex = currentPath.lastIndexOf(".");
						if (lastIndex < 0) {
							// if there isn't a final period, set the current path to nothing because we're at root
							currentPath = "";
						} else {
							// If there is a final period, replace everything after it with nothing
							currentPath = currentPath.replace(currentPath.substring(currentPath.lastIndexOf(".")), "");
							currentPath += ".";
						}
						// Add the new node name to the path
						currentPath += line.substring(whiteSpace, index);
						// Reset the depth
						depth = newDepth;
					} else {
						// Path is same depth, replace the last path node name to the current node name
						int lastIndex = currentPath.lastIndexOf(".");
						if (lastIndex < 0) {
							// if there isn't a final period, set the current path to nothing because we're at root
							currentPath = "";
						} else {
							// If there is a final period, replace everything after it with nothing
							currentPath = currentPath.replace(currentPath.substring(currentPath.lastIndexOf(".")), "");
							currentPath += ".";
						}
						currentPath += line.substring(whiteSpace, index);
					}
				}
			} else {
				node = false;
			}

			if (node) {
				// If there's a comment for the current path, retrieve it and flag that path as already commented
				String comment = comments.get(currentPath);

				if (comment != null) {
					// Add the comment to the beginning of the current line
					line = comment + System.getProperty("line.separator") + line;
				}
			}
			// Add the line to the total config String
			newContents.append(line).append(System.getProperty("line.separator"));
		}
		
		/*
		 * Due to a Bukkit Bug with the Configuration we just need to remove any extra
		 * comments at the start of a file.
		 */
		while (newContents.toString().startsWith(" " + System.getProperty("line.separator")))
			newContents = new StringBuilder(newContents.toString().replaceFirst(" " + System.getProperty("line.separator"), ""));
		
		return newContents;
	}

	/**
	 * Adds a comment just before the specified path. The comment can be
	 * multiple lines. An empty string will indicate a blank line.
	 *
	 * @param path         Configuration path to add comment.
	 * @param commentLines Comments to add. One String per line.
	 */
	public void addComment(String path, String... commentLines) {

		StringBuilder commentstring = new StringBuilder();
		StringBuilder leadingSpaces = new StringBuilder();
		for (int n = 0; n < path.length(); n++) {
			if (path.charAt(n) == '.') {
				leadingSpaces.append("  ");
			}
		}
		for (String line : commentLines) {
			if (!line.isEmpty()) {
				line = leadingSpaces + line;
			} else {
				line = " ";
			}
			if (commentstring.length() > 0) {
				commentstring.append(System.getProperty("line.separator"));
			}
			commentstring.append(line);
		}
		comments.put(path, commentstring.toString());
	}

	@Override
	public String saveToString() {
		yamlOptions.setIndent(options().indent());
		yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		yamlOptions.setWidth(10000);
		yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

		String dump = yaml.dump(getValues(false));
		return dump.equals("{}\n") ? "" : dump;
	}
}