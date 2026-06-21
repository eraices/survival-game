package com.github.eraices.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.eraices.core.GameEngine;
import com.github.eraices.core.GamePanel;

public class Player extends Entity {
    private int hotbarSelection = 1;

    public Player(GamePanel gp, int worldX, int worldY, int speed) {
        super(gp, worldX, worldY, speed);
        width = gp.tileSize;
        height = gp.tileSize;
        initHurtbox(width, height);
        setHurtboxLocationToSelf();
        frameLength = 6;
        maxHealth = 20;
        currentHealth = 15;
        setSpriteSheet("/sprites/Player", gp.ogTileSize, gp.ogTileSize, 4, 4);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getHotbarSelection() {
        return hotbarSelection;
    }

    public void setHotbarSelection(int hotbarSelection) {
        this.hotbarSelection = hotbarSelection;
    }

    public int getScreenX() {
        return (GameEngine.VIRTUAL_SCREEN_WIDTH / 2) - (gp.tileSize / 2);
    }

    public int getScreenY() {
        return (GameEngine.VIRTUAL_SCREEN_HEIGHT / 2) - (gp.tileSize / 2);
    }

    @Override
    public void update() {
        if(isMoving) {
            move();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Player is always at center screen
        int screenX = (GameEngine.VIRTUAL_SCREEN_WIDTH / 2) - (gp.tileSize / 2);
        int screenY = (GameEngine.VIRTUAL_SCREEN_HEIGHT / 2) - (gp.tileSize / 2);

        g2.drawImage(sprite, screenX, screenY, null);
    }
}
