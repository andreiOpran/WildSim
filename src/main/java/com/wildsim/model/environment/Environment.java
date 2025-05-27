package com.wildsim.model.environment;

import com.wildsim.environment.Position;

public class Environment {
	protected Position position;
	protected char symbol;

	public Environment(Position position) {
		this.position = position;
		this.symbol = 'E';
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public char getSymbol() {
		return symbol;
	}
}