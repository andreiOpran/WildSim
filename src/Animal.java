public abstract class Animal extends Organism {
	protected int movementSpeed;
	protected int visionRange;

	public Animal(Position position, int energy, char symbol, int movementSpeed, int visionRange) {
		super(position, energy, symbol);
		this.movementSpeed = movementSpeed;
		this.visionRange = visionRange;
	}

	public void move(Ecosystem ecosystem) {
		int dx = (int) (Math.random() * (2 * movementSpeed + 1)) - movementSpeed;
		int dy = (int) (Math.random() * (2 * movementSpeed + 1)) - movementSpeed;

		Position newPosition = new Position(getPosition().getX() + dx, getPosition().getY() + dy);

		if (ecosystem.isValidPosition(newPosition)) setPosition(newPosition);

		energy -= 1;
		checkDeath();
	}

	protected abstract Position findClosestFood(Ecosystem ecosystem);
	protected abstract void eat(Organism food);
}
