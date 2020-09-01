package io.github.willqi.filewatchupload;

import io.github.willqi.filewatchupload.connection.Connection;
import io.github.willqi.filewatchupload.listener.Listener;

import java.io.File;
import java.io.FileNotFoundException;

public class WatchManager {

    private final File watchFile;

    private final WatchPathConfig pathConfig;

    private final Connection connection;

    private Listener listener;

    public WatchManager (WatchPathConfig pathConfig, Connection connection, Listener listener) throws FileNotFoundException {
        this.pathConfig = pathConfig;
        this.connection = connection;
        this.listener = listener;

        // Ensure the watch path exists.
        this.watchFile = new File(pathConfig.getWatchPath());
        if (!this.watchFile.exists()) {
            throw new FileNotFoundException("The watch file/directory does not exist.");
        }

    }

    /**
     * Start watching for file changes in the target directory
     */
    public Thread watch () {

        Thread watchThread = new Thread(new WatchThread(this));
        watchThread.start();

        return watchThread;

    }

    public File getWatchFile () {
        return this.watchFile;
    }

    public Listener getListener () {
        return this.listener;
    }

    public Connection getConnection () {
        return this.connection;
    }

    public WatchPathConfig getPathConfig () {
        return this.pathConfig;
    }


    /**
     * Helper class to create watch path configuration
     */
    public static class WatchPathConfig {

        private final String watchPath;
        private final String outputPath;

        public String getWatchPath () {
            return this.watchPath;
        }

        public String getOutputPath () {
            return this.outputPath;
        }

        public WatchPathConfig (String watchPath, String outputPath) {
            this.watchPath = watchPath;
            this.outputPath = outputPath;
        }

    }

}
