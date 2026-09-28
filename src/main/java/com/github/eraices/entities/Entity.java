package com.github.eraices.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.github.eraices.core.AssetHandler;
import com.github.eraices.core.GamePanel;

public class Entity {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    public static final int NUM_I_FRAMES = 30;
    public static final int EAT_TIME = (int)(GamePanel.FPS * 1.5);
    
    protected Direction direction = Direction.DOWN;
    protected Direction movingDirection = Direction.DOWN;
    protected GamePanel gp;
    protected BufferedImage[][] spriteSheet;
    protected BufferedImage sprite;
    protected Rectangle hurtbox;
    protected int spriteNum;
    protected int spriteCounter = 0;
    protected int frameLength;
    protected int worldX;
    protected int worldY;
    protected double speed;
    protected int width;
    protected int height;
    protected boolean isMoving = false;
    protected boolean isSprinting = false;
    protected boolean isEating = false;
    protected int eatCounter;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean isInvincible = false;
    protected int iFrameCounter = 0;

    public Entity(GamePanel gp, int worldX, int worldY, int speed) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public int getXCoord() {
        return worldX / gp.tileSize;
    }

    public int getYCoord() {
        return worldY / gp.tileSize;
    }

    public int getScreenX() {
        return worldX - gp.player.worldX + gp.player.getScreenX();
    }

