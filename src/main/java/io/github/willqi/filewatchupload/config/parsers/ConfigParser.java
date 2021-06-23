package io.github.willqi.filewatchupload.config.parsers;

import io.github.willqi.filewatchupload.config.data.Config;

import com.google.gson.JsonObject;

public interface ConfigParser<T extends Config> {

    T parse(JsonObject object);

}
