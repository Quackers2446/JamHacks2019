package game;

import kinect.DepthMapDataUpdater;
import main.SandboxKinectApp;
import processing.core.PApplet;
import processing.core.PConstants;

public class Player extends Entity {

	private static final float PLAYER_SPEED = 10;
	private static final float TURN_SPEED = 0.3f;
	private static float nextSpeed = PLAYER_SPEED;
	private String name;
	private DepthMapDataUpdater depthMapUpdater;
	private boolean[] actionData = new boolean[7];
	private int direction = 1;

	public Player(PApplet pApplet, DepthMapDataUpdater depthMapUpdater, float xPos, float yPos, String name, int id) {
		super(pApplet, new Point(xPos, yPos, 0), id);
		direction = -1 + (int) Math.random() * 3;
		this.name = name;
		this.depthMapUpdater = depthMapUpdater;
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

	public void update(boolean[] actionArray) {
		actionData = actionArray;
		update();
	}

	public void control() {
		if (SandboxKinectApp.wPressed) {
			setLocation(Point.getPointByAngleAndOffset2D(getLocation(), getAngle(), 1.5f));
		}
		if (SandboxKinectApp.aPressed) {
			setLocation(Point.getPointByAngleAndOffset2D(getLocation(), getAngle() + PConstants.PI / 2, 1));
		}
		if (SandboxKinectApp.sPressed) {
			setLocation(Point.getPointByAngleAndOffset2D(getLocation(), getAngle() + PConstants.PI, 0.8f));
		}
		if (SandboxKinectApp.dPressed) {
			setLocation(Point.getPointByAngleAndOffset2D(getLocation(), getAngle() - PConstants.PI / 2, 1));
		}
		if (SandboxKinectApp.qPressed) {
			setAngle(getAngle() + 0.01f);
		}
		if (SandboxKinectApp.ePressed) {
			setAngle(getAngle() - 0.01f);
		}
	}

	public void move() {
		setLocation(Point.getPointByAngleAndOffset2D(getLocation(), getAngle(), 1));
		setAngle(getAngle() + direction * 0.01f);
	}

	@Override
	public void update() {
//		float height = depthMapUpdater.getHeight(getLocation());

		for (int i = 0; i < actionData.length; i++) {
			if (actionData[i]) {
				switch (i) {
				case 0:
					// Water
					System.out.println(name + " tried to water");
					break;
				case 1:
					// Up
					setLocation(Point.getPointByAngleAndOffset2D(getLocation(), getAngle(), nextSpeed));
					break;
				case 2:
					// Down
					setLocation(Point.getPointByAngleAndOffset2D(getLocation(), getAngle() + PConstants.PI, nextSpeed));
					break;
				case 3:
					// Left
					setLocation(
							Point.getPointByAngleAndOffset2D(getLocation(), getAngle() + PConstants.PI / 2, nextSpeed));
					break;
				case 4:
					setLocation(
							Point.getPointByAngleAndOffset2D(getLocation(), getAngle() - PConstants.PI / 2, nextSpeed));
					// Right
					break;
				case 5:
					// Clockwise
					setAngle(getAngle() + TURN_SPEED);
					break;
				case 6:
					// Counterclockwise
					setAngle(getAngle() - TURN_SPEED);
					break;
				}
			}
		}
	}

}
