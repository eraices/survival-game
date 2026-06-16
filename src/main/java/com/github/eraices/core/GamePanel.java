package com.github.eraices.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.eraices.core.GameStateManager.State;
import com.github.eraices.entities.Player;
import com.github.eraices.world.WorldManager;

public class GamePanel extends JPanel implements Runnable {
	private final int FPS = 60;
	private final double DRAW_INTERVAL = 1000000000.0 / FPS;
	
	public int ogTileSize = 16;
	public int scale = 3;
	public int tileSize = ogTileSize * scale;
	public GameStateManager gsm = new GameStateManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public UI ui = new UI(this);
	public Player player = new Player(this, 0, 0, 5);
	public WorldManager world = new WorldManager(this);


    private Thread gameThread;

	// Fullscreen variables
	private boolean isFullscreen = false;
	private int fullscreenWidth;
	private int fullscreenHeight;
	private BufferedImage virtualCanvas;
	private Graphics2D g2Virtual;
	
	public GamePanel() {

		// Set panel settings
		this.setPreferredSize(new Dimension(GameEngine.VIRTUAL_SCREEN_WIDTH, GameEngine.VIRTUAL_SCREEN_HEIGHT));
		this.setDoubleBuffered(true); // Basically improves performance
		this.setBackground(Color.CYAN);

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
		gsm.getCurrentGameState().update();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		ui.setGraphics(g2);

		gsm.getCurrentGameState().draw(g2);

		g.dispose(); // Clear up graphics resources efficiently
    }

	public void initFullscreen() {
		// Get hardware graphics
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		// Get the window GamePanel is in
		JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);

		if(gd.isFullScreenSupported()) {
			window.dispose();
			window.setUndecorated(true);

			gd.setFullScreenWindow(window);

			// Get actual monitor dimension
			fullscreenWidth = window.getWidth();
			fullscreenHeight = window.getHeight();
			isFullscreen = true;

			// Initialize virtualCanvas at our virtual screen size
			// defined in GameEngine
			virtualCanvas = new BufferedImage(GameEngine.VIRTUAL_SCREEN_WIDTH, GameEngine.VIRTUAL_SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			g2Virtual = virtualCanvas.createGraphics();

			window.setVisible(true);
		} else {
			System.err.println("Fullscreen not supported");
		}
	}
}