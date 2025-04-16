public class Main {
	public static void main(String[] args) {
		EcosystemService service = new EcosystemService(10, 10);
		service.initializeEcosystem(10, 5, 3);
		service.displayStatistics();
		service.runSimulation(10);
		service.displayStatistics();
	}
}