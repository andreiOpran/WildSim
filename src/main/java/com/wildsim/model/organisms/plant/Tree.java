package com.wildsim.model.organisms.plant;

import com.wildsim.environment.Position;

public class Tree extends Plant {
	public Tree(Position position) {
		super(position, 30, 2, 60);
		this.symbol = 'T';
	}
}

