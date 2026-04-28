package Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * The JFrame that holds the GamePanel
 * Just does some setup and exposes the gamePanel's screenManager to allow an external class to setup their own content and attach it to this engine.
 */
public class GameWindow {
	private JFrame gameWindow;
	private GamePanel gamePanel;
	private boolean isFullscreen = false;

	public GameWindow() {
		gameWindow = new JFrame("Game");
		gamePanel = new GamePanel();
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		gameWindow.setContentPane(gamePanel);
		gameWindow.setResizable(false);
		gamePanel.setPreferredSize(new Dimension(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT));
		gameWindow.pack();
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // it'd be nice if this actually worked more than 1/3rd of the time
		gamePanel.setupGame();

		// F11 toggles fullscreen
		gamePanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F) {
					toggleFullscreen();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && isFullscreen) {
					toggleFullscreen();
				}
			}
		});
	}

	private void toggleFullscreen() {
		isFullscreen = !isFullscreen;
		if (isFullscreen) {
			gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			gameWindow.setExtendedState(JFrame.NORMAL);
			gameWindow.pack();
			gameWindow.setLocationRelativeTo(null);
		}
		gamePanel.requestFocusInWindow();
	}

	// triggers the game loop to start as defined in the GamePanel class
	public void startGame() {
		gamePanel.startGame();
	}

	public ScreenManager getScreenManager() {
		return gamePanel.getScreenManager();
	}
}
