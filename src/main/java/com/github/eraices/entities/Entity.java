package com.github.eraices.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.eraices.core.AssetHandler;
import com.github.eraices.core.GamePanel;

public class Entity {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }
    
    protected Direction direction = Direction.DOWN;
    protected Direction movingDirection = Direction.DOWN;
    protected GamePanel gp;
    protected BufferedImage[][] spriteSheet;
    protected BufferedImage sprite;
    protected int spriteNum;
    protected int spriteCounter = 0;
    protected int frameLength;
    protected int worldX;
    protected int worldY;
    protected int speed;
    protected int width;
    protected int height;
    protected boolean isMoving = false;

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

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setMovingDirection(Direction movingDirection) {
        this.movingDirection = movingDirection;
    }

    public boolean isFacing(Direction direction) {
        return (this.direction == direction);
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

    public void checkSprite() {
		if(spriteCounter < frameLength) {
			spriteCounter++;
		} else {
			spriteCounter = 0;
			changeFrame();
		}
	}

    public void move() {
        switch(movingDirection) {
            case UP -> worldY -= speed;
            case DOWN -> worldY += speed;
            case LEFT -> worldX -= speed;
            case RIGHT -> worldX += speed;
            case UP_LEFT -> {
                worldY -= speed;
                worldX -= speed;
            }
            case UP_RIGHT -> {
                worldY -= speed;
                worldX += speed;
            }
            case DOWN_LEFT -> {
                worldY += speed;
                worldX -= speed;
            }
            case DOWN_RIGHT -> {
                worldY += speed;
                worldX += speed;
            }
        }

        checkSprite();
    }
}
