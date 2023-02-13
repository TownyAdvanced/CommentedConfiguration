package io.github.townyadvanced.commentedconfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.common.collect.Lists;
import io.github.townyadvanced.commentedconfiguration.setting.Settings;
import io.github.townyadvanced.commentedconfiguration.setting.TypedValueNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Location;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestTypedValueNodesSettings {
    private static final File testdir = new File("bin/test/");

    private final File configFile = new File("bin/test/config.yml");
    private List<String> ogConfigFile;
    private List<String> savedConfigFile;
    private List<String> editedConfigFile;
    private Settings settings;

    @BeforeAll
    public static void setUpAll() {
        if (testdir.exists()) {
            testdir.delete();
        }
        testdir.mkdirs();
    }

    @AfterAll
    public static void tearDownAll() {
        if (testdir.exists()) {
            testdir.delete();
            testdir.getParentFile().delete();
        }
    }

    @BeforeEach
    public void setUp() throws IOException {
        if (configFile.exists()) {
            configFile.delete();
        }
        configFile.createNewFile();

        ogConfigFile = IOUtils.readLines(Objects.requireNonNull(this.getClass().getResourceAsStream("/og_config.yml")), StandardCharsets.UTF_8);
        savedConfigFile = IOUtils.readLines(Objects.requireNonNull(this.getClass().getResourceAsStream("/saved_config.yml")), StandardCharsets.UTF_8);
        editedConfigFile = IOUtils.readLines(Objects.requireNonNull(this.getClass().getResourceAsStream("/edited_config.yml")), StandardCharsets.UTF_8);

        FileUtils.writeLines(configFile, ogConfigFile);

        settings = new Settings(configFile.toPath(), Logger.getLogger("TestTypedValueNodesSettings"), TestNodes.getAllNodes());
        assertTrue(settings.load());
    }

    @AfterEach
    public void tearDown() {
        if (configFile.exists()) {
            configFile.delete();
        }
    }

    @Test
    @DisplayName("Save the configuration file with the comments and new values.")
    public void saveFile() throws IOException {
        settings.save();
        assertEquals(FileUtils.readLines(configFile, StandardCharsets.UTF_8), savedConfigFile);
    }

    @Test
    @DisplayName("Get nodes that were not set in the configuration file.")
    public void getDefaultNodes() {
        assertEquals(settings.get(TestNodes.INTEGER_NODE), 1234);
        assertEquals(Lists.newArrayList("one", "two", "three"), settings.get(TestNodes.LIST_NODE));
    }

    @Test
    @DisplayName("Get nodes that were set in the configuration file.")
    public void getNodes() {
        assertEquals(settings.get(TestNodes.STRING_NODE), "test");
        assertEquals(settings.get(TestNodes.BOOLEAN_NODE), true);
        assertEquals(settings.get(TestNodes.LOCATION_NODE), new Location(null, 1.0, 1.0, 1.0));
    }

    @Test
    @DisplayName("Set new values for nodes in the configuration file, and save the file.")
    public void setNodes() throws IOException {
        settings.set(TestNodes.STRING_NODE, "new string");
        settings.set(TestNodes.BOOLEAN_NODE, true);
        settings.set(TestNodes.LOCATION_NODE, new Location(null, 2.0, 2.0, 2.0));
        settings.set(TestNodes.INTEGER_NODE, 4321);
        settings.set(TestNodes.LIST_NODE, Lists.newArrayList("four", "five"));

        assertEquals(settings.get(TestNodes.STRING_NODE), "new string");
        assertEquals(settings.get(TestNodes.BOOLEAN_NODE), true);
        assertEquals(settings.get(TestNodes.LOCATION_NODE), new Location(null, 2.0, 2.0, 2.0));
        assertEquals(settings.get(TestNodes.INTEGER_NODE), 4321);
        assertEquals(Lists.newArrayList("four", "five"), settings.get(TestNodes.LIST_NODE));

        settings.save();
        assertEquals(FileUtils.readLines(configFile, StandardCharsets.UTF_8), editedConfigFile);
    }

    @Test
    @DisplayName("Test Nullable and NonNull annotations")
    public void instrumenter() {
        assertThrows(IllegalArgumentException.class, () -> settings.get(null));
        assertThrows(IllegalArgumentException.class, () -> settings.set(null, "test"));
    }
}
