package io.gihub.willqi.filewatchupload;

import io.github.willqi.filewatchupload.WatchManager;
import io.github.willqi.filewatchupload.connection.Connection;
import io.github.willqi.filewatchupload.listener.SilentListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.fail;

public class WatchRunnableTest {

    @Test
    public void shouldUploadIfCorrectFileChangeIsMadeAndConfigurationIsListeningForFile (@TempDir Path tempDir) throws IOException, InterruptedException {
        Connection mockConnection = new Connection() {
            @Override
            public boolean upload(File file) {
                if (file.getName().equals("incorrect.txt")) {
                    fail("Uploaded incorrect.txt");
                }
                return true;
            }
        };

        WatchManager watchManager = new WatchManager(
                mockConnection,
                new SilentListener()
        );

        // Create file we are listening for
        File testFile = tempDir.resolve("correct.txt").toFile();
        testFile.createNewFile();

        Thread watchThread = watchManager.watch(testFile.toPath());
        Thread.sleep(500); // Should be enough time to watch.

        // Write a byte to the file to trigger the WatchManager
        FileOutputStream stream = new FileOutputStream(testFile);
        stream.write(0);
        stream.close();

        // Write another file that we are NOT listening for
        File incorrectFile = tempDir.resolve("incorrect.txt").toFile();
        incorrectFile.createNewFile();

        // Cleanup
        Thread.sleep(500); // Should be enough time to get updates and upload.
        watchThread.interrupt();    // Stop listening.

    }

}
