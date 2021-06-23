package io.github.willqi.filewatchupload.connection;

import io.github.willqi.filewatchupload.config.data.Config;
import io.github.willqi.filewatchupload.config.data.SimpleSSHConfig;

public class ConnectionResolver {

    public static Connection resolve(Config config) {
        switch (config.getType()) {
            case SIMPLE_SSH:
                return new SimpleSSHConnection((SimpleSSHConfig)config);
            default:
                return null;
        }
    }

}
