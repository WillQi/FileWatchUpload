package io.github.willqi.filewatchupload.config.data;

import io.github.willqi.filewatchupload.config.ConfigType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SimpleSSHConfig implements Config {

    private static Map<String, String> DEFAULT_CONFIGURATION = new HashMap<>();

    static {
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("watch", "<target file/directory>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("out_dir", "<output file/directory>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("ip", "<ip of remote server>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("port", "<port of remote server>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("username", "<username>");
        SimpleSSHConfig.DEFAULT_CONFIGURATION.put("password", "<password>");
    }


    private final Properties data;
    private final File configFile;


    public SimpleSSHConfig(String configPath) throws FileNotFoundException {
        this.data = new Properties();
        this.configFile = new File(configPath);

        this.loadConfigurationFile();
    }


    @Override
    public ConfigType getId() {
        return ConfigType.SIMPLE_SSH;
    }

    @Override
    public String[] getOutputDirectories() {
        return new String[]{ this.data.getProperty("out_dir") };
    }

    public void setOutputPath (String[] path) {
        this.data.put("out_dir", path);
    }

    public String getIP() {
        return this.data.getProperty("ip");
    }

    public int getPort() {
        return Integer.parseInt(this.data.getProperty("port"));
    }

    public String getUsername() {
        return this.data.getProperty("username");
    }

    public String getPassword () { return this.data.getProperty("password"); }

    /**
     * Load existing configuration or create new configuration if no configuration exists.
     */
    private void loadConfigurationFile () throws FileNotFoundException {
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
            throw new FileNotFoundException("Created configuration file in directory. Please fill in details before running again.");
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

}
