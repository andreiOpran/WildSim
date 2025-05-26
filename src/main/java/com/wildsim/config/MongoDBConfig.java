package com.wildsim.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Collections;

public class MongoDBConfig {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String DATABASE_NAME = "wildsim";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        // create credentials
        MongoCredential credential = MongoCredential.createCredential(
                USERNAME,
                "admin", // The authentication database
                PASSWORD.toCharArray());

        // configure client settings
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                    builder.hosts(Collections.singletonList(
                        new ServerAddress(HOST, PORT))))
                .build();

        // create and connect the client
        mongoClient = MongoClients.create(settings);

        // return the database
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
