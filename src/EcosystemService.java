import java.io.IOException;
import java.util.*;

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
		}
		System.out.println();
	}
}
