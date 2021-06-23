package io.github.willqi.filewatchupload.config.exceptions;

import java.io.IOException;

public class InvalidConfigurationException extends IOException {

    public InvalidConfigurationException(String message) {
        super(message);
    }

}
