package com.wildsim.ui;

import com.wildsim.environment.Position;
import com.wildsim.model.environment.WaterSource;
import com.wildsim.model.organisms.animal.Carnivore;
import com.wildsim.model.organisms.animal.Herbivore;
import com.wildsim.model.organisms.plant.Tree;
import com.wildsim.service.MongoDBService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainCRUDWindow extends Application {
    private MongoDBService dbService;

    // Trees
    private TableView<Tree> treeTable;
    private TextField treeXField, treeYField, treeEnergyField, treeSymbolField, treeGrowthRateField, treeEnergyThresholdField;
    private TextField originalTreeXField, originalTreeYField;
    private CheckBox treeAliveCheckBox;

    // Carnivores
    private TableView<Carnivore> carnivoreTable;
    private TextField carnivoreXField, carnivoreYField, carnivoreEnergyField, carnivoreSymbolField, carnivoreSpeedField, carnivoreVisionField;
    private TextField originalCarnivoreXField, originalCarnivoreYField;
    private CheckBox carnivoreAliveCheckBox;

    // Herbivores
    private TableView<Herbivore> herbivoreTable;
    private TextField herbivoreXField, herbivoreYField, herbivoreEnergyField, herbivoreSymbolField, herbivoreSpeedField, herbivoreVisionField;
    private TextField originalHerbivoreXField, originalHerbivoreYField;
    private CheckBox herbivoreAliveCheckBox;

    // Water Sources
    private TableView<WaterSource> waterSourceTable;
    private TextField waterSourceXField, waterSourceYField, waterLevelField;
    private TextField originalWaterSourceXField, originalWaterSourceYField;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the database service
        dbService = new MongoDBService();

        // Create tabs
        TabPane tabPane = new TabPane();

        Tab treeTab = new Tab("Trees", createTreeTab());
        Tab carnivoreTab = new Tab("Carnivores", createCarnivoreTab());
        Tab herbivoreTab = new Tab("Herbivores", createHerbivoreTab());
        Tab waterSourceTab = new Tab("Water Sources", createWaterSourceTab());

        treeTab.setClosable(false);
        carnivoreTab.setClosable(false);
        herbivoreTab.setClosable(false);
        waterSourceTab.setClosable(false);

        tabPane.getTabs().addAll(treeTab, carnivoreTab, herbivoreTab, waterSourceTab);

        // Refresh data on tab selection
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab.getText().equals("Trees")) {
                refreshTreeTable();
            } else if (newTab.getText().equals("Carnivores")) {
                refreshCarnivoreTable();
            } else if (newTab.getText().equals("Herbivores")) {
                refreshHerbivoreTable();
            } else if (newTab.getText().equals("Water Sources")) {
                refreshWaterSourceTable();
            }
        });

        Scene scene = new Scene(tabPane, 700, 850);

        // Apply dark mode styling
        String darkModeCSS =
                ".root { -fx-background-color: #2b2b2b; -fx-font-family: 'Segoe UI'; }" +
                        ".tab-pane .tab-header-area .tab-header-background { -fx-background-color: #1e1e1e; }" +
                        ".tab-pane .tab { -fx-background-color: #3c3f41; }" +
                        ".tab-pane .tab:selected { -fx-background-color: #4e5254; }" +
                        ".tab-pane .tab .tab-label { -fx-text-fill: #bbbbbb; }" +
                        ".tab-pane .tab:selected .tab-label { -fx-text-fill: #ffffff; }" +
                        ".table-view { -fx-background-color: #3c3f41; -fx-table-cell-border-color: #323232; }" +
                        ".table-view .column-header-background { -fx-background-color: #2b2b2b; }" +
                        ".table-view .column-header-background .filler { -fx-background-color: #2b2b2b; }" +
                        ".table-view .column-header-background .show-hide-columns-button { -fx-background-color: #2b2b2b; }" +
                        ".table-view .column-drag-header { -fx-background-color: #2b2b2b; }" +
                        ".table-view .column-header { -fx-background-color: #2b2b2b; }" +
                        ".table-view .column-header .label { -fx-text-fill: #bbbbbb; }" +
                        ".table-view .table-cell { -fx-text-fill: #bbbbbb; }" +
                        ".table-view .table-row-cell { -fx-background-color: #3c3f41; -fx-border-color: #323232; }" +
                        ".table-view .table-row-cell:odd { -fx-background-color: #323232; }" +
                        ".table-view .table-row-cell:selected { -fx-background-color: #4b6eaf; }" +
                        ".table-view .arrow { -fx-background-color: #bbbbbb; }" +
                        ".text-field { -fx-background-color: #45494a; -fx-text-fill: #bbbbbb; -fx-prompt-text-fill: #777777; }" +
                        ".label { -fx-text-fill: #bbbbbb; }" +
                        ".button { -fx-background-color: #4D5052; -fx-text-fill: #bbbbbb; -fx-background-radius: 3px; -fx-border-color: transparent; -fx-border-width: 1px; -fx-border-radius: 3px; }" +
                        ".button:hover { -fx-background-color: #5A5D5F; -fx-cursor: hand; }" +
                        ".button:pressed { -fx-background-color: #404244; }" +
                        ".button:focused { -fx-border-color: #6A6F73; }" +
                        ".check-box { -fx-text-fill: #bbbbbb; }" +
                        ".check-box .box { -fx-background-color: #45494a; -fx-border-color: #6A6F73; }" +
                        ".check-box:selected .mark { -fx-background-color: #bbbbbb; }";

        scene.getStylesheets().add("data:text/css," + darkModeCSS.replace(" ", "%20"));

        primaryStage.setTitle("WildSim CRUD Manager");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial data load
        refreshTreeTable();
    }

    // Tree Tab
    private BorderPane createTreeTab() {
        BorderPane pane = new BorderPane();

        // Create table
        treeTable = new TableView<>();

        TableColumn<Tree, Integer> xCol = new TableColumn<>("X");
        xCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getPosition().getX()).asObject());

        TableColumn<Tree, Integer> yCol = new TableColumn<>("Y");
        yCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getPosition().getY()).asObject());

        TableColumn<Tree, Integer> energyCol = new TableColumn<>("Energy");
        energyCol.setCellValueFactory(new PropertyValueFactory<>("energy"));

        TableColumn<Tree, Boolean> aliveCol = new TableColumn<>("Alive");
        aliveCol.setCellValueFactory(new PropertyValueFactory<>("alive"));

        TableColumn<Tree, Character> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));

        TableColumn<Tree, Integer> growthRateCol = new TableColumn<>("Growth Rate");
        growthRateCol.setCellValueFactory(new PropertyValueFactory<>("growthRate"));
        growthRateCol.setPrefWidth(110);

        TableColumn<Tree, Integer> thresholdCol = new TableColumn<>("Energy Threshold");
        thresholdCol.setCellValueFactory(new PropertyValueFactory<>("energyThreshold"));
        thresholdCol.setPrefWidth(140);

        treeTable.getColumns().addAll(xCol, yCol, energyCol, aliveCol, symbolCol, growthRateCol, thresholdCol);
        pane.setCenter(treeTable);

        // Form for creating/updating trees
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        // Hidden fields for original position
        originalTreeXField = new TextField();
        originalTreeYField = new TextField();

        form.add(new Label("X:"), 0, 0);
        treeXField = new TextField();
        form.add(treeXField, 1, 0);

        form.add(new Label("Y:"), 0, 1);
        treeYField = new TextField();
        form.add(treeYField, 1, 1);

        form.add(new Label("Energy:"), 0, 2);
        treeEnergyField = new TextField();
        form.add(treeEnergyField, 1, 2);

        form.add(new Label("Alive:"), 0, 3);
        treeAliveCheckBox = new CheckBox();
        treeAliveCheckBox.setSelected(true);
        form.add(treeAliveCheckBox, 1, 3);

        form.add(new Label("Symbol:"), 0, 4);
        treeSymbolField = new TextField();
        treeSymbolField.setText("T");
        form.add(treeSymbolField, 1, 4);

        form.add(new Label("Growth Rate:"), 0, 5);
        treeGrowthRateField = new TextField();
        treeGrowthRateField.setText("2");
        form.add(treeGrowthRateField, 1, 5);

        form.add(new Label("Energy Threshold:"), 0, 6);
        treeEnergyThresholdField = new TextField();
        treeEnergyThresholdField.setText("60");
        form.add(treeEnergyThresholdField, 1, 6);

        // Buttons
        Button createBtn = new Button("Create");
        createBtn.setOnAction(e -> createTree());

        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(e -> updateTree());
        updateBtn.setDisable(true);

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> deleteTree());

        Button clearBtn = new Button("Clear Form");
        clearBtn.setOnAction(e -> clearTreeForm());

        HBox buttonBox = new HBox(10, createBtn, updateBtn, deleteBtn, clearBtn);
        buttonBox.setPadding(new Insets(10));

        // Set up selection listener
        treeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                originalTreeXField.setText(String.valueOf(newSelection.getPosition().getX()));
                originalTreeYField.setText(String.valueOf(newSelection.getPosition().getY()));

                treeXField.setText(String.valueOf(newSelection.getPosition().getX()));
                treeYField.setText(String.valueOf(newSelection.getPosition().getY()));
                treeEnergyField.setText(String.valueOf(newSelection.getEnergy()));
                treeAliveCheckBox.setSelected(newSelection.isAlive());
                treeSymbolField.setText(String.valueOf(newSelection.getSymbol()));
                treeGrowthRateField.setText(String.valueOf(newSelection.getGrowthRate()));
                treeEnergyThresholdField.setText(String.valueOf(newSelection.getEnergyThreshold()));

                updateBtn.setDisable(false);  // Enable when row selected
            } else {
                updateBtn.setDisable(true);   // Disable when no selection
            }
        });

        VBox bottomBox = new VBox(10, form, buttonBox);
        pane.setBottom(bottomBox);

        return pane;
    }

    // Carnivore Tab
    private BorderPane createCarnivoreTab() {
        BorderPane pane = new BorderPane();

        // Create table
        carnivoreTable = new TableView<>();

        TableColumn<Carnivore, Integer> xCol = new TableColumn<>("X");
        xCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getPosition().getX()).asObject());

        TableColumn<Carnivore, Integer> yCol = new TableColumn<>("Y");
        yCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getPosition().getY()).asObject());

        TableColumn<Carnivore, Integer> energyCol = new TableColumn<>("Energy");
        energyCol.setCellValueFactory(new PropertyValueFactory<>("energy"));

        TableColumn<Carnivore, Boolean> aliveCol = new TableColumn<>("Alive");
        aliveCol.setCellValueFactory(new PropertyValueFactory<>("alive"));

        TableColumn<Carnivore, Character> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));

        TableColumn<Carnivore, Integer> speedCol = new TableColumn<>("Speed");
        speedCol.setCellValueFactory(new PropertyValueFactory<>("movementSpeed"));

        TableColumn<Carnivore, Integer> visionCol = new TableColumn<>("Vision Range");
        visionCol.setCellValueFactory(new PropertyValueFactory<>("visionRange"));
        visionCol.setPrefWidth(110);

        carnivoreTable.getColumns().addAll(xCol, yCol, energyCol, aliveCol, symbolCol, speedCol, visionCol);
        pane.setCenter(carnivoreTable);

        // Form for creating/updating carnivores
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        // Hidden fields for original position
        originalCarnivoreXField = new TextField();
        originalCarnivoreYField = new TextField();

        form.add(new Label("X:"), 0, 0);
        carnivoreXField = new TextField();
        form.add(carnivoreXField, 1, 0);

        form.add(new Label("Y:"), 0, 1);
        carnivoreYField = new TextField();
        form.add(carnivoreYField, 1, 1);

        form.add(new Label("Energy:"), 0, 2);
        carnivoreEnergyField = new TextField();
        form.add(carnivoreEnergyField, 1, 2);

        form.add(new Label("Alive:"), 0, 3);
        carnivoreAliveCheckBox = new CheckBox();
        carnivoreAliveCheckBox.setSelected(true);
        form.add(carnivoreAliveCheckBox, 1, 3);

        form.add(new Label("Symbol:"), 0, 4);
        carnivoreSymbolField = new TextField();
        carnivoreSymbolField.setText("C");
        form.add(carnivoreSymbolField, 1, 4);

        form.add(new Label("Movement Speed:"), 0, 5);
        carnivoreSpeedField = new TextField();
        carnivoreSpeedField.setText("2");
        form.add(carnivoreSpeedField, 1, 5);

        form.add(new Label("Vision Range:"), 0, 6);
        carnivoreVisionField = new TextField();
        carnivoreVisionField.setText("5");
        form.add(carnivoreVisionField, 1, 6);

        // Buttons
        Button createBtn = new Button("Create");
        createBtn.setOnAction(e -> createCarnivore());

        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(e -> updateCarnivore());
        updateBtn.setDisable(true);

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> deleteCarnivore());

        Button clearBtn = new Button("Clear Form");
        clearBtn.setOnAction(e -> clearCarnivoreForm());

        HBox buttonBox = new HBox(10, createBtn, updateBtn, deleteBtn, clearBtn);
        buttonBox.setPadding(new Insets(10));

        // Set up selection listener
        carnivoreTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                originalCarnivoreXField.setText(String.valueOf(newSelection.getPosition().getX()));
                originalCarnivoreYField.setText(String.valueOf(newSelection.getPosition().getY()));

                carnivoreXField.setText(String.valueOf(newSelection.getPosition().getX()));
                carnivoreYField.setText(String.valueOf(newSelection.getPosition().getY()));
                carnivoreEnergyField.setText(String.valueOf(newSelection.getEnergy()));
                carnivoreAliveCheckBox.setSelected(newSelection.isAlive());
                carnivoreSymbolField.setText(String.valueOf(newSelection.getSymbol()));
                carnivoreSpeedField.setText(String.valueOf(newSelection.getMovementSpeed()));
                carnivoreVisionField.setText(String.valueOf(newSelection.getVisionRange()));

                updateBtn.setDisable(false);
            } else {
                updateBtn.setDisable(true);   // Disable when no selection
            }
        });

        VBox bottomBox = new VBox(10, form, buttonBox);
        pane.setBottom(bottomBox);

        return pane;
    }

    // Herbivore Tab
    private BorderPane createHerbivoreTab() {
        BorderPane pane = new BorderPane();

        // Create table
        herbivoreTable = new TableView<>();

        TableColumn<Herbivore, Integer> xCol = new TableColumn<>("X");
        xCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getPosition().getX()).asObject());

        TableColumn<Herbivore, Integer> yCol = new TableColumn<>("Y");
        yCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getPosition().getY()).asObject());

        TableColumn<Herbivore, Integer> energyCol = new TableColumn<>("Energy");
        energyCol.setCellValueFactory(new PropertyValueFactory<>("energy"));

        TableColumn<Herbivore, Boolean> aliveCol = new TableColumn<>("Alive");
        aliveCol.setCellValueFactory(new PropertyValueFactory<>("alive"));

        TableColumn<Herbivore, Character> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));

        TableColumn<Herbivore, Integer> speedCol = new TableColumn<>("Speed");
        speedCol.setCellValueFactory(new PropertyValueFactory<>("movementSpeed"));

        TableColumn<Herbivore, Integer> visionCol = new TableColumn<>("Vision Range");
        visionCol.setCellValueFactory(new PropertyValueFactory<>("visionRange"));
        visionCol.setPrefWidth(110);

        herbivoreTable.getColumns().addAll(xCol, yCol, energyCol, aliveCol, symbolCol, speedCol, visionCol);
        pane.setCenter(herbivoreTable);

        // Form for creating/updating herbivores
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        // Hidden fields for original position
        originalHerbivoreXField = new TextField();
        originalHerbivoreYField = new TextField();

        form.add(new Label("X:"), 0, 0);
        herbivoreXField = new TextField();
        form.add(herbivoreXField, 1, 0);

        form.add(new Label("Y:"), 0, 1);
        herbivoreYField = new TextField();
        form.add(herbivoreYField, 1, 1);

        form.add(new Label("Energy:"), 0, 2);
        herbivoreEnergyField = new TextField();
        form.add(herbivoreEnergyField, 1, 2);

        form.add(new Label("Alive:"), 0, 3);
        herbivoreAliveCheckBox = new CheckBox();
        herbivoreAliveCheckBox.setSelected(true);
        form.add(herbivoreAliveCheckBox, 1, 3);

        form.add(new Label("Symbol:"), 0, 4);
        herbivoreSymbolField = new TextField();
        herbivoreSymbolField.setText("H");
        form.add(herbivoreSymbolField, 1, 4);

        form.add(new Label("Movement Speed:"), 0, 5);
        herbivoreSpeedField = new TextField();
        herbivoreSpeedField.setText("1");
        form.add(herbivoreSpeedField, 1, 5);

        form.add(new Label("Vision Range:"), 0, 6);
        herbivoreVisionField = new TextField();
        herbivoreVisionField.setText("3");
        form.add(herbivoreVisionField, 1, 6);

        // Buttons
        Button createBtn = new Button("Create");
        createBtn.setOnAction(e -> createHerbivore());

        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(e -> updateHerbivore());
        updateBtn.setDisable(true);

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> deleteHerbivore());

        Button clearBtn = new Button("Clear Form");
        clearBtn.setOnAction(e -> clearHerbivoreForm());

        HBox buttonBox = new HBox(10, createBtn, updateBtn, deleteBtn, clearBtn);
        buttonBox.setPadding(new Insets(10));

        // Set up selection listener
        herbivoreTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                originalHerbivoreXField.setText(String.valueOf(newSelection.getPosition().getX()));
                originalHerbivoreYField.setText(String.valueOf(newSelection.getPosition().getY()));

                herbivoreXField.setText(String.valueOf(newSelection.getPosition().getX()));
                herbivoreYField.setText(String.valueOf(newSelection.getPosition().getY()));
                herbivoreEnergyField.setText(String.valueOf(newSelection.getEnergy()));
                herbivoreAliveCheckBox.setSelected(newSelection.isAlive());
                herbivoreSymbolField.setText(String.valueOf(newSelection.getSymbol()));
                herbivoreSpeedField.setText(String.valueOf(newSelection.getMovementSpeed()));
                herbivoreVisionField.setText(String.valueOf(newSelection.getVisionRange()));

                updateBtn.setDisable(false);
            } else {
                updateBtn.setDisable(true);   // Disable when no selection
            }
        });

        VBox bottomBox = new VBox(10, form, buttonBox);
        pane.setBottom(bottomBox);

        return pane;
    }

    // Water Source Tab
    private BorderPane createWaterSourceTab() {
        BorderPane pane = new BorderPane();

        // Create table
        waterSourceTable = new TableView<>();

        TableColumn<WaterSource, Integer> xCol = new TableColumn<>("X");
        xCol.setCellValueFactory(new PropertyValueFactory<>("x"));

        TableColumn<WaterSource, Integer> yCol = new TableColumn<>("Y");
        yCol.setCellValueFactory(new PropertyValueFactory<>("y"));

        TableColumn<WaterSource, Double> waterLevelCol = new TableColumn<>("Water Level");
        waterLevelCol.setCellValueFactory(new PropertyValueFactory<>("waterLevel"));
        waterLevelCol.setPrefWidth(110);

        waterSourceTable.getColumns().addAll(xCol, yCol, waterLevelCol);
        pane.setCenter(waterSourceTable);

        // Form for creating/updating water sources
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        // Hidden fields for original position
        originalWaterSourceXField = new TextField();
        originalWaterSourceYField = new TextField();

        form.add(new Label("X:"), 0, 0);
        waterSourceXField = new TextField();
        form.add(waterSourceXField, 1, 0);

        form.add(new Label("Y:"), 0, 1);
        waterSourceYField = new TextField();
        form.add(waterSourceYField, 1, 1);

        form.add(new Label("Water Level:"), 0, 2);
        waterLevelField = new TextField();
        form.add(waterLevelField, 1, 2);

        // Buttons
        Button createBtn = new Button("Create");
        createBtn.setOnAction(e -> createWaterSource());

        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(e -> updateWaterSource());
        updateBtn.setDisable(true);

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> deleteWaterSource());

        Button clearBtn = new Button("Clear Form");
        clearBtn.setOnAction(e -> clearWaterSourceForm());

        HBox buttonBox = new HBox(10, createBtn, updateBtn, deleteBtn, clearBtn);
        buttonBox.setPadding(new Insets(10));

        // Set up selection listener
        waterSourceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                originalWaterSourceXField.setText(String.valueOf(newSelection.getX()));
                originalWaterSourceYField.setText(String.valueOf(newSelection.getY()));

                waterSourceXField.setText(String.valueOf(newSelection.getX()));
                waterSourceYField.setText(String.valueOf(newSelection.getY()));
                waterLevelField.setText(String.valueOf(newSelection.getWaterLevel()));

                updateBtn.setDisable(false);
            } else {
                updateBtn.setDisable(true);   // Disable when no selection
            }
        });

        VBox bottomBox = new VBox(10, form, buttonBox);
        pane.setBottom(bottomBox);

        return pane;
    }

    // CRUD Operations for Trees
    private void refreshTreeTable() {
        ObservableList<Tree> trees = FXCollections.observableArrayList(dbService.getAllTrees());
        treeTable.setItems(trees);
    }

    private void createTree() {
        try {
            int x = Integer.parseInt(treeXField.getText());
            int y = Integer.parseInt(treeYField.getText());
            int energy = Integer.parseInt(treeEnergyField.getText());
            boolean alive = treeAliveCheckBox.isSelected();
            char symbol = treeSymbolField.getText().charAt(0);
            int growthRate = Integer.parseInt(treeGrowthRateField.getText());
            int threshold = Integer.parseInt(treeEnergyThresholdField.getText());

            Position position = new Position(x, y);

            // Check if position is already occupied
            if (dbService.isPositionOccupied(position)) {
                showAlert("Position Error", "Position (" + x + "," + y + ") is already occupied by another entity.");
                return;
            }

            Tree tree = new Tree(position, energy, alive, symbol, growthRate, threshold);

            dbService.createTree(tree);
            refreshTreeTable();
            clearTreeForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for all fields.");
        }
    }

    private void updateTree() {
        try {
            // Save original position first
            Position originalPosition = new Position(
                    Integer.parseInt(originalTreeXField.getText()),
                    Integer.parseInt(originalTreeYField.getText())
            );

            int x = Integer.parseInt(treeXField.getText());
            int y = Integer.parseInt(treeYField.getText());
            int energy = Integer.parseInt(treeEnergyField.getText());
            boolean alive = treeAliveCheckBox.isSelected();
            char symbol = treeSymbolField.getText().charAt(0);
            int growthRate = Integer.parseInt(treeGrowthRateField.getText());
            int threshold = Integer.parseInt(treeEnergyThresholdField.getText());

            Position position = new Position(x, y);

            // Check if position is already occupied
            if (dbService.isPositionOccupied(position)) {
                showAlert("Position Error", "Position (" + x + "," + y + ") is already occupied by another entity.");
                return;
            }

            Tree tree = new Tree(position, energy, alive, symbol, growthRate, threshold);

            dbService.updateTree(tree, originalPosition);
            refreshTreeTable();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for all fields.");
        }
    }

    private void deleteTree() {
        try {
            int x = Integer.parseInt(treeXField.getText());
            int y = Integer.parseInt(treeYField.getText());

            Position position = new Position(x, y);
            dbService.deleteTree(position);
            refreshTreeTable();
            clearTreeForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for X and Y fields.");
        }
    }

    private void clearTreeForm() {
        treeXField.clear();
        treeYField.clear();
        treeEnergyField.clear();
        treeAliveCheckBox.setSelected(true);
        treeSymbolField.setText("T");
        treeGrowthRateField.setText("2");
        treeEnergyThresholdField.setText("60");
        originalTreeXField.clear();
        originalTreeYField.clear();
    }

    // CRUD Operations for Carnivores
    private void refreshCarnivoreTable() {
        ObservableList<Carnivore> carnivores = FXCollections.observableArrayList(dbService.getAllCarnivores());
        carnivoreTable.setItems(carnivores);
    }

    private void createCarnivore() {
        try {
            int x = Integer.parseInt(carnivoreXField.getText());
            int y = Integer.parseInt(carnivoreYField.getText());
            int energy = Integer.parseInt(carnivoreEnergyField.getText());
            boolean alive = carnivoreAliveCheckBox.isSelected();
            char symbol = carnivoreSymbolField.getText().charAt(0);
            int speed = Integer.parseInt(carnivoreSpeedField.getText());
            int vision = Integer.parseInt(carnivoreVisionField.getText());

            Position position = new Position(x, y);

            // Check if position is already occupied
            if (dbService.isPositionOccupied(position)) {
                showAlert("Position Error", "Position (" + x + "," + y + ") is already occupied by another entity.");
                return;
            }

            Carnivore carnivore = new Carnivore(position, energy, alive, symbol, speed, vision);

            dbService.createCarnivore(carnivore);
            refreshCarnivoreTable();
            clearCarnivoreForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for all fields.");
        }
    }

    private void updateCarnivore() {
        try {
            // Save original position first
            Position originalPosition = new Position(
                    Integer.parseInt(originalCarnivoreXField.getText()),
                    Integer.parseInt(originalCarnivoreYField.getText())
            );

            int x = Integer.parseInt(carnivoreXField.getText());
            int y = Integer.parseInt(carnivoreYField.getText());
            int energy = Integer.parseInt(carnivoreEnergyField.getText());
            boolean alive = carnivoreAliveCheckBox.isSelected();
            char symbol = carnivoreSymbolField.getText().charAt(0);
            int speed = Integer.parseInt(carnivoreSpeedField.getText());
            int vision = Integer.parseInt(carnivoreVisionField.getText());

            Position position = new Position(x, y);

            // Check if position is already occupied
            if (dbService.isPositionOccupied(position)) {
                showAlert("Position Error", "Position (" + x + "," + y + ") is already occupied by another entity.");
                return;
            }

            Carnivore carnivore = new Carnivore(position, energy, alive, symbol, speed, vision);

            dbService.updateCarnivore(carnivore, originalPosition);
            refreshCarnivoreTable();
            clearCarnivoreForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for all fields.");
        }
    }

    private void deleteCarnivore() {
        try {
            int x = Integer.parseInt(carnivoreXField.getText());
            int y = Integer.parseInt(carnivoreYField.getText());

            Position position = new Position(x, y);
            dbService.deleteCarnivore(position);
            refreshCarnivoreTable();
            clearCarnivoreForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for X and Y fields.");
        }
    }

    private void clearCarnivoreForm() {
        carnivoreXField.clear();
        carnivoreYField.clear();
        carnivoreEnergyField.clear();
        carnivoreAliveCheckBox.setSelected(true);
        carnivoreSymbolField.setText("C");
        carnivoreSpeedField.setText("2");
        carnivoreVisionField.setText("5");
        originalCarnivoreXField.clear();
        originalCarnivoreYField.clear();
    }

    // CRUD Operations for Herbivores
    private void refreshHerbivoreTable() {
        ObservableList<Herbivore> herbivores = FXCollections.observableArrayList(dbService.getAllHerbivores());
        herbivoreTable.setItems(herbivores);
    }

    private void createHerbivore() {
        try {
            int x = Integer.parseInt(herbivoreXField.getText());
            int y = Integer.parseInt(herbivoreYField.getText());
            int energy = Integer.parseInt(herbivoreEnergyField.getText());
            boolean alive = herbivoreAliveCheckBox.isSelected();
            char symbol = herbivoreSymbolField.getText().charAt(0);
            int speed = Integer.parseInt(herbivoreSpeedField.getText());
            int vision = Integer.parseInt(herbivoreVisionField.getText());

            Position position = new Position(x, y);

            // Check if position is already occupied
            if (dbService.isPositionOccupied(position)) {
                showAlert("Position Error", "Position (" + x + "," + y + ") is already occupied by another entity.");
                return;
            }

            Herbivore herbivore = new Herbivore(position, energy, alive, symbol, speed, vision);

            dbService.createHerbivore(herbivore);
            refreshHerbivoreTable();
            clearHerbivoreForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for all fields.");
        }
    }

    private void updateHerbivore() {
        try {
            // Save original position first
            Position originalPosition = new Position(
                    Integer.parseInt(originalHerbivoreXField.getText()),
                    Integer.parseInt(originalHerbivoreYField.getText())
            );

            int x = Integer.parseInt(herbivoreXField.getText());
            int y = Integer.parseInt(herbivoreYField.getText());
            int energy = Integer.parseInt(herbivoreEnergyField.getText());
            boolean alive = herbivoreAliveCheckBox.isSelected();
            char symbol = herbivoreSymbolField.getText().charAt(0);
            int speed = Integer.parseInt(herbivoreSpeedField.getText());
            int vision = Integer.parseInt(herbivoreVisionField.getText());

            Position position = new Position(x, y);

            // Check if position is already occupied
            if (dbService.isPositionOccupied(position)) {
                showAlert("Position Error", "Position (" + x + "," + y + ") is already occupied by another entity.");
                return;
            }

            Herbivore herbivore = new Herbivore(position, energy, alive, symbol, speed, vision);

            dbService.updateHerbivore(herbivore, originalPosition);
            refreshHerbivoreTable();
            clearHerbivoreForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for all fields.");
        }
    }

    private void deleteHerbivore() {
        try {
            int x = Integer.parseInt(herbivoreXField.getText());
            int y = Integer.parseInt(herbivoreYField.getText());

            Position position = new Position(x, y);
            dbService.deleteHerbivore(position);
            refreshHerbivoreTable();
            clearHerbivoreForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for X and Y fields.");
        }
    }

    private void clearHerbivoreForm() {
        herbivoreXField.clear();
        herbivoreYField.clear();
        herbivoreEnergyField.clear();
        herbivoreAliveCheckBox.setSelected(true);
        herbivoreSymbolField.setText("H");
        herbivoreSpeedField.setText("1");
        herbivoreVisionField.setText("3");
        originalHerbivoreXField.clear();
        originalHerbivoreYField.clear();
    }

    // CRUD Operations for Water Sources
    private void refreshWaterSourceTable() {
        ObservableList<WaterSource> waterSources = FXCollections.observableArrayList(dbService.getAllWaterSources());
        waterSourceTable.setItems(waterSources);
    }

    private void createWaterSource() {
        try {
            int x = Integer.parseInt(waterSourceXField.getText());
            int y = Integer.parseInt(waterSourceYField.getText());
            double waterLevel = Double.parseDouble(waterLevelField.getText());

            // Check if position is already occupied
            Position position = new Position(x, y);
            if (dbService.isPositionOccupied(position)) {
                showAlert("Position Error", "Position (" + x + "," + y + ") is already occupied by another entity.");
                return;
            }

            WaterSource waterSource = new WaterSource(x, y, waterLevel);

            dbService.createWaterSource(waterSource);
            refreshWaterSourceTable();
            clearWaterSourceForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for all fields.");
        }
    }

    private void updateWaterSource() {
        try {
            // Save original coordinates
            int originalX = Integer.parseInt(originalWaterSourceXField.getText());
            int originalY = Integer.parseInt(originalWaterSourceYField.getText());

            int x = Integer.parseInt(waterSourceXField.getText());
            int y = Integer.parseInt(waterSourceYField.getText());
            double waterLevel = Double.parseDouble(waterLevelField.getText());

            // Check if position is already occupied
            Position position = new Position(x, y);
            if (dbService.isPositionOccupied(position)) {
                showAlert("Position Error", "Position (" + x + "," + y + ") is already occupied by another entity.");
                return;
            }

            WaterSource waterSource = new WaterSource(x, y, waterLevel);

            dbService.updateWaterSource(waterSource, originalX, originalY);
            refreshWaterSourceTable();
            clearWaterSourceForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for all fields.");
        }
    }

    private void deleteWaterSource() {
        try {
            int x = Integer.parseInt(waterSourceXField.getText());
            int y = Integer.parseInt(waterSourceYField.getText());

            dbService.deleteWaterSource(x, y);
            refreshWaterSourceTable();
            clearWaterSourceForm();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for X and Y fields.");
        }
    }

    private void clearWaterSourceForm() {
        waterSourceXField.clear();
        waterSourceYField.clear();
        waterLevelField.clear();
        originalWaterSourceXField.clear();
        originalWaterSourceYField.clear();
    }

    // Utility methods
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}