package io.github.willqi.filewatchupload.config.data;

import io.github.willqi.filewatchupload.config.ConfigType;

public class SimpleSSHConfig extends RemoteConnectionConfig {

    private String username;
    private String password;


    @Override
    public ConfigType getType() {
        return ConfigType.SIMPLE_SSH;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { return this.password; }

    public void setPassword(String password) {
        this.password = password;
    }

}
