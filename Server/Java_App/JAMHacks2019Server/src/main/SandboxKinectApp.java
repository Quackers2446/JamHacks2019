package main;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import kinect.Kinect;
import processing.core.PApplet;

public class SandboxKinectApp extends PApplet {

	private Kinect kinect;
	// startX and startY will be the top-left corner of the
	private int startX, startY;
	private float[][] depthMap;

	public static void main(String[] args) {
		PApplet.main("main.SandboxKinectApp");
	}

	public void settings() {
		fullScreen();
	}

	public void setup() {
		// canvas size will be 1024 * 848
		startX = (width - 1024) / 2;
		startY = (height - 848) / 2;
		kinect = new Kinect();
		kinect.start(J4KSDK.DEPTH | J4KSDK.XYZ);
	}

	public void draw() {
		background(0);
		depthMap = kinect.getDepthMap();
		displayDepthMap(depthMap);
	}

	private void displayDepthMap(float[][] map) {
		int rectX = startX;
		int rectY = startY;
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				int colour = color(map(map[y][x], 0, 1, 0, 255));
				fill(colour);
				rect(rectX, rectY, 2, 2);
				rectX += 2;
			}
			rectY += 2;
			rectX = startX;
		}
	}

}
