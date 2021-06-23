package io.github.willqi.filewatchupload.connection;

import io.github.willqi.filewatchupload.config.data.RemoteConnectionConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.File;
import java.io.IOException;

public abstract class SSHConnection implements Connection {

    private final RemoteConnectionConfig config;

    public SSHConnection (RemoteConnectionConfig config) {
        this.config = config;
    }

    /**
     * The login details.
     */
    protected abstract boolean authenticate (SSHClient client);

    @Override
    public boolean upload(File file) {
        SSHClient client = new SSHClient();
        try {
            client.addHostKeyVerifier(new PromiscuousVerifier());
            client.useCompression();
            client.connect(this.config.getIp(), this.config.getPort());
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
        if (!this.authenticate(client)) {
            try {
                client.disconnect();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return false;
        }

        try {
            for (String outputDirectory : this.config.getOutputDirectories()) {
                client.newSCPFileTransfer().upload(file.getAbsolutePath(), outputDirectory);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }

        try {
            client.disconnect();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return true;
    }

}
