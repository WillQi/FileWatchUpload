package io.gihub.willqi.filewatchupload;

import io.github.willqi.filewatchupload.WatchManager;
import io.github.willqi.filewatchupload.connection.Connection;
import io.github.willqi.filewatchupload.listener.SilentListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


import java.io.*;
import java.nio.file.Paths;

public class WatchManagerTest {

    private static final String TEST_DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "test").toString();
    private static final String TEST_FILE_PATH = Paths.get(TEST_DIRECTORY_PATH, "correct.txt").toString();
    private static final String INCORRECT_TEST_FILE_PATH = Paths.get(TEST_DIRECTORY_PATH, "incorrect.txt").toString();

    @Test
    public void shouldUploadIfFileChangeIsMadeAndConfigurationIsListeningForFolder () {
        try {
            this.resetTestDirectory();
        } catch (IOException exception) {
            fail("Failed to create test folder structure");
            return;
        }

        Connection mockConnection = mock(Connection.class);

        final WatchManager watchManager;
        try {
            watchManager = new WatchManager(
                    new WatchManager.WatchPathConfig(TEST_DIRECTORY_PATH, ""),
                    mockConnection,
                    new SilentListener()
            );
        } catch (FileNotFoundException exception) {
            fail("Cannot test without real watch path");
            return;
        }
        watchManager.watch();

        try {
            Thread.sleep(500); // Should be enough time to watch.
        } catch (InterruptedException exception) {
            fail("Was interrupted");
            return;
        }

        File correctFile = new File(TEST_FILE_PATH);
        try {
            correctFile.createNewFile();
        } catch (IOException exception) {
            fail("Failed to create test file");
            return;
        }

        try {
            Thread.sleep(2000); // Should be enough time to get updates.
        } catch (InterruptedException exception) {
            fail("Was interrupted");
            return;
        }
        watchManager.stop();

        verify(mockConnection, times(1)).upload(any(File.class), anyString());

    }

    @Test
    public void shouldUploadIfCorrectFileChangeIsMadeAndConfigurationIsListeningForFile () {
        try {
            this.resetTestDirectory();
        } catch (IOException exception) {
            exception.printStackTrace();
            fail("Failed to create test folder structure");
            return;
        }

        File correctFile = new File(TEST_FILE_PATH);
        try {
            correctFile.createNewFile();
        } catch (IOException exception) {
            fail("Failed to create correct test file");
            return;
        }

        Connection mockConnection = mock(Connection.class);


        final WatchManager watchManager;
        try {
            watchManager = new WatchManager(
                    new WatchManager.WatchPathConfig(TEST_FILE_PATH, ""),
                    mockConnection,
                    new SilentListener()
            );
        } catch (FileNotFoundException exception) {
            fail("Cannot test without real watch path");
            return;
        }
        watchManager.watch();
        try {
            Thread.sleep(500); // Should be enough time to watch.
        } catch (InterruptedException exception) {
            fail("Was interrupted");
            return;
        }

        try {
            FileOutputStream stream = new FileOutputStream(correctFile);
            stream.write(0);
            stream.close();
        } catch (IOException exception) {
            fail("Failed to write to correct test file.");
            return;
        }

        File incorrectFile = new File(INCORRECT_TEST_FILE_PATH);
        try {
            incorrectFile.createNewFile();
        } catch (IOException exception) {
            fail("Failed to create incorrect test file");
            return;
        }

        try {
            Thread.sleep(2000); // Should be enough time to get updates.
        } catch (InterruptedException exception) {
            fail("Was interrupted");
            return;
        }
        watchManager.stop();

        verify(mockConnection, times(1)).upload(any(File.class), anyString());

    }

    private void resetTestDirectory () throws IOException {
        File testDirectory = new File(TEST_DIRECTORY_PATH);
        if (testDirectory.exists()) {
            for (File file : testDirectory.listFiles()) {
                file.delete();
            }
        }
        testDirectory.mkdir();
    }

}
