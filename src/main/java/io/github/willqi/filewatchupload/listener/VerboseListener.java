package io.github.willqi.filewatchupload.listener;

import java.io.File;

public class VerboseListener extends Listener {

    @Override
    public void onWatchFailure(String reason, Throwable exception) {
        System.out.println("Watch Failure: " + reason);
        exception.printStackTrace();
    }

    @Override
    public void onPreUpload(File modifiedFile) {
        System.out.println("File " + modifiedFile.getName() + " is being uploaded!");
    }

    @Override
    public void onUploadSuccess(File uploadedFile) {
        System.out.println(uploadedFile.getName() + " was uploaded!");
    }

    @Override
    public void onUploadFailure(File failedFile) {
        System.out.println(failedFile.getName() + " failed to be uploaded!");
    }

}
