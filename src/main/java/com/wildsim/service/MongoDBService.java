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
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

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

	public void updateTree(Tree tree) {
		MongoCollection<Document> collection = database.getCollection("trees");
		Bson filter = Filters.and(
			Filters.eq("position.x", tree.getPosition().getX()),
			Filters.eq("position.y", tree.getPosition().getY())
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

    public void updateCarnivore(Carnivore carnivore) {
        MongoCollection<Document> collection = database.getCollection("carnivores");
        Bson filter = Filters.and(
            Filters.eq("position.x", carnivore.getPosition().getX()),
            Filters.eq("position.y", carnivore.getPosition().getY())
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

    public void updateHerbivore(Herbivore herbivore) {
        MongoCollection<Document> collection = database.getCollection("herbivores");
        Bson filter = Filters.and(
            Filters.eq("position.x", herbivore.getPosition().getX()),
            Filters.eq("position.y", herbivore.getPosition().getY())
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

    public void updateWaterSource(WaterSource waterSource) {
        MongoCollection<Document> collection = database.getCollection("waterSources");
        Bson filter = Filters.and(
            Filters.eq("x", waterSource.getX()),
            Filters.eq("y", waterSource.getY())
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
		doc.append("position", new Document()
				.append("x", tree.getPosition().getX())
				.append("y", tree.getPosition().getY()));
		doc.append("energy", tree.getEnergy());
		doc.append("symbol", String.valueOf(tree.getSymbol()));
		doc.append("alive", tree.isAlive());
		return doc;
	}

	private Tree convertDocumentToTree(Document doc) {
		Document posDoc = (Document) doc.get("position");
		Position position = new Position(posDoc.getInteger("x"), posDoc.getInteger("y"));
		Tree tree = new Tree(position);
		tree.setEnergy(doc.getInteger("energy"));
		tree.setAlive(doc.getBoolean("alive"));
		return tree;
	}

	private Document convertCarnivoreToDocument(Carnivore carnivore) {
        Document doc = new Document();
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
        Position position = new Position(posDoc.getInteger("x"), posDoc.getInteger("y"));
        Carnivore carnivore = new Carnivore(position, doc.getInteger("energy"));
        carnivore.setAlive(doc.getBoolean("alive"));
        return carnivore;
    }

    private Document convertHerbivoreToDocument(Herbivore herbivore) {
        Document doc = new Document();
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
        Position position = new Position(posDoc.getInteger("x"), posDoc.getInteger("y"));
        Herbivore herbivore = new Herbivore(position, doc.getInteger("energy"));
        herbivore.setAlive(doc.getBoolean("alive"));
        return herbivore;
    }

    private Document convertWaterSourceToDocument(WaterSource waterSource) {
        Document doc = new Document();
        doc.append("x", waterSource.getX());
        doc.append("y", waterSource.getY());
        doc.append("waterLevel", waterSource.getWaterLevel());
        return doc;
    }

    private WaterSource convertDocumentToWaterSource(Document doc) {
        return new WaterSource(
            doc.getInteger("x"),
            doc.getInteger("y"),
            doc.getDouble("waterLevel")
        );
    }

}
