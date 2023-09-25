package com.nort721.server.storage.licenses;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nort721.server.Main;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MongoManager {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection licenseCollection;

    public MongoManager() {
        try {

            String databaseName = Main.config.getDatabaseName();

            MongoClientURI uri = new MongoClientURI(Main.config.getMongoString());

            Main.logger.logMessage("attempting to connect to database", true);

            client = new MongoClient(uri);
            database = client.getDatabase(databaseName);

            Main.logger.logMessage("connected to database successfully", true);

            List<String> collectionsNames = database.listCollectionNames().into(new ArrayList<>());

            if (!collectionsNames.contains("license"))
                database.createCollection("license");
            licenseCollection = database.getCollection("license");

        } catch (Exception e) {
            Main.logger.logMessage("Disabling due to issues with mongo database.", true);
            e.printStackTrace();
        }
    }
}
