package io.github.willqi.filewatchupload.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.willqi.filewatchupload.config.data.Config;
import io.github.willqi.filewatchupload.config.exceptions.InvalidConfigurationException;
import io.github.willqi.filewatchupload.config.parsers.ConfigParser;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigRegistry {

    private static final Gson GSON = new Gson();


    /**
     * Retrieve a saved configuration from the local filesystem
     * @param configId id in the configuration json
     * @return saved configuration
     * @throws IOException
     */
    public static Config get(String configId) throws IOException {
        File configFile = getConfigPath(configId).toFile();
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
     * Register configuration to local filesystem
     * @param path
     * @throws IOException
     */
    public static void register(Path path) throws IOException {
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

        File configFile = getConfigPath(data.get("id").getAsString()).toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(data.toString());
        }
    }

    private static Path getDataDirectory() {
        // TODO: Look into other OS
        return Paths.get(System.getenv("APPDATA"), "FWU");
    }

    private static Path getConfigPath(String configId) {
        return getDataDirectory().resolve(configId + ".json");
    }

}
