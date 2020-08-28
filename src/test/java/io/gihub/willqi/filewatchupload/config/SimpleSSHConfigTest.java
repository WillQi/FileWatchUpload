package io.gihub.willqi.filewatchupload.config;

import io.github.willqi.filewatchupload.config.SimpleSSHConfig;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleSSHConfigTest {

    private static final String CONFIG_FILE_NAME = "filewatchupload.config";

    private static final String CONFIG_FILE_PATH = Paths.get(System.getProperty("user.dir"), "test", CONFIG_FILE_NAME).toString();

    @Test
    public void loadConfigurationFileShouldThrowIfNoConfigurationFile () {
        try {
            this.resetTestDirectory();
        } catch (IOException exception) {
            exception.printStackTrace();
            fail("Failed to create test directory");
            return;
        }
        assertThrows(FileNotFoundException.class, () -> new SimpleSSHConfig(CONFIG_FILE_PATH));
    }

    @Test
    public void loadConfigurationFileShouldCreateConfigIfNoConfigurationFile () {
        try {
            this.resetTestDirectory();
        } catch (IOException exception) {
            exception.printStackTrace();
            fail("Failed to create test directory");
            return;
        }
        try {
            new SimpleSSHConfig(CONFIG_FILE_PATH);
        } catch (FileNotFoundException exception) {}
        File configFile = new File(CONFIG_FILE_PATH);
        assertTrue(configFile.exists());
    }

    @Test
    public void loadConfigurationFileShouldLoadConfigIfConfigurationFileExists () {
        try {
            this.resetTestDirectory();
            this.createTestConfig();
        } catch (IOException exception) {
            exception.printStackTrace();
            fail("Failed to create test directory/confg");
            return;
        }
        final SimpleSSHConfig config;
        try {
            config = new SimpleSSHConfig(CONFIG_FILE_PATH);
        } catch (FileNotFoundException exception) {
            fail("Failed to find config file.");
            return;
        }
        assertEquals(config.getUsername(), "success");
    }



    private void createTestConfig () throws IOException {
        File configFile = new File(Paths.get(System.getProperty("user.dir"), "test", CONFIG_FILE_NAME).toString());
        if (configFile.createNewFile()) {
            Properties propertyFile = new Properties();

            propertyFile.put("username", "success");

            OutputStream configStream = new FileOutputStream(configFile);
            propertyFile.store(configStream, null);
            configStream.close();
        }
    }

    private void resetTestDirectory () throws IOException {
        File testDirectory = new File(Paths.get(System.getProperty("user.dir"), "test").toString());
        if (testDirectory.exists()) {
            for (File file : testDirectory.listFiles()) {
                file.delete();
            }
        }
        testDirectory.mkdir();
    }

}
