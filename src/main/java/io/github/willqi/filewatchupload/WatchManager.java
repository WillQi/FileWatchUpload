package io.github.willqi.filewatchupload;

import io.github.willqi.filewatchupload.connection.Connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class WatchManager {

    private final File watchFile;

    private final String outputPath;

    private final Connection connection;

    private AtomicBoolean watching = new AtomicBoolean(false);

    private Thread watchThread;

    public WatchManager (String watchPath, String outputPath, Connection connection) throws FileNotFoundException {
        this.outputPath = outputPath;
        this.connection = connection;

        // Ensure the watch path exists.
        this.watchFile = new File(watchPath);
        if (!this.watchFile.exists()) {
            throw new FileNotFoundException("The watch file/directory does not exist.");
        }

    }

    /**
     * Stops the watch thread if it is created.
     */
    public void stop () {
        this.watching.compareAndSet(true, false);
        this.join();
        this.watchThread = null;
    }

    /**
     * Returns if we are current watching.
     * @return
     */
    public boolean isWatching () {
        return this.watching.get();
    }

    /**
     * Resolves when the watch thread has ended.
     * @throws InterruptedException
     */
    public void join () {
        if (this.watchThread != null) {
            try {
                this.watchThread.join();
            } catch (InterruptedException exception) {
                System.out.println("WatchManager join was interrupted.");
            }
        }
    }


    /**
     * Start watching for file changes in the target directory
     */
    public void watch () {

        if (this.watchThread != null) {
            this.stop();
        }

        this.watching.compareAndSet(false, true);

        this.watchThread = new Thread(() -> {

            final WatchKey key;
            try {
                final WatchService service = FileSystems.getDefault().newWatchService();
                key = (this.watchFile.isDirectory() ? this.watchFile.toPath() : this.watchFile.getParentFile().toPath()).register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
            } catch (IOException e) {
                System.out.println("Failed to create watch service.");
                this.stop();
                return;
            }

            while (key.isValid() && this.isWatching()) {

                for (final WatchEvent<?> event : key.pollEvents()) {

                    if (!event.equals(StandardWatchEventKinds.OVERFLOW)) {
                        final WatchEvent<Path> e = (WatchEvent<Path>)event;
                        final File modifiedFile;
                        if (this.watchFile.isDirectory()) {
                            modifiedFile = new File(Paths.get(this.watchFile.getAbsolutePath(), e.context().toString()).toString());
                        } else {
                            modifiedFile = new File(this.watchFile.getAbsolutePath());
                        }
                        if (modifiedFile.isFile() && (this.watchFile.isDirectory() || this.watchFile.getName().equals(modifiedFile.getName()))) {
                            // upload.
                            System.out.println("New " + modifiedFile.getName() + " detected! Uploading...");
                            this.connection.upload(modifiedFile, this.outputPath);
                            System.out.println("Uploaded!");
                        }

                    }
                }

                key.reset();

                try {
                    Thread.sleep(1000); // Give other threads a chance to watch.
                } catch (InterruptedException exception) {
                    System.out.println("WatchManager sleep was interrupted");
                    this.stop();
                    return;
                }
            }


        });

        this.watchThread.start();


    }

}
