# CommentedConfiguration

A yaml configuration system that supports comments, automatic editing of existing config comments, automatic adding of new config nodes.

## Importing CommentedConfiguration into your project

Maven:
```
  <repositories>
    <repository>
      <id>glaremasters repo</id>
      <url>https://repo.glaremasters.me/repository/towny/</url>
    </repository>
  </repositories>
```

```
  <dependency>
    <groupId>io.github.townyadvanced.commentedconfiguration</groupId>
    <artifactId>CommentedConfiguration</artifactId>
    <version>1.0.0</version>
  </dependency>
```

Gradle:

```
repositories {
    maven {
        name = 'glaremasters repo'
        url = 'https://repo.glaremasters.me/repository/towny/'
    }

dependencies {
    compileOnly 'io.github.townyadvanced.commentedconfiguration:CommentedConfiguration:1.0.0'
}
```


## Usage

CommentedConfiguration can be used via the included Settings class, or using your own settings-replacement similar to what Towny uses: [settings](https://github.com/TownyAdvanced/Towny/blob/master/src/com/palmergames/bukkit/towny/TownySettings.java), [enum](https://github.com/TownyAdvanced/Towny/blob/master/src/com/palmergames/bukkit/config/ConfigNodes.java).

To use the Settings you need to provide three things:
- A path where your file will be saved.
- A Logger or Plugin instance for CommentedConfiguration to use when it needs to log information. 
- A List of CommentedNodes which will be used to create your config.

Example [Nodes Class](https://github.com/TownyAdvanced/CommentedConfiguration/blob/main/src/test/java/io/github/townyadvanced/commentedconfiguration/TestNodes.java) from which the Settings examples below grabs the CommentedNodes list using #getAllNodes.

The included Settings class comes with the following constructors:
https://github.com/TownyAdvanced/CommentedConfiguration/blob/7c1122ae3e9edcd4bd61b4e1b4f9d21a3268917e/src/main/java/io/github/townyadvanced/commentedconfiguration/setting/Settings.java#L22-L46

Example initialization in your plugin Settings class:
```java
private final File configFile = new File("path/to/config.yml"); // The destination you want your config.yml saved to.
private Settings settings;

// Settings initialized with a supplied logger & a list of CommentedNodes.
settings = new Settings(configFile.toPath(), Logger.getLogger("YourLoggerNameHere"), TestNodes.getAllNodes());

// Settings initialized with a supplied plugin & a list of CommentedNodes.
settings = new Settings(configFile.toPath(), yourPluginInstance, TestNodes.getAllNodes());
```

After which, you can load and then save your file to the server using:
```java
settings.load();
settings.save();
```

From that point you can get your config file's values using the following methods, opting to use generic or typed lookups:
https://github.com/TownyAdvanced/CommentedConfiguration/blob/7c1122ae3e9edcd4bd61b4e1b4f9d21a3268917e/src/main/java/io/github/townyadvanced/commentedconfiguration/setting/Settings.java#L108-L139

You can also save settings back to the config using the following methods, just remember to save your settings field afterwards using settings.save();
https://github.com/TownyAdvanced/CommentedConfiguration/blob/7c1122ae3e9edcd4bd61b4e1b4f9d21a3268917e/src/main/java/io/github/townyadvanced/commentedconfiguration/setting/Settings.java#L141-L160

### Further example

<details><summary>Click to view</summary>

Main
```java
package org.example;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Logger;

import io.github.townyadvanced.commentedconfiguration.setting.Settings;

public class Main {
    public static void main(String[] args) {
        final Path file = Path.of("/home/ben10/Desktop/config.yml");
        final Settings settings = new Settings(file, Logger.getLogger("TEST"), TestNodes.getAllNodes());
        if (!settings.load()) {
            System.out.println("Failed to load config");
            return;
        }
        System.out.println("Loaded config");
        System.out.println(settings.get(TestNodes.BOOLEAN_NODE));
        System.out.println(settings.get(TestNodes.STRING_NODE));
        System.out.println(settings.get(TestNodes.LOCATION_NODE));
        settings.save();
        System.out.println("Saved config");

        final Path enumFile = Path.of("/home/ben10/Desktop/enumconfig.yml");
        final Settings enumSettings = new Settings(enumFile, Logger.getLogger("ENUMTEST"), Arrays.asList(TestEnumNodes.values()));
        if (!enumSettings.load()) {
            System.out.println("Failed to load enumconfig");
            return;
        }
        System.out.println("Loaded enumconfig");
        System.out.println(enumSettings.get(TestEnumNodes.TEST_BOOLEAN));
        System.out.println(enumSettings.get(TestEnumNodes.TEST_STRING));
        System.out.println(enumSettings.get(TestEnumNodes.TEST_LOCATION));
        enumSettings.save();
        System.out.println("Saved enumconfig");
    }
}
```

TestEnumNodes
```java
package org.example;

import io.github.townyadvanced.commentedconfiguration.setting.ValueNode;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TestEnumNodes implements ValueNode {
    TEST_BOOLEAN(
            "test.boolean",
            true,
            new String[] {"# This is a test boolean.", "# It is true by default."}
    ),
    TEST_STRING(
            "test.string",
            "Hello, world!",
            new String[] {"# This is a test string.", "# It is \"Hello, world!\" by default."}
    ),
    TEST_LOCATION(
            "test.location",
            new Location(null, 0, 0, 0),
            new String[] {"# This is a test location.", "# It is (0, 0, 0) by default."}
    ),
    ;

    private final String path;
    private final Object defaultValue;
    private final String[] comments;

    TestEnumNodes(String path, Object defaultValue, String[] comments) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    @Override
    public @NotNull String getPath() {
        return path;
    }

    @Override
    public @Nullable Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public @NotNull String[] getComments() {
        return comments;
    }
}
```

TestNodes
```java
package org.example;

import java.util.ArrayList;
import java.util.List;

import io.github.townyadvanced.commentedconfiguration.setting.CommentedNode;
import io.github.townyadvanced.commentedconfiguration.setting.SimpleNode;
import io.github.townyadvanced.commentedconfiguration.setting.TypedValueNode;
import org.bukkit.Location;

public class TestNodes {
    private static final List<CommentedNode> nodes = new ArrayList<>();

    private static <T> TypedValueNode<T> node(TypedValueNode<T> node) {
        nodes.add(node);
        return node;
    }

    public static final TypedValueNode<Boolean> BOOLEAN_NODE = node(SimpleNode.builder("test.boolean", Boolean.class)
            .defaultValue(true)
            .comment("This is a boolean")
            .build());

    public static final TypedValueNode<String> STRING_NODE = node(SimpleNode.builder("test.string", String.class)
            .defaultValue("default")
            .comment("This is a string")
            .build());

    public static final TypedValueNode<Location> LOCATION_NODE = node(SimpleNode.builder("test.location", Location.class)
            .defaultValue(new Location(null, 0, 0, 0))
            .comment("This is a location")
            .build());

    public static List<CommentedNode> getAllNodes() {
        return nodes;
    }
}
```


</details>

## Converting Existing Configs To CommentedConfiguration

@ipiepiepie has created a python script that will convert configuration files into CommentedConfigurations.

It is available on their [CommentedConfigurationMigrator repo](https://github.com/ipiepiepie/CommentedConfigurationMigrator), make sure you check it out if you aren't starting from scratch.
  
## History

CommentedConfiguration goes *waaay back* to the original days of Bukkit plugins. Originally devised by [dumptruckman](https://github.com/dumptruckman) who used it in his [PluginBase](https://github.com/dumptruckman/PluginBase), it was [added into Towny](https://github.com/TownyAdvanced/Towny/commit/9de37765a69c92d9fe8ffe94cb62c5c7f250c6c5) in August of 2011. It was maintained over the years in the Towny codebase, receiving a number of updates that kept it working as Bukkit developed. Even after Bukkit's native yaml configuration [received the ability to handle comments in late 2021](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/commits/3e2dd2bc120754ea4db193e878050d0eb31a6894#src/main/java/org/bukkit/configuration/file/YamlConfiguration.java), the CommentedConfiguration system is still superior for its ability to update the config's existing comments as your plugin updates.
