package com.wildsim.environment;

import com.wildsim.model.organisms.plant.Tree;
import com.wildsim.model.organisms.Organism;
import com.wildsim.model.organisms.animal.Carnivore;
import com.wildsim.model.organisms.animal.Herbivore;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {
	private int width;
	private int height;
	private List<Organism> organisms;
	private char[][] matrix;

	public Ecosystem(int width, int height) {
		this.width = width;
		this.height = height;
		this.organisms = new ArrayList<>();
		this.matrix = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[i][j] = '.';
			}
		}
	}

	public void updateMatrix() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[i][j] = '.';
			}
		}
		for (Organism organism : organisms) {
			if (organism.isAlive()) {
				Position pos = organism.getPosition();
				matrix[pos.getY()][pos.getX()] = organism.getSymbol();
			}
		}
	}

	public void addOrganism(Organism organism) {
		if (isValidPosition(organism.getPosition())) {
			organisms.add(organism);
			updateMatrix();
		}
	}

	public Organism getOrganismAt(Position position) {
		for (Organism organism : organisms) {
			if (organism.getPosition().equals(position)) {
				return organism;
			}
		}
		return null;
	}
	public List<Organism> getOrganisms() {
		return organisms;
	}

	public boolean isValidPosition(Position position) {
		return position.getX() >= 0 && position.getX() < width && position.getY() >= 0 && position.getY() < height;
	}

	public void progressSimulation() {
		for (Organism organism : organisms) {
			if (organism.isAlive()) {
				organism.live(this);
			}
		}
		organisms.removeIf(organism -> !organism.isAlive());
		updateMatrix();
	}

	public void displayMatrix() {
		System.out.print("   ");
		for (int j = 0; j < width; j++) {
			System.out.print(j + " ");
		}
		System.out.println();

		// Print row numbers and matrix content
		for (int i = 0; i < height; i++) {
			System.out.print(i + " ");
			if (i < 10) {
				System.out.print(" ");
			}
			for (int j = 0; j < width; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void generateRandomTrees(int count) {
		for (int i = 0; i < count; i++) {
			Position position = Position.randomPosition(width, height);
			if (getOrganismAt(position) == null) {
				Tree tree = new Tree(position);
				addOrganism(tree);
			} else {
				i--; // retry if position is unavailable
			}
		}
	}

	public void generateRandomHerbivores(int count) {
		for (int i = 0; i < count; i++) {
			Position position = Position.randomPosition(width, height);
			if (getOrganismAt(position) == null) {
				Herbivore herbivore = new Herbivore(position, 30);
				addOrganism(herbivore);
			} else {
				i--; // retry if position is unavailable
			}
		}
	}

	public void generateRandomCarnivores(int count) {
		for (int i = 0; i < count; i++) {
			Position position = Position.randomPosition(width, height);
			if (getOrganismAt(position) == null) {
				Carnivore carnivore = new Carnivore(position, 30);
				addOrganism(carnivore);
			} else {
				i--; // retry if position is unavailable
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public char[][] getMatrix() {
		return matrix;
	}
}
