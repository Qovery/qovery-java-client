package com.qovery.client;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Base64;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by evoxmusic on 19/12/2019.
 */
public class Qovery {
    private final String ENV_JSON_B64 = "QOVERY_JSON_B64";
    private final String ENV_IS_PRODUCTION = "QOVERY_IS_PRODUCTION";
    private final String ENV_BRANCH_NAME = "QOVERY_BRANCH_NAME";
    private JsonValue configuration;

    public Qovery() {
        this.configuration = getConfiguration(ENV_JSON_B64);
        if (this.configuration == null) {
            this.getConfiguration(new File(".qovery/local_configuration.json"));
        }
    }

    public Qovery(File configurationFile) {
        this(configurationFile, true);
    }

    public Qovery(File configurationFile, boolean isEnvironmentVariableOverride) {
        this.configuration = getConfiguration(ENV_JSON_B64);
        if (this.configuration == null || isEnvironmentVariableOverride) {
            this.configuration = getConfiguration(configurationFile);
        }
    }

    public Qovery(String environmentVariable) {
        this.configuration = getConfiguration(environmentVariable);
    }

    private JsonValue getConfiguration(File configurationFile) {
        if (configurationFile == null) {
            return null;
        }

        try {
            Reader reader = new FileReader(configurationFile);
            return Json.parse(reader);
        } catch (IOException ignored) {
        }

        return null;
    }

    private JsonValue getConfiguration(String environmentVariableContent) {
        if (environmentVariableContent == null || environmentVariableContent.isEmpty()) {
            return null;
        }

        String b64JSON = System.getenv(environmentVariableContent);
        if (b64JSON == null || b64JSON.isEmpty()) {
            return null;
        }

        String jsonString = new String(Base64.getDecoder().decode(b64JSON));
        return Json.parse(jsonString);
    }

    public JsonValue getConfiguration() {
        return configuration;
    }

    public DatabaseConfiguration getDatabaseConfiguration(String name) {
        Iterator<JsonValue> databasesIterator = configuration.asObject().get("databases").asArray().iterator();
        Iterable<JsonValue> databasesIterable = () -> databasesIterator;

        return StreamSupport.stream(databasesIterable.spliterator(), false)
                .filter(v -> v.asObject().getString("name", null).equals(name))
                .findFirst()
                .map(DatabaseConfiguration::new)
                .orElse(null);

    }

    public Stream<DatabaseConfiguration> listDatabaseConfiguration() {
        Iterator<JsonValue> databasesIterator = configuration.asObject().get("databases").asArray().iterator();
        Iterable<JsonValue> databasesIterable = () -> databasesIterator;

        return StreamSupport.stream(databasesIterable.spliterator(), false)
                .map(DatabaseConfiguration::new);
    }

    public boolean isProduction() {
        return System.getProperty(ENV_IS_PRODUCTION).toLowerCase().equals("true");
    }

    public String getBranchName() {
        return System.getProperty(ENV_BRANCH_NAME);
    }

}
