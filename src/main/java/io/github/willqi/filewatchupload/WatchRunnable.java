package io.github.willqi.filewatchupload;

import io.github.willqi.filewatchupload.listener.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class WatchRunnable implements Runnable {

    private final WatchManager watchManager;
    private final File watchFile;

    public WatchRunnable(WatchManager watchManager, File watchFile) {
        this.watchManager = watchManager;
        this.watchFile = watchFile;
    }

    @Override
    public void run() {
        Listener listener = this.watchManager.getListener();

        while (!Thread.interrupted()) {
            WatchKey key;
            try {
                key = this.fetchKey();
            } catch (IOException exception) {
                listener.onWatchFailure("Failed to fetch WatchKey", exception);
                return;
            }

            // The key becomes invalid if the directory is deleted.
            while (key.isValid()) {

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (!event.equals(StandardWatchEventKinds.OVERFLOW)) {
                        // File created/changed
                        WatchEvent<Path> watchEvent = (WatchEvent<Path>)event;
                        File modifiedFile = new File(
                                Paths.get(watchFile.getParentFile().getAbsolutePath(),
                                watchEvent.context().toString()).toString()
                        );

                        // Should we upload the modified file
                        if (watchFile.getName().equals(modifiedFile.getName()) && watchFile.isFile()) {
                            listener.onPreUpload(modifiedFile);
                            if (this.watchManager.getConnection().upload(modifiedFile)) {
                                listener.onUploadSuccess(modifiedFile);
                            } else {
                                listener.onUploadFailure(modifiedFile);
                            }
                        }
                    }
                }
                key.reset();
            }

            // WatchKey is no longer valid.
            if (!Thread.interrupted()) {
                try {
                    Thread.sleep(1000); // The folder was deleted. Wait a bit before getting the next WatchKey
                } catch (InterruptedException exception) {
                    listener.onWatchFailure("Interrupted while attempting to fetch new WatchKey", exception);
                    return;
                }
            }

        }

    }

    private WatchKey fetchKey() throws IOException {
        WatchService service = FileSystems.getDefault().newWatchService();
        WatchKey key = watchFile
                .getParentFile()
                .toPath()
                .register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        return key;
    }

}
