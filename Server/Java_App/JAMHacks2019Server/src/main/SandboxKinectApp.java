package main;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import kinect.DepthMapDataUpdater;
import kinect.Kinect;
import processing.core.PApplet;
import processing.core.PImage;

public class SandboxKinectApp extends PApplet {

	private Kinect kinect;
	// startX and startY will be the top-left corner of the
	private int startX, startY;
	private DepthMapDataUpdater depthMapUpdater;
	private PImage depthImage;
	private boolean imageLoaded = false;

	public static void main(String[] args) {
		PApplet.main("main.SandboxKinectApp");
	}

	public void settings() {
//		fullScreen();
		size(512, 424);
	}

	public void setup() {
		// canvas size will be 1024 * 848
		startX = (width - 1024) / 2;
		startY = (height - 848) / 2;
		kinect = new Kinect();
		kinect.start(J4KSDK.DEPTH | J4KSDK.XYZ);
//		depthMapUpdater = new DepthMapDataUpdater(kinect);
//		depthMapUpdater.start();
	}

	public void draw() {
//		background(0);
//		updateAndDisplayDepthMap();

		background(255);
		if (kinect.isDepthMapLoaded()) {
			loadPixels();
			for (int y = 0; y < 424; y++) {
				int index = y * 512;
				for (int x = 0; x < 512; x++) {
//					float height = 1000 - map(kinect.getDepthMap()[y][x], 0, 1f, 0, 1000);
//					int colourRange = (int) (height / 200);
//					float rangeHeight = height % 200;
//					int r, g, b;
//					switch (colourRange) {
//					case 0:
//
//						break;
//					}
					pixels[index] = color((int) map(kinect.getDepthMap()[y][x] - 0.4f, 0, 0.2f, 0, 255));
//					System.out.println(pixels[index]);
					index++;
				}
			}
			updatePixels();
		}
	}

	private void updateAndDisplayDepthMap() {
		loadPixels();
		if (depthMapUpdater.hasNewMap()) {

			int[][] image = depthMapUpdater.getImage();
			for (int i = 0; i < image.length; i++) {
				int index = width * i;
				for (int j = 0; j < image[i].length; j++) {
					pixels[index] = image[i][j];
				}
			}
//			System.out.println();
//			depthImage = loadImage("res/depthImage.png");
//			imageLoaded = true;
			depthMapUpdater.resetHasNewMap();
		}
//		if (imageLoaded = true) {
//			System.out.println("im an idiot");
//			image(depthImage, 0, 0);
//		}
		updatePixels();
	}

}
