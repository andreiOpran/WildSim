package com.wildsim.service;

import com.wildsim.environment.Ecosystem;
import com.wildsim.model.organisms.Organism;
import com.wildsim.model.organisms.animal.Carnivore;
import com.wildsim.model.organisms.animal.Herbivore;
import com.wildsim.model.organisms.plant.Tree;
import com.wildsim.ui.EcosystemDisplay;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

import java.util.*;

public class EcosystemService {
	private Ecosystem ecosystem;
	private EcosystemDisplay ecosystemDisplay;
	private int totalSteps;
	private int currentStep;
	private TextArea logArea;
	private Label evolutionLabel;


	public EcosystemService(int width, int height) {
		this.ecosystem = new Ecosystem(width, height);
		this.ecosystemDisplay = new EcosystemDisplay(ecosystem);
	}

	public void initializeEcosystem(int treesCount, int herbivoresCount, int carnivoresCount) {
		ecosystem.generateRandomTrees(treesCount);
		ecosystem.generateRandomHerbivores(herbivoresCount);
		ecosystem.generateRandomCarnivores(carnivoresCount);
	}

	public void runSimulation(int evolutionSteps) {
		this.totalSteps = evolutionSteps;
		this.currentStep = 0;
		nextStep();
	}

    public void nextStep() {
        if (currentStep < totalSteps) {
			// update evolution label
			if (evolutionLabel != null) {
				Platform.runLater(() -> evolutionLabel.setText("EVOLUTION NUMBER " + (currentStep)));
			}

			// capture logs
			List<String> stepLogs = new ArrayList<>();
			ecosystem.progressSimulation(stepLogs);

			// display logs
			for (String logMessage : stepLogs) {
				log("(EVO" + (currentStep) + ") " + logMessage);
			}

            ecosystem.progressSimulation(stepLogs);
            ecosystemDisplay.update();
            currentStep++;
        }
		else {
			if (evolutionLabel != null) {
				Platform.runLater(() -> evolutionLabel.setText("Simulation completed."));
			}
			log ("\nEcosystem statistics after " + totalSteps + " steps:");
			displayStatistics();
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
		System.out.println("Ecosystem Statistics:");
		for (Map.Entry<String, Integer> entry : stats.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
			log(entry.getKey() + ": " + entry.getValue());
		}
		log(" ");
		System.out.println();
	}

	public void log(String message) {
		System.out.println(message);
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
