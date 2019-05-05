package main;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class KeyHandler {
	public static volatile boolean wPressed = false;
	public static volatile boolean aPressed = false;
	public static volatile boolean sPressed = false;
	public static volatile boolean dPressed = false;
	public static volatile boolean qPressed = false;
	public static volatile boolean ePressed = false;

	public static void main(String[] args) {
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
}