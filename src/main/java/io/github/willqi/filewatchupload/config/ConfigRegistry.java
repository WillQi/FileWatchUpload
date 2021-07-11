package io.github.willqi.filewatchupload.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.willqi.filewatchupload.config.data.Config;
import io.github.willqi.filewatchupload.config.exceptions.InvalidConfigurationException;
import io.github.willqi.filewatchupload.config.parsers.ConfigParser;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigRegistry {

    private static final Gson GSON = new Gson();

    private final Path dataDirectory;


    public ConfigRegistry(Path dataDirectory) {
        // TODO: Look into other OS
        this.dataDirectory = dataDirectory;
        this.dataDirectory.toFile().mkdirs();
    }

    /**
     * Retrieve a saved configuration from the local filesystem
     * @param configId id in the configuration json
     * @return saved configuration
     * @throws IOException
     */
    public Config get(String configId) throws IOException {
        File configFile = this.getConfigPath(configId).toFile();
        if (!configFile.exists()) {
            throw new FileNotFoundException("Configuration file does not exist.");
        }

        JsonObject data;
        try (FileReader reader = new FileReader(configFile)) {
            data = GSON.fromJson(reader, JsonObject.class);
        }

        if (!data.has("type")) {
            throw new InvalidConfigurationException("Missing type in configuration file requested.");
        }

        // Determine the config parser to use
        ConfigType configType = ConfigType.getTypeById(data.get("type").getAsString());
        if (configType == null) {
            throw new InvalidConfigurationException("Could not find a configuration handler for the type " + data.get("type").getAsString());
        }

        ConfigParser<? extends Config> parser = configType.getParser();
        return parser.parse(data);
    }

    /**
     * Retrieve all configuration ids in the registry
     * @return a list of ids
     */
    public List<String> getIds() {
        File[] files = this.getDataDirectory().toFile().listFiles();
        List<String> ids = new ArrayList<>(files.length);
        for (File file : files) {
            ids.add(file.getName().substring(0, file.getName().lastIndexOf(".json")));
        }
        return ids;
    }

    /**
     * Register configuration to local filesystem
     * @param path
     * @throws IOException
     */
    public void register(Path path) throws IOException {
        File registerConfigFile = path.toFile();
        if (!registerConfigFile.exists()) {
            throw new FileNotFoundException("Could not find configuration file to register.");
        }

        JsonObject data;
        try (FileReader reader = new FileReader(registerConfigFile)) {
            data = GSON.fromJson(reader, JsonObject.class);
        }

        if (!data.has("id")) {
            throw new InvalidConfigurationException("Missing id property in configuration.");
        }

        File configFile = this.getConfigPath(data.get("id").getAsString()).toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(data.toString());
        }
    }

    public Path getDataDirectory() {
        return this.dataDirectory;
    }

    private Path getConfigPath(String configId) {
        return this.getDataDirectory().resolve(configId + ".json");
    }

}
