package com.qovery.client;

import com.eclipsesource.json.JsonValue;

/**
 * Created by evoxmusic on 19/12/2019.
 */
public class DatabaseConfiguration {

    private String type;
    private String name;
    private String host;
    private int port;
    private String username;
    private String password;
    private String version;

    public DatabaseConfiguration() {
    }

    public DatabaseConfiguration(JsonValue json) {
        this.type = json.asObject().getString("type", null);
        this.name = json.asObject().getString("name", null);
        this.host = json.asObject().getString("fqdn", null);
        this.port = json.asObject().getInt("port", -1);
        this.username = json.asObject().getString("username", null);
        this.password = json.asObject().getString("password", null);
        this.version = json.asObject().getString("version", null);
    }

    public DatabaseConfiguration(String type, String name, String host, int port, String username, String password, String version) {
        this.type = type;
        this.name = name;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
