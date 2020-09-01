package io.github.willqi.filewatchupload;

import io.github.willqi.filewatchupload.listener.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class WatchThread implements Runnable {

    private WatchManager watchManager;

    public WatchThread (WatchManager watchManager) {
        this.watchManager = watchManager;
    }

    @Override
    public void run() {
        File watchFile = this.watchManager.getWatchFile();
        Listener listener = this.watchManager.getListener();
        WatchKey key;
        try {
            WatchService service = FileSystems.getDefault().newWatchService();
            key = (watchFile.isDirectory() ? watchFile.toPath() : watchFile.getParentFile().toPath()).register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException exception) {
            listener.onWatchFailure("Failed to create watch service.");
            exception.printStackTrace();
            return;
        }

        while (key.isValid() && !Thread.interrupted()) {

            for ( WatchEvent<?> event : key.pollEvents()) {

                if (!event.equals(StandardWatchEventKinds.OVERFLOW)) {
                    WatchEvent<Path> e = (WatchEvent<Path>)event;
                    File modifiedFile;
                    if (watchFile.isDirectory()) {
                        modifiedFile = new File(Paths.get(watchFile.getAbsolutePath(), e.context().toString()).toString());
                    } else {
                        modifiedFile = new File(Paths.get(watchFile.getParentFile().getAbsolutePath(), e.context().toString()).toString());
                    }
                    if (modifiedFile.isFile() && (watchFile.isDirectory() || watchFile.getName().equals(modifiedFile.getName()))) {
                        // upload.
                        listener.onPreUpload(modifiedFile);
                        if (this.watchManager.getConnection().upload(modifiedFile, this.watchManager.getPathConfig().getOutputPath())) {
                            listener.onUploadSuccess(modifiedFile);
                        } else {
                            listener.onUploadFailure(modifiedFile);
                        }
                    }

                }
            }

            key.reset();

            try {
                Thread.sleep(1000); // Give other threads a chance to watch.
            } catch (InterruptedException exception) {
                return;
            }
        }
    }

}
