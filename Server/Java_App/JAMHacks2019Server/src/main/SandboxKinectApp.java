package main;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import game.Player;
import kinect.DepthMapDataUpdater;
import kinect.Kinect;
import networking.SandboxServer;
import processing.core.PApplet;

public class SandboxKinectApp extends PApplet {

	public static volatile boolean wPressed = false;
	public static volatile boolean aPressed = false;
	public static volatile boolean sPressed = false;
	public static volatile boolean dPressed = false;
	public static volatile boolean qPressed = false;
	public static volatile boolean ePressed = false;

	private Kinect kinect;
	private DepthMapDataUpdater depthMapUpdater;
	private GameDataHandlerThread gameDataHandler;
	private SandboxServer sandboxServer;
	Player mainPlayer = new Player(this, depthMapUpdater, 150, 20, "Donny", 1);
	Player secondPlayer = new Player(this, depthMapUpdater, 280, 90, "Matthew", 1);

	public static void main(String[] args) {
		PApplet.main("main.SandboxKinectApp");
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent ke) {
				synchronized (KeyHandler.class) {
					switch (ke.getID()) {
					case KeyEvent.KEY_PRESSED:
						if (ke.getKeyCode() == KeyEvent.VK_W) {
							System.out.println("pressed");
							wPressed = true;
						} else if (ke.getKeyCode() == KeyEvent.VK_A) {
							aPressed = true;
						} else if (ke.getKeyCode() == KeyEvent.VK_S) {
							sPressed = true;
						} else if (ke.getKeyCode() == KeyEvent.VK_D) {
							dPressed = true;
						} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
							dPressed = true;
						} else if (ke.getKeyCode() == KeyEvent.VK_E) {
							dPressed = true;
						}
						break;

					case KeyEvent.KEY_RELEASED:
						if (ke.getKeyCode() == KeyEvent.VK_W) {
							wPressed = false;
						} else if (ke.getKeyCode() == KeyEvent.VK_A) {
							aPressed = false;
						} else if (ke.getKeyCode() == KeyEvent.VK_S) {
							sPressed = false;
						} else if (ke.getKeyCode() == KeyEvent.VK_D) {
							dPressed = false;
						} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
							dPressed = false;
						} else if (ke.getKeyCode() == KeyEvent.VK_E) {
							dPressed = false;
						}
						break;
					}
					return false;
				}
			}
		});
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
		gameDataHandler = new GameDataHandlerThread(this, depthMapUpdater);
		gameDataHandler.start();
		sandboxServer = new SandboxServer(8000);
		sandboxServer.start();
		background(0);
	}

	public void draw() {
		pushMatrix();
		translate(115, 10);
		updateAndDisplayDepthMap();
		gameDataHandler.parseData(sandboxServer.getData());
		gameDataHandler.displayEntities();
		mainPlayer.control();
		mainPlayer.display();
		secondPlayer.move();
		secondPlayer.display();
		popMatrix();
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
