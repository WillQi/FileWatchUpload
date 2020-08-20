package io.github.willqi.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DefaultConfig implements Config {

    private static Map<String, String> DEFAULT_CONFIGURATION = new HashMap<>();

    private Properties data;

    private File configFile;

    public DefaultConfig(String configPath) {
        this.data = new Properties();
        this.configFile = new File(configPath);

        this.loadConfigurationFile();
    }

    @Override
    public String getWatchPath() {
        return this.data.getProperty("watch");
    }

    @Override
    public String getOutputPath() {
        return this.data.getProperty("out_dir");
    }

    @Override
    public String getIP() {
        return this.data.getProperty("ip");
    }

    @Override
    public String getPort() {
        return this.data.getProperty("port");
    }

    @Override
    public String getUsername() {
        return this.data.getProperty("username");
    }

    public String getPassword () { return this.data.getProperty("password"); }

    public void setWatchPath (String path) {
        this.data.put("watch", path);
    }

    public void setOutputPath (String path) {
        this.data.put("out_dir", path);
    }

    /**
     * Load existing configuration or create new configuration if no configuration exists.
     */
    private void loadConfigurationFile () {
        this.data.clear();
        Properties config = new Properties();

        // Load existing configuration.
        if (configFile.exists() && configFile.isFile()) {
            try {
                InputStream configStream = new FileInputStream(configFile);
                config.load(configStream);
                configStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }
        } else {
            // Write new configuration file and load default configuration.
            this.data.putAll(DefaultConfig.DEFAULT_CONFIGURATION);
            this.save();
        }
    }

    private void save () {
        try {
            this.configFile.createNewFile();
            OutputStream configStream = new FileOutputStream(this.configFile);
            this.data.store(configStream, null);
            configStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    static {
        DefaultConfig.DEFAULT_CONFIGURATION.put("watch", "<target file/directory>");
        DefaultConfig.DEFAULT_CONFIGURATION.put("out_dir", "<output file/directory>");
        DefaultConfig.DEFAULT_CONFIGURATION.put("ip", "<ip of remote server>");
        DefaultConfig.DEFAULT_CONFIGURATION.put("port", "<port of remote server>");
        DefaultConfig.DEFAULT_CONFIGURATION.put("username", "<username>");
        DefaultConfig.DEFAULT_CONFIGURATION.put("password", "<password>");

    }
}
