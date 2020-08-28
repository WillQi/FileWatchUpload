package io.github.willqi.filewatchupload.connection;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.File;
import java.io.IOException;

public abstract class SSHConnection implements Connection {

    protected final String ip;

    protected final int port;

    public SSHConnection (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * The login details.
     */
    protected abstract boolean authenticate (SSHClient client);

    @Override
    public boolean upload(File file, String targetLocation) {
        final SSHClient client = new SSHClient();
        try {
            client.addHostKeyVerifier(new PromiscuousVerifier());
            client.useCompression();
            client.connect(this.ip, this.port);
        } catch (IOException exception) {
            System.out.println("Failed to connect to SSH server.");
            return false;
        }
        if (!this.authenticate(client)) {
            System.out.println("Failed to authenticate. Invalid credentials.");
            try {
                client.disconnect();
            } catch (IOException exception) {
                System.out.println("Failed to disconnect SSH client");
            }
            return false;
        }

        try {
            client.newSCPFileTransfer().upload(file.getAbsolutePath(), targetLocation);
        } catch (IOException exception) {
            System.out.println("Failed to upload file");
        }

        try {
            client.disconnect();
        } catch (IOException exception) {
            System.out.println("Failed to disconnect SSH client");
        }

        return true;
    }

}
