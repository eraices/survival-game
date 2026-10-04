package com.github.eraices.entities;

import com.github.eraices.core.GamePanel;
import com.github.eraices.items.Item;

public class DroppedItem extends Entity {
    private static final int SLOW_DOWN_RATE = 10;

    private InventorySlot item;
    private int speedX;
    private int speedY;
    private int slowCounter = 0;

    public DroppedItem(GamePanel gp, int worldX, int worldY, int speedX, int speedY, InventorySlot item) {
        super(gp, worldX, worldY);
        this.item = item;
        this.speedX = speedX;
        this.speedY = speedY;
        isInvincible = true;
        sprite = gp.iManager.getIcon(item.getItem().getIconID());
        width = sprite.getWidth();
        height = sprite.getHeight();
        initHurtbox(width, height);
    }

    public DroppedItem(GamePanel gp, int worldX, int worldY, int speed, Direction direction, InventorySlot item) {
        super(gp, worldX, worldY);
        this.item = item;
        setSpeed(speed, direction);
        isInvincible = true;
        sprite = gp.iManager.getIcon(item.getItem().getIconID());
        width = sprite.getWidth();
        height = sprite.getHeight();
        initHurtbox(width, height);
    }

    public Item getItem() {
        return item.getItem();
    }

    public int getCount() {
        return item.getCount();
    }

    public void setCount(int count) {
        item.setCount(count);
    }

    @Override
    public void update() {
        if(isRemoved) return;

        if((speedX != 0) || (speedY != 0)) {
            move();

            slowCounter++;
            
            // After a while slow down
            if(slowCounter >= SLOW_DOWN_RATE) {
                slowDown();
                slowCounter =  0;
            }
        }

        checkEntityCollision();
        checkInvincibility();
    }
    
    @Override
    public void move() {
        int newWorldX = worldX + speedX;
        int newWorldY = worldY + speedY;

        checkBlockCollision(newWorldX, newWorldY);
        updateCurrentChunk();
    }

    @Override
    protected void collideWithDroppedItem(DroppedItem droppedItem) {
        // Do nothing if:
        // 1. These are different items
        // 2. This item is a full stack
        // 3. The other item is a full stack
        if((!this.getItem().equals(droppedItem.getItem()))
            || (this.item.isFull())
            || (droppedItem.item.isFull())) {
                return;
            }

        // Both stacks aren't full; coalesce them
        int totalCount = this.getCount() + droppedItem.getCount();
        if(totalCount <= getItem().getMaxStackSize()) {             // Coalesce into a single stack
            this.setCount(totalCount);
            droppedItem.remove();
        } else {                                                    // Use one stack to make the other full
            int amountForMaxStack = this.getItem().getMaxStackSize() - this.getCount();

            this.setCount(this.getCount() + amountForMaxStack);
            droppedItem.setCount(droppedItem.getCount() - amountForMaxStack);
        }
    } 

    @Override
    protected void collideWithPlayer(Player player) {
        player.collidePlayerAndDroppedItem(this);
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
