package com.github.eraices.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.github.eraices.core.GameStateManager.State;
import com.github.eraices.entities.Player;

public class GamePanel extends JPanel implements Runnable {
	private final int FPS = 60;
	private final double DRAW_INTERVAL = 1000000000.0 / FPS;
	
	public GameStateManager gsm = new GameStateManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public Player player = new Player(0, 0, 5, this);
	public UI ui = new UI(this);

    private Thread gameThread;
	private int tileSize = 16;
	
	public GamePanel(int tileSize) {
		this.tileSize = tileSize;

		// Set panel settings
		this.setPreferredSize(new Dimension(GameEngine.VIRTUAL_SCREEN_WIDTH, GameEngine.VIRTUAL_SCREEN_HEIGHT));
		this.setDoubleBuffered(true); // Basically improves performance
		this.setBackground(Color.BLACK);

		// Add KeyHandler
		this.addKeyListener(keyH);
		this.setFocusable(true);

		// Set starting game state
		gsm.setCurrGameState(State.PLAY);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		int frameNum = 1;
		
		// This loop will keep running as long as this thread exists
		while(gameThread != null) {
			// Calculate time between last loop and this one
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / DRAW_INTERVAL;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			// New frame every draw interval
			if(delta >= 1) {
				update();
				repaint();
				
				delta = 0;
				drawCount++;
			}
			
			// Print FPS to console every second
			if(timer >= 1000000000) {
				System.out.println("FPS (" + frameNum + "): " + drawCount);
				drawCount = 0;
				timer = 0;
				frameNum++;
			}
		}
	}

	public void update() {
    	// TODO: Implement update stuff
		//System.out.println(keyH);
		gsm.getCurrentGameState().update();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		ui.setGraphics(g2);

		// TODO: Implement drawing stuff
		//testUI(g2);
		gsm.getCurrentGameState().draw(g2);

		g.dispose(); // Clear up graphics resources efficiently
    }

	public int getTileSize() {
		return tileSize;
	}

	private void testUI(Graphics2D g2) {
		ui.setGraphics(g2);

		// Calculate box dimensions and coordinates
		int boxWidth = 400;
		int boxHeight = 300;
		int boxX = ui.getXForCenteredBox(boxWidth);
		int boxY = ui.getYForCenteredBox(boxHeight);

		// Calculate text coordinates
		String text = "Sample text";
		int textX = ui.getXForCenteredTextInBox(text, boxX, boxWidth);
		int textY = ui.getYForCenteredTextInBox(text, boxY, boxHeight);

		// Draw box, then text
		ui.drawBox(boxX, boxY, boxWidth, boxHeight, false, false);
		ui.drawString(text, textX, textY);
	}
}