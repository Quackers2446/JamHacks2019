package main;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import game.Player;
import kinect.DepthMapDataUpdater;
import kinect.Kinect;
import processing.core.PApplet;

public class SandboxKinectApp extends PApplet {

	private Kinect kinect;
	private DepthMapDataUpdater depthMapUpdater;
	Player mainPlayer;
	Player secondaryPlayer;

	public static void main(String[] args) {
		PApplet.main("main.SandboxKinectApp");
	}

	public void settings() {
		fullScreen(2);
//		size(512, 424);
	}

	public void setup() {
		kinect = new Kinect();
		kinect.start(J4KSDK.DEPTH | J4KSDK.XYZ);
		depthMapUpdater = new DepthMapDataUpdater(kinect);
		depthMapUpdater.start();
		mainPlayer = new Player(this, depthMapUpdater, 100, 100, "Donny", 1);
		secondaryPlayer = new Player(this, depthMapUpdater, 200, 200, "Kegan", 2);
//		secondaryPlayer.setAngle(3924823);
		background(0);
	}

	public void draw() {
		pushMatrix();
		translate(115, 10);
		updateAndDisplayDepthMap();
		mainPlayer.move();
		mainPlayer.update();
		mainPlayer.display();
		secondaryPlayer.move();
		secondaryPlayer.update();
		secondaryPlayer.display();
		popMatrix();
		System.out.println("===================");
//		simpleMapDisplay();
	}

	private void updateAndDisplayDepthMap() {
		if (depthMapUpdater.hasNewMap()) {
			loadPixels();
			int[][] image = depthMapUpdater.getImage();
			for (int i = 0; i < image.length; i++) {
				int index = 115 + width * (i + 10);
				for (int j = 0; j < image[i].length; j++) {
					pixels[index] = image[image.length - i - 1][j];
					index++;
				}
			}
			depthMapUpdater.resetHasNewMap();
			updatePixels();
		}
		noFill();
		strokeWeight(2);
		stroke(0, 0, 255);
		rect(0, 0, 576, 576);
	}

	@SuppressWarnings("unused")
	private void simpleMapDisplay() {
		background(255);
		if (kinect.isDepthMapLoaded()) {
			loadPixels();
			for (int y = 0; y < 424; y++) {
				int index = y * 512;
				for (int x = 0; x < 512; x++) {
//					System.out.println(map(kinect.getDepthMap()[y][x] - 0.4f, 0, 0.2f, 0, 255));
					pixels[index] = color((int) map(kinect.getDepthMap()[y][x], 0, 1f, 0, 255));
//					System.out.println(kinect.getDepthMap()[y][x]);
					index++;
				}
			}
			updatePixels();
		}
	}

}