    public int getScreenY() {
        return worldY - gp.player.worldY + gp.player.getScreenY();
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setMovingDirection(Direction movingDirection) {
        this.movingDirection = movingDirection;
    }

    public void initHurtbox(int entityWidth, int entityHeight) {
        int hurtboxWidth = entityWidth - (gp.scale * 2);
        int hurtboxHeight = entityHeight - (gp.scale * 2);

        hurtbox = new Rectangle(hurtboxWidth, hurtboxHeight);
    }

    public Rectangle getHurtBox() {
        return hurtbox;
    }

    public void setHurtboxLocation(int newWorldX, int newWorldY) {
        hurtbox.setLocation(newWorldX + gp.scale, newWorldY + gp.scale);
    }

    public void setHurtboxLocationToSelf() {
        int newWorldX = worldX + gp.scale;
        int newWorldY = worldY + gp.scale;

        hurtbox.setLocation(newWorldX, newWorldY);
    }

    public boolean isFacing(Direction direction) {
        return (this.direction == direction);
    }

    public boolean isFacingUp() {
        return (direction == Direction.UP) ||
                (direction == Direction.UP_LEFT) ||
                (direction == Direction.UP_RIGHT);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
        
        if(!isMoving) {
            spriteNum = 0;
            spriteCounter = 0;
        }

        setSprite();
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public void setIsSprinting(boolean isSprinting) {
        // If setting it to the same value, do nothing
        if(this.isSprinting == isSprinting) {
            return;
        } else if(isSprinting) {    // Going from walking to sprinting; double speed
            speed *= 2;
        } else {                    // Going from sprinting to walking; halve speed
            speed /= 2;
        }
        this.isSprinting = isSprinting;
    }

    public boolean isEating() {
        return isEating;
    }

    public void setIsEating(boolean isEating) {
        // If setting it to the same value, do nothing
        if(this.isEating == isEating) {
            return;
        } else if(isEating) {       // Going from not eating to eating; halve speed
            if(isSprinting) {       // Stop sprinting, if sprinting
                setIsSprinting(false);
            }
            speed /= 2;
        } else {                    // Going from eating to not eating; double speed
            speed *= 2;
            eatCounter = 0;
        }
        this.isEating = isEating;
    }

    public void setSprite() {
        int row = direction.ordinal();
		sprite = spriteSheet[row][spriteNum];
	}

    public void setSpriteSheet(String fileName, int frameWidth, int frameHeight, int rows, int cols) {
        int scaledWidth = frameWidth * gp.scale;
        int scaledHeight = frameHeight * gp.scale;

        spriteSheet = AssetHandler.loadSpriteSheet
                       (fileName, frameWidth, frameHeight, rows, cols, scaledWidth, scaledHeight);

        setSprite();
     }

    public void update() {
        // TODO: Implement this
        checkInvincibility();
    }

    public void draw(Graphics2D g2) {
        // TODO: Implement draw logic for non-player entities
    }

	public void changeFrame() {
		if(spriteNum < (spriteSheet[0].length - 1)) {
			spriteNum++;
		} else {
			spriteNum = 0;
		}
		
		setSprite();
	}

    public void checkInvincibility() {
        if(isInvincible) {
            iFrameCounter++;

            if(iFrameCounter >= NUM_I_FRAMES) {
                isInvincible = false;
                iFrameCounter = 0;
            }
        }
    }

    public void checkSprite() {
		if(spriteCounter < frameLength) {
			spriteCounter++;
		} else {
			spriteCounter = 0;
			changeFrame();
		}
	}

    public void move() {
        // Tentative coordinates of the entity's new location
        int newWorldX = worldX;
        int newWorldY = worldY;

        switch(movingDirection) {
            case UP -> newWorldY -= speed;
            case DOWN -> newWorldY += speed;
            case LEFT -> newWorldX -= speed;
            case RIGHT -> newWorldX += speed;
            case UP_LEFT -> {
                newWorldY -= speed;
                newWorldX -= speed;
            }
            case UP_RIGHT -> {
                newWorldY -= speed;
                newWorldX += speed;
            }
            case DOWN_LEFT -> {
                newWorldY += speed;
                newWorldX -= speed;
            }
            case DOWN_RIGHT -> {
                newWorldY += speed;
                newWorldX += speed;
            }
        }

        // Check horizontal and vertical collision separately,
        // since we can move diagonally
        setHurtboxLocation(newWorldX, worldY);
        if(!gp.cChecker.checkBlockCollision(this)) {
            worldX = newWorldX;
        } else {
            worldX = nudgeHorizontally(newWorldX, worldX);
        }

        setHurtboxLocation(worldX, newWorldY);
        if(!gp.cChecker.checkBlockCollision(this)) {
            worldY = newWorldY;
        } else {
            worldY = nudgeVertically(newWorldY, worldY);
        }

        setHurtboxLocationToSelf();
        checkSprite();
    }

    public int nudgeHorizontally(int newWorldX, int worldX) {
        // Offset because hurtbox is smaller than its entity
        int offset = gp.scale;

        if(newWorldX > worldX) {
            // Moving right, snap to the left side of the block that was hit
            int hitXCoord = Math.floorDiv(hurtbox.x + hurtbox.width - 1, gp.tileSize);
            int leftSideX = hitXCoord * gp.tileSize;

            // The new position should be just 1 pixel away from that side
            return leftSideX - hurtbox.width - offset;
        } else if (worldX > newWorldX) {
            // Moving left, snap to the right side of the block that was hit
            int hitXCoord = Math.floorDiv(hurtbox.x, gp.tileSize);
            int rightSideX = (hitXCoord + 1) * gp.tileSize;

            // The new position should be just 1 pixel away from that side
            return rightSideX - offset;
        }

        return worldX; // Entity is already touching the edge
    }

    public int nudgeVertically(int newWorldY, int worldY) {
        // Offset because hurtbox is smaller than its entity
        int offset = gp.scale;

        if(newWorldY > worldY) {
            // Moving down, snap to the top side of the block that was hit
            int hitYCoord = Math.floorDiv(hurtbox.y + hurtbox.height - 1, gp.tileSize);
            int topSideY = hitYCoord * gp.tileSize;

            // The new position should be just 1 pixel away from that side
            return topSideY - hurtbox.height - offset;
        } else if (worldY > newWorldY) {
            // Moving up, snap to the bottom side of the block that was hit
            int hitYCoord = Math.floorDiv(hurtbox.y, gp.tileSize);
            int bottomSideY = (hitYCoord + 1) * gp.tileSize;

            // The new position should be just 1 pixel away from that side
            return bottomSideY - offset;
        }

        return worldY; // Entity is already touching the edge
    }

    public void die() {
        // TODO: Proplerly implement death
        speed = 0;
    }

    public void heal(int amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
    }

    public void takeDamage(int amount) {
        // Only take damage if not in invincibility
        if(!isInvincible) {
            currentHealth = Math.max(currentHealth - amount, 0);
        }
        
        if(currentHealth <= 0) {
            die();
        } else {
            isInvincible = true;
        }
    }
}
