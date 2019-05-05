package networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import main.GameDataHandlerThread;

public class SandboxServer extends Thread {
	private ServerSocket socket;
	private Scanner socketIn;
	private PrintWriter socketOut;
	private ArrayList<String> queue = new ArrayList<>(); 

	public SandboxServer(int port) {
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
		}
	}

	public void run() {

		for (;;) {
			try {
				Socket client = socket.accept();
				Scanner in = new Scanner(client.getInputStream());
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				while(client.getInputStream().available() > 0) {
					String data = in.nextLine();
					if(data.startsWith("Name:")) {
						String name = data.substring(5);
						int index = GameDataHandlerThread.requestIndex(name);
						if (index >= 0) {
//							GameDataHandlerThread.addNewPlayer()
						}
						out.println(index);
						
					} else {
						//Feed to Data Handler
					}
				}
			} catch (IOException e) {
			}
		}
	}

	public String[] getData() {
		return queue.toArray(new String[queue.size()]);
	}
}
