package com.github.eraices.states;

import java.awt.Graphics2D;

import com.github.eraices.core.GamePanel;
import com.github.eraices.core.Key;
import com.github.eraices.core.KeyHandler;
import com.github.eraices.entities.Entity.Direction;
import com.github.eraices.items.Consumable;
import com.github.eraices.items.Item;

public class PlayState implements GameState {
    private GamePanel gp;

    public PlayState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void handleInputPress(int keyCode) {
        checkHotbarKeys(keyCode);
        checkPlayerUse();
        checkPlayerSprint();
        checkPlayerMovement();
    }

    @Override
    public void handleInputRelease(int keyCode) {
        checkPlayerUse();
        checkPlayerSprint();
        checkPlayerMovement();
    }

    @Override
    public void update() {
        gp.player.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.world.draw(g2);
        gp.player.draw(g2);
        gp.ui.drawHUD();
    }

    private void checkHotbarKeys(int keyCode) {
        switch(keyCode) {
            case Key._1 -> gp.player.setHotbarSelection(0);
            case Key._2 -> gp.player.setHotbarSelection(1);
            case Key._3 -> gp.player.setHotbarSelection(2);
            case Key._4 -> gp.player.setHotbarSelection(3);
            case Key._5 -> gp.player.setHotbarSelection(4);
            case Key._6 -> gp.player.setHotbarSelection(5);
            case Key._7 -> gp.player.setHotbarSelection(6);
            case Key._8 -> gp.player.setHotbarSelection(7);
            case Key._9 -> gp.player.setHotbarSelection(8);
        }
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

    private void checkPlayerSprint() {
        if(gp.keyH.isPressed(KeyHandler.SPRINT)) {              // Sprint key pressed; try to sprint
            if ((!gp.player.isSprinting())
                && (!gp.player.isEating())) {
                    gp.player.setIsSprinting(true);
            }
        } else if((!gp.keyH.isPressed(KeyHandler.SPRINT))
                  && (gp.player.isSprinting())) {               // Sprint key not pressed; stop sprinting
            gp.player.setIsSprinting(false);
        } else {                                                // Probably unnecessary, but here
            gp.player.setIsSprinting(false);       // to avoid any problems
        }
    }

    private void checkPlayerUse() {
        if(gp.keyH.isPressed(KeyHandler.USE)) {             // Use item
            Item item = gp.player.getHeldItem();

            if((item != null) && (item instanceof Consumable)
                && (!gp.player.isFull())) {
                gp.player.setIsEating(true);
            }
        } else {                                            // Stop using item
            gp.player.setIsEating(false);
        }
    }
}
