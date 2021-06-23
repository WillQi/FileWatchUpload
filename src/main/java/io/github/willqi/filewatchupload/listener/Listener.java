package io.github.willqi.filewatchupload.listener;

import java.io.File;

public abstract class Listener {

    /**
     * Called when a failure occurs with watching a file/directory.
     * @param reason
     */
    public void onWatchFailure(String reason, Throwable exception) {}

    /**
     * Called when a file is to be uploaded
     * @param modifiedFile
     */
    public void onPreUpload(File modifiedFile) {}

    /**
     * Called when a file was successfully uploaded
     * @param uploadedFile
     */
    public void onUploadSuccess(File uploadedFile) {}

    /**
     * Called when a file failed to upload
     * @param failedFile
     */
    public void onUploadFailure(File failedFile) {}

}
