package io.github.willqi.filewatchupload.config.data;

import io.github.willqi.filewatchupload.config.ConfigType;

public abstract class Config {

    private String[] outputDirectories = new String[0];


    public abstract ConfigType getType();

    public String[] getOutputDirectories() {
        return this.outputDirectories;
    }

    public void setOutputDirectories(String[] outputDirectories) {
        this.outputDirectories = outputDirectories;
    }

}