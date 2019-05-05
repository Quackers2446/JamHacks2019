package main;

import java.util.ArrayList;
import java.util.Scanner;

import game.Player;

public class GameDataHandlerThread extends Thread {

	private ArrayList<Player> players = new ArrayList<>();

	private Scanner scanner;

	public GameDataHandlerThread() {
		scanner = new Scanner(System.in);
	}

	public void run() {
		while (true) {
			String line = scanner.nextLine();
			if (line != null) {
				parseLine(line);
			}
		}
	}

	private void parseLine(String line) {
		if() {
			addNewPlayer();
		}
	}

	private void addNewPlayer() {

	}

}
