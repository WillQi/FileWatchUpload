package io.github.willqi.filewatchupload.connection;

import net.schmizz.sshj.SSHClient;

import java.io.IOException;

public class SimpleSSHConnection extends SSHConnection {

    protected String username;

    protected String password;


    public SimpleSSHConnection(String ip, int port, String username, String password) {
        super(ip, port);
        this.username = username;
        this.password = password;
    }

    @Override
    protected boolean authenticate (SSHClient client) {
        try {
            client.authPassword(this.username, this.password);
        } catch (IOException exception) {
            return false;
        }
        return client.isAuthenticated();
    }
}
