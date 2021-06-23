package io.github.willqi.filewatchupload.connection;

import io.github.willqi.filewatchupload.config.data.SimpleSSHConfig;
import net.schmizz.sshj.SSHClient;

import java.io.IOException;

public class SimpleSSHConnection extends SSHConnection {

    private final SimpleSSHConfig config;


    public SimpleSSHConnection(SimpleSSHConfig config) {
        super(config);
        this.config = config;
    }

    @Override
    protected boolean authenticate (SSHClient client) {
        try {
            client.authPassword(this.config.getUsername(), this.config.getPassword());
        } catch (IOException exception) {
            return false;
        }
        return client.isAuthenticated();
    }
}
