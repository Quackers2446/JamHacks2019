package game;

import kinect.DepthMapDataUpdater;
import processing.core.PApplet;
import processing.core.PConstants;

public class Player extends Entity implements Movable {

	private static final float PLAYER_SPEED = 10;
	private static float nextSpeed = PLAYER_SPEED;
	private String name;
	private DepthMapDataUpdater depthMapUpdater;

	public Player(PApplet pApplet, DepthMapDataUpdater depthMapUpdater, float xPos, float yPos, String name, int id) {
		super(pApplet, new Point(xPos, yPos, 0), id);
		this.name = name;
		this.depthMapUpdater = depthMapUpdater;
	}

	@Override
	public void move() {
		setLocation(Point.getPointByAngleAndOffset2D(getLocation(), getAngle(), nextSpeed));
		setAngle(getAngle() - 0.005f);
		nextSpeed = PLAYER_SPEED;
		float height = depthMapUpdater.getHeight(getLocation());
		if (height <= DepthMapDataUpdater.SEA_LEVEL) {
			nextSpeed /= 10;
		}
		System.out.println(height + " " + DepthMapDataUpdater.SEA_LEVEL + " " + nextSpeed);
	}

	@Override
	public void update() {

	}

	@Override
	public void display() {
		PApplet a = getApplet();
		a.pushMatrix();
		a.translate(getLocation().getX(), getLocation().getY());
		a.rotate(-getAngle() - PConstants.PI / 2);
		a.fill(50);
		a.textSize(20);
		a.textAlign(PConstants.CENTER);
		a.text(name, 0, -10);
		a.triangle(0, 10, 5, -5, -5, -5);
//		System.out.println(getLocation().getX() + " " + getLocation().getY());
		a.popMatrix();
	}

}
