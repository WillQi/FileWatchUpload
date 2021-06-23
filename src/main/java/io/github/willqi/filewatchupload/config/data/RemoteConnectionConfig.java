package io.github.willqi.filewatchupload.config.data;

public abstract class RemoteConnectionConfig extends Config {

    private String ip;
    private int port;


    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
