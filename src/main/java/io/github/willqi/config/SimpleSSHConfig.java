package io.github.willqi.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SimpleSSHConfig implements Config {

    private static Map<String, String> DEFAULT_CONFIGURATION = new HashMap<>();

    private Properties data;

    private File configFile;

    public SimpleSSHConfig(String configPath) {
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
    public int getPort() {
        return Integer.parseInt(this.data.getProperty("port"));
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

        // Load existing configuration.
        if (configFile.exists() && configFile.isFile()) {
            try {
                InputStream configStream = new FileInputStream(configFile);
                this.data.load(configStream);
                configStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }
        } else {
            // Write new configuration file and load default configuration.;
            this.data.putAll(SimpleSSHConfig.DEFAULT_CONFIGURATION);
            this.save();
            System.out.println("Created configuration file in directory. Please fill in details before running again.");
            System.exit(0);
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
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("watch", "<target file/directory>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("out_dir", "<output file/directory>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("ip", "<ip of remote server>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("port", "<port of remote server>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("username", "<username>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("password", "<password>");

    }
}
