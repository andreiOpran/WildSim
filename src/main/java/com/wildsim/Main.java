package com.wildsim;

import com.wildsim.service.EcosystemService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		// log area
		TextArea logArea = new TextArea();
		logArea.setEditable(false);
		logArea.setPrefWidth(350);
		logArea.setWrapText(true);

		ScrollPane logScrollPane = new ScrollPane(logArea);
		logScrollPane.setFitToWidth(true);
		logScrollPane.setFitToHeight(true);

		VBox rightPanel = new VBox(logScrollPane);
		VBox.setVgrow(logScrollPane, Priority.ALWAYS);
		rightPanel.setPrefWidth(350);

		EcosystemService service = new EcosystemService(10, 10);
		service.initializeEcosystem(10, 5, 3);

		// set up the service to use our log area
		service.setLogArea(logArea);

		BorderPane root = new BorderPane();
		root.setCenter(service.getEcosystemDisplay());
		root.setRight(rightPanel);

		Scene scene = new Scene(root, 1260, 960);
		final int[] stepCount = {0};
		final int maxSteps = 10;
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.SPACE && stepCount[0] < maxSteps) {
				service.nextStep();
				stepCount[0]++;
			}
		});
		primaryStage.setTitle("WildSim Ecosystem Simulation");
		primaryStage.setScene(scene);
		primaryStage.show();

		service.displayStatistics();
		service.runSimulation(10);
		root.requestFocus();
	}

	public static void main(String[] args) {
		launch(args);
	}
}