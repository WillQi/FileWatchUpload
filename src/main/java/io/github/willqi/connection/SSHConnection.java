package io.github.willqi.connection;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.File;
import java.io.IOException;

public abstract class SSHConnection implements Connection {

    protected final String ip;

    protected final int port;

    protected final SSHClient client = new SSHClient();

    public SSHConnection (String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            this.client.addHostKeyVerifier(new PromiscuousVerifier());
            this.client.useCompression();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * The login details.
     */
    protected abstract boolean authenticate ();

    @Override
    public void upload(File file, String targetLocation) {
        try {
            this.client.connect(this.ip, this.port);
        } catch (IOException exception) {
            System.out.println("Failed to upload.");
            exception.printStackTrace();
            return;
        }
        if (!this.authenticate()) {
            System.out.println("Failed to upload. Invalid credentials.");
            this.disconnectClient();
            return;
        }

        try {
            this.client.newSCPFileTransfer().upload(file.getAbsolutePath(), targetLocation);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        this.disconnectClient();
    }

    private void disconnectClient () {
        try {
            this.client.disconnect();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
