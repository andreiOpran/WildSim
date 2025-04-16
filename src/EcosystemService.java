import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EcosystemService {
	private Ecosystem ecosystem;

	public EcosystemService(int width, int height) {
		this.ecosystem = new Ecosystem(width, height);
	}

	public void initializeEcosystem(int treesCount, int herbivoresCount, int carnivoresCount) {
		ecosystem.generateRandomTrees(treesCount);
		ecosystem.generateRandomHerbivores(herbivoresCount);
		ecosystem.generateRandomCarnivores(carnivoresCount);
	}

	public void runSimulation(int evolutionSteps) {
		for(int i = 0; i < evolutionSteps; i++) {
			System.out.println("EVOLUTION NUMBER " + (i + 1));
			ecosystem.progressSimulation();
			ecosystem.displayMatrix();
			System.out.println("Press Enter for next evolution...");
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, Integer> getStatistics() {
		Map<String, Integer> stats = new LinkedHashMap<>();
		int treesCount = 0;
		int herbivoresCount = 0;
		int carnivoresCount = 0;

		for(Organism organism : ecosystem.getOrganisms()) {
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

		return stats;
	}

	public void displayStatistics() {
		Map<String, Integer> stats = getStatistics();
		System.out.println("Ecosystem Statistics:");
		for (Map.Entry<String, Integer> entry : stats.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		System.out.println();
	}
}
