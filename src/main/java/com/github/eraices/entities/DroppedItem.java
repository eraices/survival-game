package com.github.eraices.entities;

import com.github.eraices.core.GamePanel;

public class DroppedItem extends Entity {
    private static final int SLOW_DOWN_RATE = 5;

    private InventorySlot item;
    private int speedX;
    private int speedY;
    private int slowCounter = 0;

    public DroppedItem(GamePanel gp, int worldX, int worldY, int speedX, int speedY, InventorySlot item) {
        super(gp, worldX, worldY);
        this.item = item;
        this.speedX = speedX;
        this.speedY = speedY;
        sprite = gp.iManager.getIcon(item.getItem().getIconID());
    }

    public DroppedItem(GamePanel gp, int worldX, int worldY, int speed, Direction direction, InventorySlot item) {
        super(gp, worldX, worldY);
        this.item = item;
        setSpeed(speed, direction);
        sprite = gp.iManager.getIcon(item.getItem().getIconID());
    }

    @Override
    public void update() {
        if((speedX == 0) && (speedY == 0)) {
            return;
        }

        move();

        slowCounter++;

        // After a while slow down
        if(slowCounter >= SLOW_DOWN_RATE) {
            slowDown();
            slowCounter =  0;
        }
    }
    
    @Override
    public void move() {
        worldX += speedX;
        worldY += speedY;

        updateCurrentChunk();
    }

    private void setSpeed(int speed, Direction direction) {
        switch(direction) {
            case UP -> speedY = speed * -1;
            case DOWN -> speedY = speed;
            case LEFT -> speedX = speed * -1;
            case RIGHT -> speedX = speed;
            default -> speedX = speed;
        }
    }

    private void slowDown() {
        // Slow horizontal movement
        if(speedX < 0) {
            speedX++;
        } else if(speedX > 0) {
            speedX--;
        }

        // Slow vertical movement
        if(speedY < 0) {
            speedY++;
        } else if(speedY > 0) {
            speedY--;
        }
    }
}
