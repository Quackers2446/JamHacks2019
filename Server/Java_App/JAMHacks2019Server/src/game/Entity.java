package game;

public abstract class Entity {

	private Point location;
	private float angle;

	public abstract void update();

	public abstract void display();

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

}
