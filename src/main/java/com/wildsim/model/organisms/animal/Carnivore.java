package com.wildsim.model.organisms.animal;

import com.wildsim.environment.Ecosystem;
import com.wildsim.environment.Position;
import com.wildsim.model.organisms.Organism;

public class Carnivore extends Animal {
	public Carnivore(Position position, int energy) {
		super(position, energy, 'C', 2, 5);
	}

	public Carnivore(Position position, int energy, boolean alive, char symbol, int movementSpeed, int visionRange) {
        super(position, energy, symbol, movementSpeed, visionRange);
        this.alive = alive;
    }

	@Override
	public String live(Ecosystem ecosystem) {
		Position foodPosition = findClosestFood(ecosystem);

		if(foodPosition != null && position.distanceTo(foodPosition) <= 1) {
			Organism food = ecosystem.getOrganismAt(foodPosition);
			if (food instanceof Herbivore && food.isAlive()) {
				String log = eat(food);
				return log;
			}
		} else {
			move(ecosystem);
		}
		return "";
	}

	@Override
	protected Position findClosestFood(Ecosystem ecosystem) {
		Position closestFood = null;
		double closestDistance = Double.MAX_VALUE;

		for(Organism organism : ecosystem.getOrganisms()) {
			if (organism instanceof Herbivore && organism.isAlive()) {
				double distance = position.distanceTo(organism.getPosition());
				if (distance <= visionRange && distance < closestDistance) {
					closestDistance = distance;
					closestFood = organism.getPosition();
				}
			}
		}
		return closestFood;
	}

	protected String eat(Organism food) {
		if (food instanceof Herbivore) {
			energy += 20;
			// herbivore has 20% chance of living, but with minimal health
			if (Math.random() < 0.2) {
				food.setEnergy(5);
			} else {
				food.setAlive(false);
				return "Carnivore at " + position + " has eaten herbivore at " + food.getPosition();
			}
		}
		return "";
	}
}
