# CommentedConfiguration

A yaml configuration system that supports comments, automatic editing of existing config comments, automatic adding of new config nodes.

## Importing CommentedConfiguration into your project

Instructions to follow when we have a repo.

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
