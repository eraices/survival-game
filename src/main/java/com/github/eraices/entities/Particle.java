package com.github.eraices.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.eraices.core.GamePanel;
import com.github.eraices.core.RNG;

public class Particle extends Entity {
    private static final Color DEFAULT_COLOR = new Color(16, 20, 31); // Black
    private static final int INC_GRAVITY = 2;
    private static final int MIN_HEALTH = GamePanel.FPS;
    private static final int MAX_HEALTH = GamePanel.FPS * 2;

    private Color color;
    private int gravity = 0;
    private int gravityCounter = 0;

    public Particle(GamePanel gp, int worldX, int worldY, BufferedImage icon) {
        super(gp, worldX, worldY);
        speed = RNG.randomInt(-1, 1);
        currentHealth = RNG.randomInt(MIN_HEALTH, MAX_HEALTH);
        color = pickColorFrom(icon);
    }

    public Color pickColorFrom(BufferedImage icon) {
        if(icon == null) return DEFAULT_COLOR;

        // Try 20 times to get a valid pixel. If we can't, return default color
        for(int i = 0; i < 20; i++) {
            int px = (int)(Math.random() * icon.getWidth());
            int py = (int)(Math.random() * icon.getHeight());

            // Get RGB and (unsigned) bit shift so only alpha is left
            int argb = icon.getRGB(px, py);
            int alpha = argb >>> 24;

            // If not transparent, great! Otherwise, try again
            if(alpha > 0) {
                // Particle will spawn from the pixel we found
                worldX += px;
                worldY += py;

                return new Color(argb, true);
            }
        }

        // Couldn't find a valid pixel; just use default color
        return DEFAULT_COLOR;
    }
    
    @Override
    public void update() {
        move();

        gravityCounter++;
        if(gravityCounter >= INC_GRAVITY) {
            gravity++;
            gravityCounter = 0;
        }

        currentHealth--;
        if(currentHealth <= 0) {
            die();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(getScreenX(), getScreenY(), gp.scale, gp.scale);
    }

    @Override
    public void move() {
        worldX += speed;
        worldY += speed + gravity;

        updateCurrentChunk();
    }
}