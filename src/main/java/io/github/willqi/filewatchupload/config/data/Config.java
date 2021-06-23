package io.github.willqi.filewatchupload.config.data;

import io.github.willqi.filewatchupload.config.ConfigType;

public interface Config {

    ConfigType getId();

    String[] getOutputDirectories();

}