package io.github.willqi.connection;

import java.io.File;
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
    protected boolean authenticate() {
        try {
            this.client.authPassword(this.username, this.password);
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
        return this.client.isAuthenticated();
    }
}
