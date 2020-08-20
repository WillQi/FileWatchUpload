package io.github.willqi.connection;

public class SimpleSSHConnection extends SSHConnection {
    public SimpleSSHConnection(String ip, String port, String username, String password) {
        super(ip, port);
    }
}
