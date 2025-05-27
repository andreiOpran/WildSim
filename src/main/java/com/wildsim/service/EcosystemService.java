package com.wildsim.service;

import com.wildsim.environment.Ecosystem;
import com.wildsim.model.environment.Environment;
import com.wildsim.model.environment.WaterSource;
import com.wildsim.model.organisms.Organism;
import com.wildsim.model.organisms.animal.Carnivore;
import com.wildsim.model.organisms.animal.Herbivore;
import com.wildsim.model.organisms.plant.Tree;
import com.wildsim.ui.EcosystemDisplay;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import org.bson.Document;

import java.util.*;

public class EcosystemService {
	private static EcosystemService instance;

	private Ecosystem ecosystem;
	private EcosystemDisplay ecosystemDisplay;
	private int totalSteps;
	private int currentStep;
	private TextArea logArea;
	private Label evolutionLabel;
	private MongoDBService dbService;


	private EcosystemService(int width, int height) {
		this.ecosystem = new Ecosystem(width, height);
		this.ecosystemDisplay = new EcosystemDisplay(ecosystem);
		this.dbService = new MongoDBService();
	}

	private EcosystemService() {
        this.dbService = new MongoDBService();
    }

	public static void initializeInstance(int width, int height) {
		instance = new EcosystemService(width, height);
	}

	public static EcosystemService getInstance() {
		if (instance == null) {
			instance = new EcosystemService();
		}
		return instance;
	}

	public void initializeEcosystem(int treesCount, int herbivoresCount, int carnivoresCount, int waterSourcesCount) {
		ecosystem.generateRandomTrees(treesCount);
		ecosystem.generateRandomHerbivores(herbivoresCount);
		ecosystem.generateRandomCarnivores(carnivoresCount);
		ecosystem.generateRandomWaterSources(waterSourcesCount);
	}

	public void runSimulation(int evolutionSteps) {
		this.totalSteps = evolutionSteps;
		this.currentStep = 0;
		nextStep();
	}

    public void nextStep() {
        if (currentStep < totalSteps) {
			// load the organisms from the database
			loadOrganismsFromDatabase();
			loadEnvironmentsFromDatabase();

			// update evolution label
			if (evolutionLabel != null) {
				Platform.runLater(() -> evolutionLabel.setText("EVOLUTION NUMBER " + (currentStep)));
			}

			// capture logs
			List<String> stepLogs = new ArrayList<>();

			// run simulation
			ecosystem.progressSimulation(stepLogs);

			// display logs
			for (String logMessage : stepLogs) {
				log("(EVO" + (currentStep) + ") " + logMessage);
			}

			saveOrganismsToDatabase();
			saveEnvironmentsToDatabase();
            ecosystemDisplay.update();
            currentStep++;
        }
		else {
			// end of simulation
			// update label
			if (evolutionLabel != null) {
				Platform.runLater(() -> evolutionLabel.setText("Simulation completed."));
			}
			log ("\nEcosystem statistics after " + totalSteps + " steps:");

			// statistics
			displayStatistics();

			// clear the database collections
			dbService.getDatabase().getCollection("trees").deleteMany(new Document());
			dbService.getDatabase().getCollection("herbivores").deleteMany(new Document());
			dbService.getDatabase().getCollection("carnivores").deleteMany(new Document());
			dbService.getDatabase().getCollection("water_sources").deleteMany(new Document());
		}
    }

	private void loadOrganismsFromDatabase() {
		ecosystem.clearOrganisms();
		ecosystem.clearEnvironmentsList();

		// load trees
		List<Tree> trees = dbService.getAllTrees();
		for (Tree tree : trees) {
			ecosystem.addOrganism(tree);
		}

		// load herbivores
		List<Herbivore> herbivores = dbService.getAllHerbivores();
		for (Herbivore herbivore : herbivores) {
			ecosystem.addOrganism(herbivore);
		}

		// load carnivores
		List<Carnivore> carnivores = dbService.getAllCarnivores();
		for (Carnivore carnivore : carnivores) {
			ecosystem.addOrganism(carnivore);
		}
	}

	private void saveOrganismsToDatabase() {
		// clear existing data
		dbService.getDatabase().getCollection("trees").deleteMany(new Document());
		dbService.getDatabase().getCollection("herbivores").deleteMany(new Document());
		dbService.getDatabase().getCollection("carnivores").deleteMany(new Document());

		// save current state
		for (Organism organism : ecosystem.getOrganisms()) {
			if (organism instanceof Tree) {
				dbService.createTree((Tree) organism);
			} else if (organism instanceof Herbivore) {
				dbService.createHerbivore((Herbivore) organism);
			} else if (organism instanceof Carnivore) {
				dbService.createCarnivore((Carnivore) organism);
			}
		}
	}

	private void loadEnvironmentsFromDatabase() {
		ecosystem.clearEnvironmentsList();

		// load water sources
		List<WaterSource> waterSources = dbService.getAllWaterSources();
		for (WaterSource waterSource : waterSources) {
			ecosystem.addEnvironment(waterSource);
		}
	}

	private void saveEnvironmentsToDatabase() {
		// clear existing data
		dbService.getDatabase().getCollection("water_sources").deleteMany(new Document());

		// save current state
		for (Environment environment : ecosystem.getEnvironments()) {
			if (environment instanceof WaterSource) {
				dbService.createWaterSource((WaterSource) environment);
			}
		}
	}

	public Map<String, Integer> getStatistics() {
		Map<String, Integer> stats = new HashMap<>();
		int treesCount = 0;
		int herbivoresCount = 0;
		int carnivoresCount = 0;

		for (Organism organism : ecosystem.getOrganisms()) {
			if (organism instanceof Tree) {
				++treesCount;
			} else if (organism instanceof Herbivore) {
				++herbivoresCount;
			} else if (organism instanceof Carnivore) {
				++carnivoresCount;
			}
		}
		stats.put("Trees", treesCount);
		stats.put("Herbivores", herbivoresCount);
		stats.put("Carnivores", carnivoresCount);
		stats.put("Total Organisms", treesCount + herbivoresCount + carnivoresCount);

		// sort ascending by value
		List<Map.Entry<String, Integer>> statsEntries = new ArrayList<>(stats.entrySet());
		Collections.sort(statsEntries, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry <String, Integer > entry1, Map.Entry < String, Integer > entry2){
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});
		Map<String, Integer> sortedStats = new LinkedHashMap<>();
		for(Map.Entry<String, Integer> statEntry : statsEntries) {
			sortedStats.put(statEntry.getKey(), statEntry.getValue());
		}
		return sortedStats;
	}

	public void displayStatistics() {
		Map<String, Integer> stats = getStatistics();
		for (Map.Entry<String, Integer> entry : stats.entrySet()) {
			log(entry.getKey() + ": " + entry.getValue());
		}
		log(" ");
		dbService.saveEcosystemStatistics(stats);
	}

	public void log(String message) {
		if (logArea != null) {
			Platform.runLater(() -> {
				logArea.appendText(message + "\n");
				logArea.setScrollTop(Double.MAX_VALUE); // autoscroll to bottom
			});
		}
	}

	public EcosystemDisplay getEcosystemDisplay() {
		return ecosystemDisplay;
	}

	public void setLogArea(TextArea logArea) {
		this.logArea = logArea;
	}

	public void setEvolutionLabel(Label evolutionLabel) {
        this.evolutionLabel = evolutionLabel;
    }
}
