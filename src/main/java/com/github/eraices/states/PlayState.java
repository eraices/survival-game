package com.github.eraices.states;

import java.awt.Graphics2D;

import com.github.eraices.core.GamePanel;
import com.github.eraices.core.KeyHandler;
import com.github.eraices.entities.Entity.Direction;

public class PlayState implements GameState {
    private GamePanel gp;

    public PlayState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void handleInput(int keyCode) {
        checkPlayerMovement();
    }

    @Override
    public void update() {
        gp.player.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.player.draw(g2);
        gp.ui.drawPlayerCoords();
    }
    
    // If no movement keys are being pressed, stops player movement.
    // If a movement key is being pressed, starts player movement. 
    private void checkPlayerMovement() {
        boolean upPressed = gp.keyH.isPressed(KeyHandler.UP);
        boolean downPressed = gp.keyH.isPressed(KeyHandler.DOWN);
        boolean leftPressed = gp.keyH.isPressed(KeyHandler.LEFT);
        boolean rightPressed = gp.keyH.isPressed(KeyHandler.RIGHT);

        if(!upPressed && !downPressed
            && !leftPressed && !rightPressed) {         // No Movement
            gp.player.setIsMoving(false);
        } else if(upPressed && leftPressed) {           // UP_LEFT movement
            if(!gp.player.isFacing(Direction.LEFT)) {
                gp.player.setDirection(Direction.UP);
            }
            gp.player.setMovingDirection(Direction.UP_LEFT);
            gp.player.setIsMoving(true);
        } else if(upPressed && rightPressed) {          // UP_RIGHT movement
            if(!gp.player.isFacing(Direction.RIGHT)) {
                gp.player.setDirection(Direction.UP);
            }
            gp.player.setMovingDirection(Direction.UP_RIGHT);
            gp.player.setIsMoving(true);
        } else if(downPressed && leftPressed) {         // DOWN_LEFT movement
            if(!gp.player.isFacing(Direction.LEFT)) {
                gp.player.setDirection(Direction.DOWN);
            }
            gp.player.setMovingDirection(Direction.DOWN_LEFT);
            gp.player.setIsMoving(true);
        } else if(downPressed && rightPressed) {        // DOWN_RIGHT movement
            if(!gp.player.isFacing(Direction.RIGHT)) {
                gp.player.setDirection(Direction.DOWN);
            }
            gp.player.setMovingDirection(Direction.DOWN_RIGHT);
            gp.player.setIsMoving(true);
        }
        else if(upPressed) {                            // UP movement
            gp.player.setDirection(Direction.UP);
            gp.player.setMovingDirection(Direction.UP);
            gp.player.setIsMoving(true);
        } else if(downPressed) {                        // DOWN movement
            gp.player.setDirection(Direction.DOWN);
            gp.player.setMovingDirection(Direction.DOWN);
            gp.player.setIsMoving(true);
        } else if(leftPressed) {                        // LEFT movement
            gp.player.setDirection(Direction.LEFT);
            gp.player.setMovingDirection(Direction.LEFT);
            gp.player.setIsMoving(true);
        } else if(rightPressed) {                       // RIGHT movement
            gp.player.setDirection(Direction.RIGHT);
            gp.player.setMovingDirection(Direction.RIGHT);
            gp.player.setIsMoving(true);
        }
    }
}
