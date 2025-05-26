package com.wildsim.model.organisms.plant;

import com.wildsim.environment.Position;

public class Tree extends Plant {
	public Tree(Position position) {
		super(position, 30, 2, 60);
		this.symbol = 'T';
	}

	public Tree(Position position, int energy, boolean alive, char symbol, int growthRate, int energyThreshold) {
        super(position, energy, growthRate, energyThreshold);
        this.alive = alive;
        this.symbol = symbol;
    }
}

