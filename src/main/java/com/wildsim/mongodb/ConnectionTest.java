package com.wildsim.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class ConnectionTest {

    public static void main(String[] args) {
        String username = "root";
        String password = "root";
        String database = "wildsim";

        System.out.println("Attempting to connect to MongoDB...");

        try {
            // Create credentials
            MongoCredential credential = MongoCredential.createCredential(
                    username,
                    "admin",
                    password.toCharArray());

            // Configure client settings
            MongoClientSettings settings = MongoClientSettings.builder()
                    .credential(credential)
                    .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(
                            new ServerAddress("localhost", 27017))))
                    .build();

            // Create and connect the client
            MongoClient mongoClient = MongoClients.create(settings);

            // Get database
            MongoDatabase db = mongoClient.getDatabase(database);

            // List collections to verify connection
            MongoIterable<String> collections = db.listCollectionNames();

            // Current date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            System.out.println("Successfully connected to MongoDB at " + formattedDateTime);
            System.out.println("Connected as user: " + username);
            System.out.println("Database: " + database);

            System.out.println("\nExisting collections:");
            boolean hasCollections = false;
            for (String collection : collections) {
                System.out.println("- " + collection);
                hasCollections = true;
            }

            if (!hasCollections) {
                System.out.println("No collections found. Database is empty.");
            }

            // Close the connection
            mongoClient.close();
            System.out.println("\nConnection closed successfully.");

        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}