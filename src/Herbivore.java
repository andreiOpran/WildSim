public class Herbivore extends Animal {
	public Herbivore (Position position, int energy) {
		super(position, energy, 'H', 1, 3);
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
	protected void eat(Organism food) {
		if (food instanceof Plant) {
			energy += 10;
			food.setEnergy(food.getEnergy() - 10);
			food.checkDeath();
			if (!food.isAlive()) {
				System.out.println("Herbivore at " + position + " has eaten plant at " + food.getPosition());
			}
		}
	}

	@Override
	public void live(Ecosystem ecosystem) {
		Position foodPosition = findClosestFood(ecosystem);

		if (foodPosition != null && position.distanceTo(foodPosition) <= 1) {
			Organism food = ecosystem.getOrganismAt(foodPosition);
			if (food instanceof Plant && food.isAlive()) {
				eat(food);
			}
		} else {
			move(ecosystem);
		}
	}


}
