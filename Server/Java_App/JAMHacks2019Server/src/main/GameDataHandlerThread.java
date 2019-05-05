package main;

import java.util.ArrayList;
import java.util.HashMap;

import game.Player;
import kinect.DepthMapDataUpdater;
import processing.core.PApplet;

public class GameDataHandlerThread extends Thread {

	private HashMap<Integer, Player> players = new HashMap<>();
	private PApplet pApplet;
	private DepthMapDataUpdater depthMapUpdater;
	private ArrayList<String> actionData = new ArrayList<>();

	public GameDataHandlerThread(PApplet applet, DepthMapDataUpdater depthMapUpdater) {
		pApplet = applet;
		this.depthMapUpdater = depthMapUpdater;
	}

	public void run() {
		while (true) {
			if (actionData.isEmpty()) {
				sleep(10);
			} else {
				parseLine(actionData.remove(0));
			}
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

	public static int requestIndex(String name) {
		// Check through players to see if there is available current ID, and for
		// duplicate names
		return 0;
	}

	public void parseData(String[] data) {
		for (String action : data) {
			actionData.add(action);
		}
	}

}
