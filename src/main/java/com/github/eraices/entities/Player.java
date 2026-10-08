package com.github.eraices.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.SpinnerDateModel;

import com.github.eraices.core.GameEngine;
import com.github.eraices.core.GamePanel;
import com.github.eraices.core.Icon;
import com.github.eraices.items.*;

public class Player extends Entity {
    private static final int MAX_HEALTH = 20;
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final int SPEED = 2;
    private static final int NUM_HOTBAR_SLOTS = 9;
    private static final int FOOD_PARTICLE_FREQUENCY = 5;
    private static final double DIGESTION_THRESHOLD = 10;
    private static final double PASSIVE_HEALING_THRESHOLD = 10;

    private int hotbarSelection = 0;
    private int maxHunger = 20;
    private int currentHunger = 10;
    private double digestion = 0;
    private double passiveHealing = 0;

    private boolean didDropItem = false;

    public Player(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY);
        initSelf(MAX_HEALTH, WIDTH, HEIGHT, SPEED, NUM_HOTBAR_SLOTS);
        setSpriteSheet("/sprites/Player", WIDTH, HEIGHT, 4, 4);
        
        for(int i = 0; i < 64; i++) {
            hotbar[4].add(new Consumable("Bread", Icon.BREAD, 64, 6));
        }
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

    public boolean didDropItem() {
        return didDropItem;
    }

    public void setDidDropItem(boolean didDropItem) {
        this.didDropItem = didDropItem;
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
        if(isRemoved) return;

        if(isMoving) {
            move();
        }
        if(isEating) {
            eat();
        }
        checkEntityCollision();
        checkPassiveHealing();
        checkInvincibility();
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

    public void collidePlayerAndDroppedItem(DroppedItem droppedItem) {
        // Only try to pick up the item if it's not invincible
        if(!droppedItem.isInvincible) {
            // Try to find a hotbar slot that can hold the item
            for(int i = 0; i < NUM_HOTBAR_SLOTS; i++) {
                // Skip slots that have a different item or are at a full stack
                if((hotbar[i].getItem() != null) && 
                    ((!hotbar[i].getItem().equals(droppedItem.getItem()))
                    || (hotbar[i].isFull()))) {
                    continue;
                }

                /*
                Found a valid slot, check if it can hold the total amount
                 */
                // This slot is empty, so it's fine
                if(hotbar[i].getItem() == null) {
                    hotbar[i] = new InventorySlot(droppedItem.getItem(), droppedItem.getCount());
                    droppedItem.remove();
                    return;
                } 
                // This slot isn't empty, but can hold the total amount
                else if(hotbar[i].hasSpaceFor(droppedItem.getCount())) {
                    int totalCount = hotbar[i].getCount() + droppedItem.getCount();
                    hotbar[i].setCount(totalCount);
                    droppedItem.remove();
                    return;
                }
                // This slot isn't empty, but can only hold some of the amount
                else {
                    // Not implemented, for now, just complain
                    System.out.println("What the heck");
                }
            }
        }
    }

    public void dropItem() {
        Item heldItem = getHeldItem();

        if(heldItem != null) {
            InventorySlot item = new InventorySlot(heldItem, 1);
            new DroppedItem(gp, getCenterX(), getCenterY(), 3, direction, item);
            hotbar[hotbarSelection].remove();
            didDropItem = true;
        }
    }

    @Override
    protected void collideWithDroppedItem(DroppedItem droppedItem) {
        collidePlayerAndDroppedItem(droppedItem);
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
            int particleWorldX = worldX + (getItemX() - getScreenX());
            int particleWorldY = worldY + (getItemY() - getScreenY());

            new Particle(gp, particleWorldX, particleWorldY, icon);
        }

        // Consume the item after eating
        if(eatCounter >= EAT_TIME) {
            consume((Consumable)item);
            hotbar[hotbarSelection].remove();

            setIsEating(false);
        }
    }

    public int getTotalItemCount() {
        int sum = 0;
        for(int i = 0; i < NUM_HOTBAR_SLOTS; i++) {
            if(hotbar[i] != null) {
                sum += hotbar[i].getCount();
            }
        }
        return sum;
    }
}
