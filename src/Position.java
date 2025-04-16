import java.util.Objects;

public class Position {
	private int x;
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}

	public double distanceTo(Position other) {
		int dx = this.x - other.x;
		int dy = this.y - other.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static Position randomPosition(int width, int height) {
		int x = (int) (Math.random() * width);
		int y = (int) (Math.random() * height);
		return new Position(x, y);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return x == position.x && y == position.y;
	}
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
