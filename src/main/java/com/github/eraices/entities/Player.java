package com.github.eraices.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.eraices.core.GameEngine;
import com.github.eraices.core.GamePanel;

public class Player extends Entity {
    public Player(int worldX, int worldY, int speed, GamePanel gp) {
        super(worldX, worldY, speed, gp);
    }

    @Override
    public void update() {
        if(isMoving) {
            move();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int tileSize = gp.getTileSize();

        // Player is always at center screen
        int screenX = (GameEngine.VIRTUAL_SCREEN_WIDTH / 2) - (tileSize / 2);
        int screenY = (GameEngine.VIRTUAL_SCREEN_HEIGHT / 2) - (tileSize / 2);

        g2.setColor(Color.WHITE);
        g2.fillOval(screenX, screenY, tileSize, tileSize);
    }
}
