package com.github.eraices.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.eraices.core.GameEngine;
import com.github.eraices.core.GamePanel;
import com.github.eraices.core.Icon;
import com.github.eraices.items.*;

public class Player extends Entity {
    private static final int NUM_HOTBAR_SLOTS = 9;
    private static final int FOOD_PARTICLE_FREQUENCY = 5;
    private static final double DIGESTION_THRESHOLD = 10;
    private static final double PASSIVE_HEALING_THRESHOLD = 10;

    private InventorySlot[] hotbar = new InventorySlot[NUM_HOTBAR_SLOTS];
    private int hotbarSelection = 1;
    private int maxHunger = 20;
    private int currentHunger = 10;
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

        for(int i = 0; i < NUM_HOTBAR_SLOTS; i++) {
            hotbar[i] = new InventorySlot(null, 0);
        }
        hotbar[4].add(new Consumable("Bread", Icon.BREAD, 64, 6));
        hotbar[4].add(new Consumable("Bread", Icon.BREAD, 64, 6));
        hotbar[4].add(new Consumable("Bread", Icon.BREAD, 64, 6));
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

    public InventorySlot getHotbarSlot(int slot) {
        return hotbar[slot];
    }

    public Item getHeldItem() {
        return hotbar[hotbarSelection].getItem();
    }

    public int getItemX() {
        return switch(direction) {
            case Direction.UP -> getScreenX() + (8 * gp.scale);
            case Direction.DOWN -> getScreenX();
            case Direction.LEFT -> 
                switch(spriteNum) {
                    case 1 -> getScreenX();
                    case 3 -> getScreenX() + (8 * gp.scale);
                    default -> getScreenX() + (4 * gp.scale);
                };
            case Direction.RIGHT -> 
                switch(spriteNum) {
                    case 1 -> getScreenX() + (8 * gp.scale);
                    case 3 -> getScreenX();
                    default -> getScreenX() + (4 * gp.scale);
                };
            default -> getScreenX();
        };
    }

    public int getItemY() {
        return switch(spriteNum) {
            case 1, 3 -> getScreenY() + (6 * gp.scale);
            default -> getScreenY() + (7 * gp.scale);
        };
    }

    public int getCurrentHunger() {
        return currentHunger;
    }

    public int getMaxHunger() {
        return maxHunger;
    }

    public boolean isFull() {
        return currentHunger == maxHunger;
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

    @Override
    public int getScreenX() {
        return (GameEngine.VIRTUAL_SCREEN_WIDTH / 2) - (gp.tileSize / 2);
    }

    @Override
    public int getScreenY() {
        return (GameEngine.VIRTUAL_SCREEN_HEIGHT / 2) - (gp.tileSize / 2);
    }

    @Override
    public void update() {
        if(isMoving) {
            move();
        }
        if(isEating) {
            eat();
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

        // If holding an item, draw the item too
        if(hotbar[hotbarSelection].getItem() != null) {
            BufferedImage item = gp.iManager.getIcon(hotbar[hotbarSelection].getItem().getIconID());
            int itemX = getItemX();
            int itemY = getItemY();

            // If player is looking up, draw item then player,
            // else draw player then item
            if(isFacingUp()) {
                g2.drawImage(item, itemX, itemY, null);
                g2.drawImage(sprite, screenX, screenY, null);
            } else {
                g2.drawImage(sprite, screenX, screenY, null);
                g2.drawImage(item, itemX, itemY, null);
            }
        } else { // No item, so just draw player
            g2.drawImage(sprite, screenX, screenY, null);
        }
    }

    private void checkPassiveHealing() {
        // Accumulate passive healing if alive, not at full health, and more than 9 hunger
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

    private void consume(Consumable item) {
        int newHunger = currentHunger + item.getHunger();

        currentHunger = Math.min(newHunger, maxHunger);
    }

    private void eat() {
        Item item = getHeldItem();
        BufferedImage icon = gp.iManager.getIcon(item.getIconID());

        eatCounter++;

        // Every few frames, spawn a food particle
        if(eatCounter % FOOD_PARTICLE_FREQUENCY == 0) {
            int particleSpeed = (int)(Math.random() * 3) - 1;
            new Particle(gp, getItemX(), getItemY(), particleSpeed, icon);
        }

        // Consume the item after eating
        if(eatCounter >= EAT_TIME) {
            consume((Consumable)item);
            hotbar[hotbarSelection].remove();

            setIsEating(false);
        }
    }
}
