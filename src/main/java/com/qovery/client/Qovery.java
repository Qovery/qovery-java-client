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

    public Qovery(File configurationFile, boolean overrideWithEnvironmentVariable) {
        this.configuration = getConfiguration(configurationFile);
        if (this.configuration == null || overrideWithEnvironmentVariable) {
            this.configuration = getConfiguration(ENV_JSON_B64);
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

    private JsonValue getConfiguration(String environmentVariable) {
        if (environmentVariable == null || environmentVariable.isEmpty()) {
            return null;
        }

        String b64JSON = System.getenv(environmentVariable);
        if (b64JSON == null || b64JSON.isEmpty()) {
            return null;
        }

        String jsonString = new String(Base64.getDecoder().decode(b64JSON));
        return Json.parse(jsonString);
    }

    public JsonValue getConfiguration() {
        return configuration;
    }

    public Stream<DatabaseConfiguration> listDatabaseConfiguration() {
        if (configuration == null) {
            return null;
        }

        JsonValue databasesJsonValue = configuration.asObject().get("databases");
        if (databasesJsonValue == null) {
            return null;
        }

        Iterator<JsonValue> databasesIterator = databasesJsonValue.asArray().iterator();
        Iterable<JsonValue> databasesIterable = () -> databasesIterator;

        return StreamSupport.stream(databasesIterable.spliterator(), false)
                .map(DatabaseConfiguration::new);
    }

    public DatabaseConfiguration getDatabaseConfiguration(String name) {
        Stream<DatabaseConfiguration> databasesConfiguration = listDatabaseConfiguration();
        if (databasesConfiguration == null) {
            return null;
        }

        return databasesConfiguration
                .filter(v -> v.getName().equals(name))
                .findFirst()
                .orElse(null);

    }

    public boolean isProduction() {
        String isProductionStr = System.getProperty(ENV_IS_PRODUCTION);
        if (isProductionStr == null || isProductionStr.isEmpty()) {
            return false;
        }

        return isProductionStr.toLowerCase().equals("true");
    }

    public String getBranchName() {
        return System.getProperty(ENV_BRANCH_NAME);
    }

}
