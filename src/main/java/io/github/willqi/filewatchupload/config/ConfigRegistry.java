package io.github.willqi.filewatchupload.config;

import io.github.willqi.filewatchupload.config.data.Config;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigRegistry {

    /**
     * Retrieve a saved configuration from the local filesystem
     * @param configId
     * @return saved configuration
     * @throws IOException
     */
    public static Config get(String configId) throws IOException {
        return null;
    }

    /**
     * Register configuration to local filesystem
     * @param path
     * @throws IOException
     */
    public static void register(Path path) throws IOException {

    }

}
