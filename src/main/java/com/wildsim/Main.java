package com.wildsim;

import com.wildsim.service.EcosystemService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		EcosystemService service = new EcosystemService(10, 10);
		service.initializeEcosystem(10, 5, 3);

		BorderPane root = new BorderPane();
		root.setCenter(service.getEcosystemDisplay());

		Button nextButton = new Button("Next Evolution");
		nextButton.setOnAction(event -> service.nextStep());

		HBox controls = new HBox(10);
		controls.setPadding(new Insets(10));
		controls.getChildren().add(nextButton);

		root.setBottom(controls);

		Scene scene = new Scene(root, 960, 960);
		primaryStage.setTitle("WildSim Ecosystem Simulation");
		primaryStage.setScene(scene);
		primaryStage.show();

		service.displayStatistics();
		service.runSimulation(10);
	}

	public static void main(String[] args) {
		launch(args);
	}
}