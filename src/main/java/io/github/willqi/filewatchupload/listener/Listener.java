package io.github.willqi.filewatchupload.listener;

import java.io.File;

public interface Listener {

    /**
     * Called when a failure occurs with watching a file/directory.
     * @param reason
     */
    void onWatchFailure (String reason);

    /**
     * Called when a file is to be uploaded
     * @param modifiedFile
     */
    void onPreUpload (File modifiedFile);

    /**
     * Called when a file was successfully uploaded
     * @param uploadedFile
     */
    void onUploadSuccess (File uploadedFile);

    /**
     * Called when a file failed to upload
     * @param failedFile
     */
    void onUploadFailure (File failedFile);

}
