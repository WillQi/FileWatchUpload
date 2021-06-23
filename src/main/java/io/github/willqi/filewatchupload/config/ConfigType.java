package io.github.willqi.filewatchupload.config;

import io.github.willqi.filewatchupload.config.parsers.ConfigParser;
import io.github.willqi.filewatchupload.config.parsers.SimpleSSHConfigParser;


public enum ConfigType {

    SIMPLE_SSH("simple_ssh", new SimpleSSHConfigParser());


    private final String id;
    private final ConfigParser parser;


    ConfigType(String id, ConfigParser parser) {
        this.id = id;
        this.parser = parser;
    }

    public String getId() {
        return this.id;
    }

    public ConfigParser getParser() {
        return this.parser;
    }


    public static ConfigType getTypeById(String id) {
        for (ConfigType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }

}
