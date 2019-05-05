package game;

import processing.core.PApplet;

public abstract class Entity {

	private Point location;
	private float angle = 0;
	private PApplet applet;
	@SuppressWarnings("unused")
	private int id;

	public Entity(PApplet pApplet, Point location, int playerID) {
		applet = pApplet;
		this.id = playerID;
		this.location = location;
	}

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

	public PApplet getApplet() {
		return applet;
	}
}
