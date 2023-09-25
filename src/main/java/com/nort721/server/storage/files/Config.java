package com.nort721.server.storage.files;

import com.nort721.server.Constants;
import com.nort721.server.Main;
import com.nort721.server.storage.logs.YamlStorage;

import java.io.File;

public class Config {

    private final YamlStorage configYaml;

    public Config() {
        String path = Main.getSelfPath();
        assert path != null;
        path = FilenameUtils.getPath(path);

        File configFile = new File(path + "config.yml");

        if (!configFile.exists()) {
            Main.logger.logMessage("No configuration file found, generating a new one . . .", true);
            configYaml = new YamlStorage("config", path);
            configYaml.set("dbname", "none");
            configYaml.set("mongoString", "none");
            configYaml.set("communication encryption key", "none");
            configYaml.set("Max Hardware-id per license", 0);
            configYaml.save();
        } else {
            configYaml = new YamlStorage("config", path);
        }
    }

    public String getDatabaseName() {
        String str = configYaml.getString("dbname");
        return str.length() < 2 ? "none" : str;
    }

    public void setDatabaseName(String val) {
        configYaml.set("dbname", val);
        configYaml.save();
    }

    public String getMongoString() {
        String str = configYaml.getString("mongoString");
        return str.length() < 2 ? "none" : str;
    }

    public void setMongoString(String str) {
        configYaml.set("mongoString", str);
        configYaml.save();
    }

    public String getCommunicationKey() {
        String str = configYaml.getString("communication encryption key");
        return str.length() < 2 ? "none" : str;
    }

    public void setCommunicationKey(String str) {
        configYaml.set("communication encryption key", str);
        configYaml.save();
        Constants.init();
    }

    public int getPort() {
        return configYaml.getInt("port");
    }

    public void setPort(int val) {
        configYaml.set("port", val);
        configYaml.save();
        Constants.init();
    }

    public int getMaxHWID() {
        return configYaml.getInt("Max Hardware-id per license");
    }

    public void setMaxHWID(int val) {
        configYaml.set("Max Hardware-id per license", val);
        configYaml.save();
        Constants.init();
    }
}
