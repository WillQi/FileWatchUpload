package io.github.willqi.filewatchupload.config.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.willqi.filewatchupload.config.data.SimpleSSHConfig;

public class SimpleSSHConfigParser implements ConfigParser<SimpleSSHConfig> {

    @Override
    public SimpleSSHConfig parse(JsonObject object) {
        SimpleSSHConfig config = new SimpleSSHConfig();

        config.setIp(object.get("ip").getAsString());
        config.setPort(object.get("port").getAsInt());
        config.setUsername(object.get("username").getAsString());
        config.setPassword(object.get("password").getAsString());

        JsonArray outputDirectoriesJSON = object.getAsJsonArray("output");
        String[] outputDirectories = new String[outputDirectoriesJSON.size()];
        for (int i = 0; i < outputDirectories.length; i++) {
            outputDirectories[i] = outputDirectoriesJSON.get(i).getAsString();
        }
        config.setOutputDirectories(outputDirectories);

        return config;
    }

}
