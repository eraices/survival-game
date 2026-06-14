package com.github.eraices.entities;

import java.awt.Graphics2D;

import com.github.eraices.core.GamePanel;

public class Entity {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }
    
    protected Direction direction = Direction.DOWN;
    protected GamePanel gp;
    protected int worldX = 0;
    protected int worldY = 0;
    protected int speed = 2;
    protected int width;
    protected int height;
    protected boolean isMoving = false;

    public Entity(int worldX, int worldY, int speed, GamePanel gp) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.gp = gp;
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

    public boolean isFacing(Direction direction) {
        return (this.direction == direction);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void update() {
        // TODO: Implement this
    }

    public void draw(Graphics2D g2) {
        // TODO: Implement draw logic for non-player entities
    }

    public void move() {
        switch(direction) {
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
    }
}
