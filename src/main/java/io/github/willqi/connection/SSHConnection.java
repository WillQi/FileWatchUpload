package io.github.willqi.connection;

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
    public void upload(File file, String targetLocation) {
        SSHClient client = new SSHClient();
        try {
            client.addHostKeyVerifier(new PromiscuousVerifier());
            client.useCompression();
            client.connect(this.ip, this.port);
        } catch (IOException exception) {
            System.out.println("Failed to upload.");
            exception.printStackTrace();
            return;
        }
        if (!this.authenticate(client)) {
            System.out.println("Failed to upload. Invalid credentials.");
            try {
                client.disconnect();
            } catch (IOException exception) {}
            return;
        }

        try {
            client.newSCPFileTransfer().upload(file.getAbsolutePath(), targetLocation);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            client.disconnect();
        } catch (IOException exception) {}
    }

}
