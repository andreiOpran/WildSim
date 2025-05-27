package com.wildsim.ui;

import com.wildsim.service.EcosystemService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class SimulationUI {
    private EcosystemService service;
    private int stepCount = 0;
    private final int maxSteps = 100;
    private Label evolutionLabel;

    public void initialize(Stage primaryStage) {
        // Create UI components
        TextArea logArea = createLogArea();
        ScrollPane logScrollPane = createLogScrollPane(logArea);
        evolutionLabel = createEvolutionLabel();
        VBox rightPanel = createRightPanel(evolutionLabel, logScrollPane);

        // Initialize service
        service = new EcosystemService(10, 10);
        service.initializeEcosystem(10, 5, 3);
        service.setLogArea(logArea);
        service.setEvolutionLabel(evolutionLabel);

        // Set up main layout
        BorderPane root = new BorderPane();
        root.setCenter(service.getEcosystemDisplay());
        root.setRight(rightPanel);

        // Create scene with key handler
        Scene scene = createScene(root);

        // Configure and show stage
        configureStage(primaryStage, scene);

        // Start simulation
        service.log("Ecosystem statistics at step 0:");
        service.displayStatistics();
        service.runSimulation(maxSteps);
        root.requestFocus();
    }

    private Label createEvolutionLabel() {
        Label label = new Label("EVOLUTION NUMBER 0");
        label.setFont(Font.font(16));
        label.setPadding(new Insets(10));
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle("-fx-background-color: #3D3D3D; -fx-text-fill: #E0E0E0; -fx-border-color: #555555;");
        return label;
    }

    private TextArea createLogArea() {
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefWidth(400);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-control-inner-background: #383838; -fx-text-fill: #E0E0E0; -fx-background-color: #2D2D2D;");
        return logArea;
    }

    private ScrollPane createLogScrollPane(TextArea logArea) {
        ScrollPane scrollPane = new ScrollPane(logArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #2D2D2D; -fx-border-color: #555555; -fx-background-color: #2D2D2D;");
        return scrollPane;
    }

    private VBox createRightPanel(Label evolutionLabel, ScrollPane logScrollPane) {
        VBox rightPanel = new VBox(evolutionLabel, logScrollPane);
        VBox.setVgrow(logScrollPane, Priority.ALWAYS);
        rightPanel.setPrefWidth(400);
        rightPanel.setStyle("-fx-background-color: #2D2D2D;");
        rightPanel.setPadding(new Insets(10));
        rightPanel.setSpacing(10);
        return rightPanel;
    }

    private Scene createScene(BorderPane root) {
        Scene scene = new Scene(root, 1360, 960);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && stepCount < maxSteps) {
                service.nextStep();
                stepCount++;
            } else if (event.getCode() == KeyCode.C) {
                // Open CRUD window
                openCrudWindow();
            }
        });
        return scene;
    }

    private void openCrudWindow() {
        // new stage for the CRUD window
        Stage crudStage = new Stage();

        // inistialize crud window
        CRUDWindow crudWindow = new CRUDWindow();
        crudWindow.start(crudStage);

//        crudStage.setWidth(400);
//        crudStage.setHeight(960);
        crudStage.setMinWidth(400);
        crudStage.setMinHeight(990);
        crudStage.sizeToScene();
    }

    private void configureStage(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("WildSim Ecosystem Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
