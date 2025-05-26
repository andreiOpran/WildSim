package com.wildsim.model.organisms.animal;

import com.wildsim.environment.Ecosystem;
import com.wildsim.model.organisms.plant.Plant;
import com.wildsim.environment.Position;
import com.wildsim.model.organisms.Organism;

public class Herbivore extends Animal {
	public Herbivore (Position position, int energy) {
		super(position, energy, 'H', 1, 3);
	}

	public Herbivore(Position position, int energy, boolean alive, char symbol, int movementSpeed, int visionRange) {
        super(position, energy, symbol, movementSpeed, visionRange);
        this.alive = alive;
    }

	@Override
	protected Position findClosestFood(Ecosystem ecosystem) {
		Position closestFood = null;
		double closestDistance = Double.MAX_VALUE;
		for (Organism organism : ecosystem.getOrganisms()) {
			if (organism instanceof Plant && organism.isAlive()) {
				double distance = position.distanceTo(organism.getPosition());
				if (distance <= visionRange && distance < closestDistance) {
					closestDistance = distance;
					closestFood = organism.getPosition();
				}
			}
		}
		return closestFood;
	}

	@Override
	protected String eat(Organism food) {
		if (food instanceof Plant) {
			energy += 10;
			food.setEnergy(food.getEnergy() - 10);
			food.checkDeath();
			if (!food.isAlive()) {
				return "Herbivore at " + position + " has eaten plant at " + food.getPosition();
			}
		}
		return "";
	}

	@Override
	public String live(Ecosystem ecosystem) {
		Position foodPosition = findClosestFood(ecosystem);

		if (foodPosition != null && position.distanceTo(foodPosition) <= 1) {
			Organism food = ecosystem.getOrganismAt(foodPosition);
			if (food instanceof Plant && food.isAlive()) {
				String log = eat(food);
				return log;
			}
		} else {
			move(ecosystem);
		}
		return "";
	}

}

