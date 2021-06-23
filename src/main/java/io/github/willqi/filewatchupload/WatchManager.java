package io.github.willqi.filewatchupload;

import io.github.willqi.filewatchupload.config.data.Config;
import io.github.willqi.filewatchupload.connection.Connection;
import io.github.willqi.filewatchupload.listener.Listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class WatchManager {

    private final Connection connection;
    private final Listener listener;

    public WatchManager (Connection connection, Listener listener) {
        this.connection = connection;
        this.listener = listener;
    }

    /**
     * Start watching for file changes in the target directory
     */
    public Thread watch (Path path) throws IOException {

        File watchFile = path.toFile();
        if (!watchFile.exists()) {
            throw new FileNotFoundException("Cannot find file to watch");
        }
        if (watchFile.isDirectory()) {
            throw new FileNotFoundException("The watched file cannot be a directory.");
        }

        Thread watchThread = new Thread(new WatchRunnable(this, watchFile));
        watchThread.start();

        return watchThread;

    }

    public Connection getConnection() {
        return this.connection;
    }

    public Listener getListener () {
        return this.listener;
    }

}
