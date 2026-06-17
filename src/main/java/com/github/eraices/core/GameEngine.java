package com.github.eraices.core;

import javax.swing.JFrame;

public class GameEngine {
	// The game will calculate based on these dimensions,
	// regardless of the actual window dimensions
	public static final int VIRTUAL_SCREEN_WIDTH = 960;
	public static final int VIRTUAL_SCREEN_HEIGHT = 540;

	private JFrame window;

	public GameEngine() {
		initializeWindow();
	}

	public static void main(String[] args)  {
		// Force rendering to ignore OS scaling
		System.setProperty("sun.java2d.uiScale", "1");

		new GameEngine();
	 }

	private void initializeWindow() {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Survival Game");
		window.setResizable(true);

		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		window.pack();

		window.setLocationRelativeTo(null);
		window.setVisible(true);

		gamePanel.toggleFullscreen();
		
		gamePanel.startGameThread();
	}
}