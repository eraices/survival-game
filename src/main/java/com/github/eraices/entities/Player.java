package com.github.eraices.entities;

import java.awt.Graphics2D;

import com.github.eraices.core.GameEngine;
import com.github.eraices.core.GamePanel;

public class Player extends Entity {
    private static final double DIGESTION_THRESHOLD = 10;
    private static final double PASSIVE_HEALING_THRESHOLD = 10;

    private int hotbarSelection = 1;
    private int maxHunger = 20;
    private int currentHunger = 20;
    private double digestion = 0;
    private double passiveHealing = 0;

    public Player(GamePanel gp, int worldX, int worldY, int speed) {
        super(gp, worldX, worldY, speed);
        width = gp.tileSize;
        height = gp.tileSize;
        initHurtbox(width, height);
        setHurtboxLocationToSelf();
        frameLength = 6;
        maxHealth = 20;
        currentHealth = 1;
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

    public int getCurrentHunger() {
        return currentHunger;
    }

    public int getMaxHunger() {
        return maxHunger;
    }

    public void incDigestion(double amount) {
        digestion += amount;

        // Once the threshold is reached, decrease hunger.
        // If hunger is already at 0, then take damage instead
        if(digestion >= DIGESTION_THRESHOLD) { // Decrease hunger by 1
            if(currentHunger > 0) {
                currentHunger--;
            } else {
                takeDamage(1);
            }
            digestion -= DIGESTION_THRESHOLD;
        }
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
        checkPassiveHealing();
    }

    @Override
    public void move() {
        super.move();
        if(isSprinting) {
            incDigestion(0.01);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Player is always at center screen
        int screenX = (GameEngine.VIRTUAL_SCREEN_WIDTH / 2) - (gp.tileSize / 2);
        int screenY = (GameEngine.VIRTUAL_SCREEN_HEIGHT / 2) - (gp.tileSize / 2);

        g2.drawImage(sprite, screenX, screenY, null);
    }

    private void checkPassiveHealing() {
        // Accumulating passive healing if alive, not at full health, and more than 9 hunger
        if((currentHealth > 0) && (currentHealth < maxHealth) && (currentHunger >= maxHunger - 2)) {

            passiveHealing += 0.25;

            // After enough time has passed, heal 1 HP.
            // This also drains hunger
            if(passiveHealing >= PASSIVE_HEALING_THRESHOLD) {
                heal(1);
                passiveHealing -= PASSIVE_HEALING_THRESHOLD;

                incDigestion(2);
            }
        } else { // Passive healing ends, so reset it
            passiveHealing = 0;
        }
    }
}
