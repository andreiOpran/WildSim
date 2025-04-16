public class Carnivore extends Animal {
	public Carnivore(Position position, int energy) {
		super(position, energy, 'C', 2, 5);
	}

	@Override
	public void live(Ecosystem ecosystem) {
		Position foodPosition = findClosestFood(ecosystem);

		if(foodPosition != null && position.distanceTo(foodPosition) <= 1) {
			Organism food = ecosystem.getOrganismAt(foodPosition);
			if (food instanceof Herbivore && food.isAlive()) {
				eat(food);
			}
		} else {
			move(ecosystem);
		}
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

	protected void eat(Organism food) {
		if (food instanceof Herbivore) {
			energy += 20;
			// herbivore has 20% chance of living, but with minimal health
			if (Math.random() < 0.2) {
				food.setEnergy(5);
			} else {
				food.setAlive(false);
				System.out.println("Carnivore at " + position + " has eaten herbivore at " + food.getPosition());
			}
		}
	}
}
