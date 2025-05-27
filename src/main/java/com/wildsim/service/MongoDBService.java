package com.wildsim.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.wildsim.config.MongoDBConfig;
import com.wildsim.environment.Position;
import com.wildsim.model.organisms.animal.Carnivore;
import com.wildsim.model.organisms.animal.Herbivore;
import com.wildsim.model.organisms.plant.Tree;
import com.wildsim.model.environment.WaterSource;
import java.util.Date;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoDBService {
	private final MongoDatabase database;

	public MongoDBService() {
		this.database = MongoDBConfig.getDatabase();
	}


	// TREES
	public void createTree(Tree tree) {
		MongoCollection<Document> collection = database.getCollection("trees");
		Document doc = convertTreeToDocument(tree);
		collection.insertOne(doc);
	}

	public List<Tree> getAllTrees() {
		MongoCollection<Document> collection = database.getCollection("trees");
		List<Tree> trees = new ArrayList<>();

		for (Document doc : collection.find()) {
			trees.add(convertDocumentToTree(doc));
		}
		return trees;
	}

	public Tree getTreeByPosition(Position position) {
		MongoCollection<Document> collection = database.getCollection("trees");
		Bson filter = Filters.and(
				Filters.eq("position.x", position.getX()),
				Filters.eq("positions.y", position.getY())
		);
		Document doc = collection.find(filter).first();
		return doc != null ? convertDocumentToTree(doc) : null;
	}

	public void updateTree(Tree tree, Position oldPosition) {
		MongoCollection<Document> collection = database.getCollection("trees");
		Bson filter = Filters.and(
			Filters.eq("position.x", oldPosition.getX()),
			Filters.eq("position.y", oldPosition.getY())
		);
		Document doc = convertTreeToDocument(tree);
		collection.replaceOne(filter, doc, new ReplaceOptions().upsert(true));
	}

	public boolean deleteTree(Position position) {
		MongoCollection<Document> collection = database.getCollection("trees");
		Bson filter = Filters.and(
			Filters.eq("position.x", position.getX()),
			Filters.eq("position.y", position.getY())
		);
		DeleteResult result = collection.deleteOne(filter);
		return result.getDeletedCount() > 0;
	}


	// CARNIVORE
    public void createCarnivore(Carnivore carnivore) {
        MongoCollection<Document> collection = database.getCollection("carnivores");
        Document doc = convertCarnivoreToDocument(carnivore);
        collection.insertOne(doc);
    }

    public List<Carnivore> getAllCarnivores() {
        MongoCollection<Document> collection = database.getCollection("carnivores");
        List<Carnivore> carnivores = new ArrayList<>();

        for (Document doc : collection.find()) {
            carnivores.add(convertDocumentToCarnivore(doc));
        }
        return carnivores;
    }

    public Carnivore getCarnivoreByPosition(Position position) {
        MongoCollection<Document> collection = database.getCollection("carnivores");
        Bson filter = Filters.and(
            Filters.eq("position.x", position.getX()),
            Filters.eq("position.y", position.getY())
        );
        Document doc = collection.find(filter).first();
        return doc != null ? convertDocumentToCarnivore(doc) : null;
    }

	public void updateCarnivore(Carnivore carnivore, Position oldPosition) {
		MongoCollection<Document> collection = database.getCollection("carnivores");
		Bson filter = Filters.and(
			Filters.eq("position.x", oldPosition.getX()),
			Filters.eq("position.y", oldPosition.getY())
		);
		Document doc = convertCarnivoreToDocument(carnivore);
		collection.replaceOne(filter, doc, new ReplaceOptions().upsert(true));
	}

    public boolean deleteCarnivore(Position position) {
        MongoCollection<Document> collection = database.getCollection("carnivores");
        Bson filter = Filters.and(
            Filters.eq("position.x", position.getX()),
            Filters.eq("position.y", position.getY())
        );
        DeleteResult result = collection.deleteOne(filter);
        return result.getDeletedCount() > 0;
    }


	// HERBIVORE
    public void createHerbivore(Herbivore herbivore) {
        MongoCollection<Document> collection = database.getCollection("herbivores");
        Document doc = convertHerbivoreToDocument(herbivore);
        collection.insertOne(doc);
    }

    public List<Herbivore> getAllHerbivores() {
        MongoCollection<Document> collection = database.getCollection("herbivores");
        List<Herbivore> herbivores = new ArrayList<>();

        for (Document doc : collection.find()) {
            herbivores.add(convertDocumentToHerbivore(doc));
        }
        return herbivores;
    }

    public Herbivore getHerbivoreByPosition(Position position) {
        MongoCollection<Document> collection = database.getCollection("herbivores");
        Bson filter = Filters.and(
            Filters.eq("position.x", position.getX()),
            Filters.eq("position.y", position.getY())
        );
        Document doc = collection.find(filter).first();
        return doc != null ? convertDocumentToHerbivore(doc) : null;
    }

	public void updateHerbivore(Herbivore herbivore, Position oldPosition) {
		MongoCollection<Document> collection = database.getCollection("herbivores");
		Bson filter = Filters.and(
			Filters.eq("position.x", oldPosition.getX()),
			Filters.eq("position.y", oldPosition.getY())
		);
		Document doc = convertHerbivoreToDocument(herbivore);
		collection.replaceOne(filter, doc, new ReplaceOptions().upsert(true));
	}

    public boolean deleteHerbivore(Position position) {
        MongoCollection<Document> collection = database.getCollection("herbivores");
        Bson filter = Filters.and(
            Filters.eq("position.x", position.getX()),
            Filters.eq("position.y", position.getY())
        );
        DeleteResult result = collection.deleteOne(filter);
        return result.getDeletedCount() > 0;
    }


	// WATER SOURCE
    public void createWaterSource(WaterSource waterSource) {
        MongoCollection<Document> collection = database.getCollection("waterSources");
        Document doc = convertWaterSourceToDocument(waterSource);
        collection.insertOne(doc);
    }

    public List<WaterSource> getAllWaterSources() {
        MongoCollection<Document> collection = database.getCollection("waterSources");
        List<WaterSource> waterSources = new ArrayList<>();

        for (Document doc : collection.find()) {
            waterSources.add(convertDocumentToWaterSource(doc));
        }
        return waterSources;
    }

    public WaterSource getWaterSourceByPosition(int x, int y) {
        MongoCollection<Document> collection = database.getCollection("waterSources");
        Bson filter = Filters.and(
            Filters.eq("x", x),
            Filters.eq("y", y)
        );
        Document doc = collection.find(filter).first();
        return doc != null ? convertDocumentToWaterSource(doc) : null;
    }

	public void updateWaterSource(WaterSource waterSource, int oldX, int oldY) {
		MongoCollection<Document> collection = database.getCollection("waterSources");
		Bson filter = Filters.and(
			Filters.eq("x", oldX),
			Filters.eq("y", oldY)
		);
		Document doc = convertWaterSourceToDocument(waterSource);
		collection.replaceOne(filter, doc, new ReplaceOptions().upsert(true));
	}

    public boolean deleteWaterSource(int x, int y) {
        MongoCollection<Document> collection = database.getCollection("waterSources");
        Bson filter = Filters.and(
            Filters.eq("x", x),
            Filters.eq("y", y)
        );
        DeleteResult result = collection.deleteOne(filter);
        return result.getDeletedCount() > 0;
    }


	// CONVERSIONS
	private Document convertTreeToDocument(Tree tree) {
		Document doc = new Document();
		doc.append("type", "tree");
		doc.append("position", new Document()
				.append("x", tree.getPosition().getX())
				.append("y", tree.getPosition().getY()));
		doc.append("energy", tree.getEnergy());
		doc.append("symbol", String.valueOf(tree.getSymbol()));
		doc.append("alive", tree.isAlive());
		doc.append("growthRate", tree.getGrowthRate());
		doc.append("energyThreshold", tree.getEnergyThreshold());
		return doc;
	}

	private Tree convertDocumentToTree(Document doc) {
		Document posDoc = (Document) doc.get("position");
		Position position = new Position(
			posDoc != null ? posDoc.getInteger("x", 0) : 0,
			posDoc != null ? posDoc.getInteger("y", 0) : 0
		);

		Integer energy = doc.getInteger("energy");
		Boolean alive = doc.getBoolean("alive");
		String symbolStr = doc.getString("symbol");
		Integer growthRate = doc.getInteger("growthRate");
		Integer energyThreshold = doc.getInteger("energyThreshold");

		return new Tree(
			position,
			energy != null ? energy : 30,
			alive != null ? alive : true,
			symbolStr != null && !symbolStr.isEmpty() ? symbolStr.charAt(0) : 'T',
			growthRate != null ? growthRate : 2,
			energyThreshold != null ? energyThreshold : 65
		);
	}

	private Document convertCarnivoreToDocument(Carnivore carnivore) {
		Document doc = new Document();
		doc.append("type", "carnivore");
		doc.append("position", new Document()
				.append("x", carnivore.getPosition().getX())
				.append("y", carnivore.getPosition().getY()));
		doc.append("energy", carnivore.getEnergy());
		doc.append("symbol", String.valueOf(carnivore.getSymbol()));
		doc.append("alive", carnivore.isAlive());
		doc.append("visionRange", carnivore.getVisionRange());
		doc.append("movementSpeed", carnivore.getMovementSpeed());
		return doc;
	}

	private Carnivore convertDocumentToCarnivore(Document doc) {
		Document posDoc = (Document) doc.get("position");
		Position position = new Position(
			posDoc != null ? posDoc.getInteger("x", 0) : 0,
			posDoc != null ? posDoc.getInteger("y", 0) : 0
		);

		Integer energy = doc.getInteger("energy");
		Boolean alive = doc.getBoolean("alive");
		String symbolStr = doc.getString("symbol");
		Integer movementSpeed = doc.getInteger("movementSpeed");
		Integer visionRange = doc.getInteger("visionRange");

		return new Carnivore(
			position,
			energy != null ? energy : 20,
			alive != null ? alive : true,
			symbolStr != null && !symbolStr.isEmpty() ? symbolStr.charAt(0) : 'C',
			movementSpeed != null ? movementSpeed : 2,
			visionRange != null ? visionRange : 5
		);
	}

	private Document convertHerbivoreToDocument(Herbivore herbivore) {
		Document doc = new Document();
		doc.append("type", "herbivore");
		doc.append("position", new Document()
				.append("x", herbivore.getPosition().getX())
				.append("y", herbivore.getPosition().getY()));
		doc.append("energy", herbivore.getEnergy());
		doc.append("symbol", String.valueOf(herbivore.getSymbol()));
		doc.append("alive", herbivore.isAlive());
		doc.append("visionRange", herbivore.getVisionRange());
		doc.append("movementSpeed", herbivore.getMovementSpeed());
		return doc;
	}

	private Herbivore convertDocumentToHerbivore(Document doc) {
		Document posDoc = (Document) doc.get("position");
		Position position = new Position(
			posDoc != null ? posDoc.getInteger("x", 0) : 0,
			posDoc != null ? posDoc.getInteger("y", 0) : 0
		);

		Integer energy = doc.getInteger("energy");
		Boolean alive = doc.getBoolean("alive");
		String symbolStr = doc.getString("symbol");
		Integer movementSpeed = doc.getInteger("movementSpeed");
		Integer visionRange = doc.getInteger("visionRange");

		return new Herbivore(
			position,
			energy != null ? energy : 15,
			alive != null ? alive : true,
			symbolStr != null && !symbolStr.isEmpty() ? symbolStr.charAt(0) : 'H',
			movementSpeed != null ? movementSpeed : 1,
			visionRange != null ? visionRange : 3
		);
	}

	private Document convertWaterSourceToDocument(WaterSource waterSource) {
		Document doc = new Document();
		doc.append("type", "water_source");
		doc.append("x", waterSource.getX());
		doc.append("y", waterSource.getY());
		doc.append("waterLevel", waterSource.getWaterLevel());
		return doc;
	}

	private WaterSource convertDocumentToWaterSource(Document doc) {
		Integer x = doc.getInteger("x");
		Integer y = doc.getInteger("y");
		Double waterLevel = doc.getDouble("waterLevel");

		return new WaterSource(
			x != null ? x : 0,
			y != null ? y : 0,
			waterLevel != null ? waterLevel : 100.0
		);
	}


	// FILTERS
	private Bson createTreeFilter(Tree tree) {
		return Filters.and(
			Filters.eq("type", "tree"),
			Filters.eq("energy", tree.getEnergy()),
			Filters.eq("alive", tree.isAlive()),
			Filters.eq("symbol", String.valueOf(tree.getSymbol())),
			Filters.eq("growthRate", tree.getGrowthRate()),
			Filters.eq("energyThreshold", tree.getEnergyThreshold())
		);
	}

	private Bson createCarnivoreFilter(Carnivore carnivore) {
		return Filters.and(
			Filters.eq("type", "carnivore"),
			Filters.eq("energy", carnivore.getEnergy()),
			Filters.eq("alive", carnivore.isAlive()),
			Filters.eq("symbol", String.valueOf(carnivore.getSymbol())),
			Filters.eq("visionRange", carnivore.getVisionRange()),
			Filters.eq("movementSpeed", carnivore.getMovementSpeed())
		);
	}

	private Bson createHerbivoreFilter(Herbivore herbivore) {
		return Filters.and(
			Filters.eq("type", "herbivore"),
			Filters.eq("energy", herbivore.getEnergy()),
			Filters.eq("alive", herbivore.isAlive()),
			Filters.eq("symbol", String.valueOf(herbivore.getSymbol())),
			Filters.eq("visionRange", herbivore.getVisionRange()),
			Filters.eq("movementSpeed", herbivore.getMovementSpeed())
		);
	}

	// POSITIONING
	public boolean isPositionOccupied(Position position) {
        // Check in trees collection
        if (getTreeAtPosition(position) != null) return true;

        // Check in carnivores collection
        if (getCarnivoreAtPosition(position) != null) return true;

        // Check in herbivores collection
        if (getHerbivoreAtPosition(position) != null) return true;

        // Check in water sources collection (they use x,y directly)
        MongoCollection<Document> collection = database.getCollection("waterSources");
        Bson filter = Filters.and(
            Filters.eq("x", position.getX()),
            Filters.eq("y", position.getY())
        );
        return collection.countDocuments(filter) > 0;
    }

    public Tree getTreeAtPosition(Position position) {
        MongoCollection<Document> collection = database.getCollection("trees");
        Bson filter = Filters.and(
            Filters.eq("position.x", position.getX()),
            Filters.eq("position.y", position.getY())
        );
        Document doc = collection.find(filter).first();
        return doc != null ? convertDocumentToTree(doc) : null;
    }

    public Carnivore getCarnivoreAtPosition(Position position) {
        MongoCollection<Document> collection = database.getCollection("carnivores");
        Bson filter = Filters.and(
            Filters.eq("position.x", position.getX()),
            Filters.eq("position.y", position.getY())
        );
        Document doc = collection.find(filter).first();
        return doc != null ? convertDocumentToCarnivore(doc) : null;
    }

    public Herbivore getHerbivoreAtPosition(Position position) {
        MongoCollection<Document> collection = database.getCollection("herbivores");
        Bson filter = Filters.and(
            Filters.eq("position.x", position.getX()),
            Filters.eq("position.y", position.getY())
        );
        Document doc = collection.find(filter).first();
        return doc != null ? convertDocumentToHerbivore(doc) : null;
    }

	public void saveEcosystemStatistics(Map<String, Integer> statistics) {
		Document doc = new Document()
					   .append("timestamp", new Date())
					   .append("statistics", new Document(statistics));

		getDatabase().getCollection("ecosystem_statistics").insertOne(doc);
	}

	public MongoDatabase getDatabase() {
		return database;
	}
}
