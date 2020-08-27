package io.github.willqi.filewatchupload.connection;

import java.io.File;

public interface Connection {

    void upload (File file, String targetLocation);

}
