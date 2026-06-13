package com.github.eraices.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    
	private final int FPS = 60;
	private final double DRAW_INTERVAL = 1000000000.0 / FPS;

    private Thread gameThread;
	private KeyHandler keyH = new KeyHandler();
	private UI ui = new UI();
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(GameEngine.VIRTUAL_SCREEN_WIDTH, GameEngine.VIRTUAL_SCREEN_HEIGHT));
		this.setDoubleBuffered(true); // Basically improves performance
		this.setBackground(Color.BLACK);

		// Add KeyHandler
		this.addKeyListener(keyH);
		this.setFocusable(true);
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
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / DRAW_INTERVAL;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				
				delta = 0;
				drawCount++;
			}
			
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
		System.out.println(keyH);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		// TODO: Implement drawing stuff
		ui.setGraphics(g2);
		ui.drawBox(ui.getXForCenteredBox(100), ui.getYForCenteredBox(100), 100, 100, false, false);

		g.dispose(); // Clear up graphics resources efficiently
    }
}