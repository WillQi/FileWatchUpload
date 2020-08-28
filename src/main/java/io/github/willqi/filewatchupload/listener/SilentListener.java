package io.github.willqi.filewatchupload.listener;

import java.io.File;

public class SilentListener implements Listener {

    public void onWatchFailure (String reason) {}

    public void onPreUpload (File modifiedFile) {}

    public void onUploadSuccess (File uploadedFile) {}

    public void onUploadFailure (File failedFile) {}

}
