package io.github.willqi.filewatchupload.connection;

import java.io.File;

public interface Connection {

    /**
     * Uploads a file.
     * @param file The file to upload.
     * @return if the upload was successful
     */
    boolean upload (File file);

}
