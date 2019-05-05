package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import game.Player;
import kinect.DepthMapDataUpdater;
import processing.core.PApplet;

public class GameDataHandlerThread extends Thread {

	private HashMap<Integer, Player> players = new HashMap<>();
	private PApplet pApplet;
	private DepthMapDataUpdater depthMapUpdater;
	private Scanner scanner;

	public GameDataHandlerThread(PApplet applet, DepthMapDataUpdater depthMapUpdater) {
		scanner = new Scanner(System.in);
		pApplet = applet;
		this.depthMapUpdater = depthMapUpdater;
	}

	public void run() {
		while (true) {
			System.out.println("hi3");
			try {
				while (System.in.available() > 0) {
					System.out.println("hiw");
					parseLine(scanner.nextLine());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			sleep(1);
		}
	}

	private void parseLine(String line) {
		String[] data = line.split(",");
		int id = Integer.parseInt(data[0]);
		int movement = Integer.parseInt(data[1]);
		String name = data[2];
		if (!players.containsKey(id)) {
			addNewPlayer(id, name);
		} else {
			boolean[] actions = new boolean[7];
			for (int i = 0; i < 7; i++)
				actions[i] = (movement & (1 << i)) > 0;
			players.get(id).update(actions);
		}
	}

	private void addNewPlayer(int id, String name) {
		System.out.println("added new player");
		Player player = new Player(pApplet, depthMapUpdater, (int) Math.random() * DepthMapDataUpdater.MAP_PIXEL_SIZE_X,
				(int) Math.random() * DepthMapDataUpdater.MAP_PIXEL_SIZE_Y, name, id);
		players.put(id, player);
	}

	public void displayEntities() {
		for (Player player : players.values()) {
			System.out.println("displaying player");
			player.display();
		}
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println("oh no");
			e.printStackTrace();
		}
	}

}
