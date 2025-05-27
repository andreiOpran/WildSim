package com.wildsim;

import com.wildsim.ui.SimulationUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		SimulationUI simulationUI = new SimulationUI();
		simulationUI.initialize(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}