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
	public Player player = new Player(this, 0, 0, 4);
	public WorldManager world = new WorldManager(this);
	public CollisionChecker cChecker = new CollisionChecker(this);


    private Thread gameThread;

	// Fullscreen variables
	private boolean isFullscreen = false;
	private int fullscreenWidth;
	private int fullscreenHeight;
	private BufferedImage virtualCanvas;
	private Graphics2D g2Virtual;

	// Letterboxing variables
	private int aspectRatioWidth;
	private int aspectRatioHeight;
	private int offsetX;
	private int offsetY;
	
	public GamePanel() {

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
		gsm.getCurrentGameState().update();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if(isFullscreen && g2Virtual != null) {
			// Clear off-screen canvas
			g2Virtual.clearRect(0, 0, GameEngine.VIRTUAL_SCREEN_WIDTH, GameEngine.VIRTUAL_SCREEN_HEIGHT);

			// Use g2Virtual instead of g2 for fullscreen
			ui.setGraphics(g2Virtual);
			gsm.getCurrentGameState().draw(g2Virtual);

			// Blit to hardware screen
        g2.drawImage(virtualCanvas, offsetX, offsetY, aspectRatioWidth, aspectRatioHeight, null);
		} else {
			// Use g2 instead of g2virtual for windowed
			ui.setGraphics(g2);
			gsm.getCurrentGameState().draw(g2);
		}

		g2.dispose(); // Clear up graphics resources efficiently
    }

	public void toggleFullscreen() {
		// Get hardware graphics
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		// Get the window GamePanel is in
		JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);

		isFullscreen = !isFullscreen;

		if(isFullscreen) {						// Enter fullscreen
			if(gd.isFullScreenSupported()) {
				window.dispose();
				window.setUndecorated(true);

				gd.setFullScreenWindow(window);

				// Get actual monitor dimension
				fullscreenWidth = window.getWidth();
				fullscreenHeight = window.getHeight();
				isFullscreen = true;

				// Calculate the ratio of monitor's dimensions to our aspect ratio's dimensions
				double scaleX = (double) fullscreenWidth / GameEngine.VIRTUAL_SCREEN_WIDTH;
				double scaleY = (double) fullscreenHeight / GameEngine.VIRTUAL_SCREEN_HEIGHT;

				// Use the smaller scale to ensure the game screen fits on the monitor
				double scale = Math.min(scaleX, scaleY);

				// Set the proper aspect ratio, so everything fits without being stretched
				aspectRatioWidth = (int) (GameEngine.VIRTUAL_SCREEN_WIDTH * scale);
				aspectRatioHeight = (int) (GameEngine.VIRTUAL_SCREEN_HEIGHT * scale);

				// Calculate offsets so the game screen is properly centered
				offsetX = (fullscreenWidth - aspectRatioWidth) / 2;
				offsetY = (fullscreenHeight - aspectRatioHeight) / 2;

				// Initialize virtualCanvas at our virtual screen size
				// defined in GameEngine
				virtualCanvas = new BufferedImage(GameEngine.VIRTUAL_SCREEN_WIDTH, GameEngine.VIRTUAL_SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				g2Virtual = virtualCanvas.createGraphics();

				window.setVisible(true);
			} else {
				System.err.println("Fullscreen not supported");
			}
		} else { // Exit fullscreen
			window.dispose();
			window.setUndecorated(false);

			gd.setFullScreenWindow(null);

			// Reset to virtual screen size defined in GameEngine
			this.setPreferredSize(new Dimension(GameEngine.VIRTUAL_SCREEN_WIDTH, GameEngine.VIRTUAL_SCREEN_HEIGHT));

			window.pack();
			window.setLocationRelativeTo(null);
			window.setVisible(true);

			// Clean up virtual graphics since we're not using them anymore
			if(g2Virtual != null) {
				g2Virtual.dispose();
				virtualCanvas = null;
			}
		}

		
	}
}