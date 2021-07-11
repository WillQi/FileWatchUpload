package io.github.willqi.filewatchupload.listener;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VerboseListener extends Listener {

    @Override
    public void onWatchFailure(String reason, Throwable exception) {
        System.out.println(getTimePrefix() + " Watch Failure: " + reason);
        exception.printStackTrace();
    }

    @Override
    public void onPreUpload(File modifiedFile) {
        System.out.println(getTimePrefix() + " File " + modifiedFile.getName() + " is being uploaded!");
    }

    @Override
    public void onUploadSuccess(File uploadedFile) {
        System.out.println(getTimePrefix() + " " + uploadedFile.getName() + " was uploaded!");
    }

    @Override
    public void onUploadFailure(File failedFile) {
        System.out.println(getTimePrefix() + " " + failedFile.getName() + " failed to be uploaded!");
    }

    private static String getTimePrefix() {
        LocalTime time = LocalTime.now();
        return "[" + time.format(DateTimeFormatter.ISO_TIME) + "]";
    }

}
