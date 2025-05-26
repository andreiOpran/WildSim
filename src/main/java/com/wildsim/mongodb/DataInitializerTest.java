package com.wildsim.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class DataInitializerTest {
	public static void main(String[] args) {
		String username = "root";
		String password = "root";
		String database = "wildsim";

		System.out.println("Connecting to MongoDB to initialize sample data...");

		try {
			// create credentials
			MongoCredential credential = MongoCredential.createCredential(
					username,
					"admin",
					password.toCharArray()
			);

			// configure client settings
			MongoClientSettings settings = MongoClientSettings.builder()
					.credential(credential)
					.applyToClusterSettings(builder ->
							builder.hosts(Collections.singletonList(
									new ServerAddress("localhost", 27017)
							))).build();

			// create client
			MongoClient mongoClient = MongoClients.create(settings);

			// get database
			MongoDatabase db = mongoClient.getDatabase(database);

			// initialize collections and sample data
			initializeHerbivores(db);
			initializeCarnivores(db);
			initializeTrees(db);
			initializeWaterSources(db);

			// close connection
			mongoClient.close();
			System.out.println("Connection closed.");
		} catch (Exception e) {
			System.err.println("Failed to initialize MongoDB data: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void initializeHerbivores(MongoDatabase db) {
		// get or create herbivores collection
		MongoCollection<Document> collection = db.getCollection("herbivores");

		List<Document> herbivores = Arrays.asList(
				new Document("type", "herbivore")
					.append("position", new Document("x", 10).append("y", 15))
					.append("energy", 100)
					.append("alive", true)
					.append("symbol", "H")
					.append("movementSpeed", 1)
					.append("visionRange", 3),

				new Document("type", "Herbivore")
					.append("position", new Document("x", 5).append("y", 8))
					.append("energy", 85)
					.append("alive", true)
					.append("symbol", "H")
					.append("movementSpeed", 1)
					.append("visionRange", 3),

				new Document("type", "Herbivore")
					.append("position", new Document("x", 20).append("y", 12))
					.append("energy", 120)
					.append("alive", true)
					.append("symbol", "H")
					.append("movementSpeed", 1)
					.append("visionRange", 3)
		);

		// insert the documents
		InsertManyResult result = collection.insertMany(herbivores);
		System.out.println("Added " + result.getInsertedIds().size() + " herbivores to the database");
	}

	private static void initializeCarnivores(MongoDatabase db) {
        MongoCollection<Document> collection = db.getCollection("carnivores");

        List<Document> carnivores = Arrays.asList(
            new Document("type", "Carnivore")
                .append("position", new Document("x", 15).append("y", 20))
                .append("energy", 150)
                .append("alive", true)
                .append("symbol", "C")
                .append("movementSpeed", 2)
                .append("visionRange", 5),

            new Document("type", "Carnivore")
                .append("position", new Document("x", 8).append("y", 25))
                .append("energy", 130)
                .append("alive", true)
                .append("symbol", "C")
                .append("movementSpeed", 2)
                .append("visionRange", 5),

            new Document("type", "Carnivore")
                .append("position", new Document("x", 25).append("y", 18))
                .append("energy", 180)
                .append("alive", true)
                .append("symbol", "C")
                .append("movementSpeed", 2)
                .append("visionRange", 5)
        );

        InsertManyResult result = collection.insertMany(carnivores);
        System.out.println("Added " + result.getInsertedIds().size() + " carnivores to the database");
    }

	private static void initializeTrees(MongoDatabase db) {
        MongoCollection<Document> collection = db.getCollection("trees");

        List<Document> trees = Arrays.asList(
            new Document("type", "Tree")
                .append("position", new Document("x", 5).append("y", 5))
                .append("energy", 30)
                .append("alive", true)
                .append("symbol", "T")
                .append("growthRate", 2)
                .append("energyThreshold", 60),

            new Document("type", "Tree")
                .append("position", new Document("x", 12).append("y", 18))
                .append("energy", 30)
                .append("alive", true)
                .append("symbol", "T")
                .append("growthRate", 2)
                .append("energyThreshold", 60),

            new Document("type", "Tree")
                .append("position", new Document("x", 22).append("y", 8))
                .append("energy", 30)
                .append("alive", true)
                .append("symbol", "T")
                .append("growthRate", 2)
                .append("energyThreshold", 60)
        );

        InsertManyResult result = collection.insertMany(trees);
        System.out.println("Added " + result.getInsertedIds().size() + " trees to the database");
    }

	private static void initializeWaterSources(MongoDatabase db) {
        MongoCollection<Document> collection = db.getCollection("water_source");

        List<Document> trees = Arrays.asList(
            new Document("type", "water_source")
                .append("position", new Document("x", 3).append("y", 7))
                .append("waterLevel", 35),

            new Document("type", "water_source")
                .append("position", new Document("x", 1).append("y", 4))
                .append("waterLevel", 40),

            new Document("type", "water_source")
                .append("position", new Document("x", 2).append("y", 8))
                .append("waterLevel", 15)
        );

        InsertManyResult result = collection.insertMany(trees);
        System.out.println("Added " + result.getInsertedIds().size() + " water sources to the database");
    }

}
