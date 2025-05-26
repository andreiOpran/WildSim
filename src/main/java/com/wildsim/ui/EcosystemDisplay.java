package com.wildsim.ui;

import com.wildsim.environment.Ecosystem;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class EcosystemDisplay extends StackPane {
	private Canvas canvas;
	private GraphicsContext gc;
	private Ecosystem ecosystem;
	private int cellSize  = 96; // size of each cell in pixels

	private Map<Character, Image> textures = new HashMap<>();

	public EcosystemDisplay(Ecosystem ecosystem) {
		this.ecosystem = ecosystem;
		int width = ecosystem.getWidth() * cellSize;
		int height = ecosystem.getHeight() * cellSize;

		canvas = new Canvas(width, height);
		gc = canvas.getGraphicsContext2D();

		loadTextures();

		getChildren().add(canvas);
		render();
	}

	private void loadTextures() {
		textures.put('.', new Image(getClass().getResourceAsStream("/images/empty.png")));
        textures.put('T', new Image(getClass().getResourceAsStream("/images/tree.png")));
        textures.put('H', new Image(getClass().getResourceAsStream("/images/herbivore.png")));
        textures.put('C', new Image(getClass().getResourceAsStream("/images/carnivore.png")));
	}

	public void render() {
		char[][] matrix = ecosystem.getMatrix();

		for (int i = 0; i < ecosystem.getHeight(); i++) {
			for (int j = 0; j < ecosystem.getWidth(); j++) {
				char symbol = matrix[i][j];
				Image texture = textures.getOrDefault(symbol, textures.get('.'));
				gc.drawImage(texture, j * cellSize, i * cellSize, cellSize, cellSize);
			}
		}
	}

	public void update() {
		render();
	}
}
