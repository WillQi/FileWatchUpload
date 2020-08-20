package io.github.willqi;

import io.github.willqi.connection.Connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

public class WatchManager {

    private File watchFile;

    private String outputPath;

    private Connection connection;

    public WatchManager (String watchPath, String outputPath, Connection connection) throws FileNotFoundException {
        this.outputPath = outputPath;

        // Ensure the watch path exists.
        this.watchFile = new File(watchPath);
        if (!this.watchFile.exists()) {
            throw new FileNotFoundException("The watch file/directory does not exist.");
        }

    }

    public void watch () {

        WatchKey key;
        try {
            WatchService service = FileSystems.getDefault().newWatchService();
            System.out.println((this.watchFile.isDirectory() ? this.watchFile.toPath() : this.watchFile.getParentFile().toPath()));
            key = (this.watchFile.isDirectory() ? this.watchFile.toPath() : this.watchFile.getParentFile().toPath()).register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (key.isValid()) {

            for (WatchEvent<?> event : key.pollEvents()) {

                if (!event.equals(StandardWatchEventKinds.OVERFLOW)) {
                    WatchEvent<Path> e = (WatchEvent<Path>)event;
                    File modifiedFile = new File(Paths.get(this.watchFile.getAbsolutePath(), e.context().toString()).toString());
                    if (modifiedFile.isFile() && (this.watchFile.isDirectory() || this.watchFile.getName().equals(modifiedFile.getName()))) {
                        // upload.
                        System.out.println("Uploading...");
                    }

                }
            }

            key.reset();
        }

    }

}
