package com.wildsim.model.organisms;

import com.wildsim.environment.Ecosystem;
import com.wildsim.environment.Position;

public abstract class Organism {
	protected Position position;
	protected int energy;
	protected boolean alive;
	protected char symbol; // Symbol representing the organism in the matrix

	public Organism(Position position, int energy, boolean alive, char symbol) {
		this.position = position;
		this.energy = energy;
		this.alive = alive;
		this.symbol = symbol;
	}
	public Organism(Position position, int energy, char symbol) {
		this(position, energy, true, symbol);
	}


	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public char getSymbol() {
		return symbol;
	}
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}

    public abstract void live(Ecosystem ecosystem);

	public void checkDeath() {
		if (energy <= 0) {
			alive = false;
		}
	}
}
