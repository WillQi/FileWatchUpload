package io.github.willqi.filewatchupload.connection;

import java.io.File;

public interface Connection {

    /**
     * Uploads a file.
     * @param file The file to upload.
     * @param targetLocation The target location of the file
     * @return Whether or not the upload was a success.
     */
    boolean upload (File file, String targetLocation);

}
