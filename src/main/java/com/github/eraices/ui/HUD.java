package com.github.eraices.ui;

import com.github.eraices.core.GameEngine;
import com.github.eraices.core.GamePanel;
import com.github.eraices.core.Icon;

public class HUD {
    // These are all relating to the hotbar's size/position
    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;
    private static final int X = 2;
    private static final int Y = 3;
    private static final int NUM_HOTBAR_SLOTS = 9;

    private GamePanel gp;
    private UI ui;

    public HUD(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
    }

    public void draw() {
        ui.drawPlayerCoords();
        drawHotbar();
        drawHearts();
        drawHunger();
    }

    private void drawHearts() {
        int numHearts = gp.player.getMaxHealth() / 2;           // Each heart = 2 health
        int numFullHearts = gp.player.getCurrentHealth() / 2;
        int numHalfHearts = gp.player.getCurrentHealth() % 2;   // Will either be 1 or 0

        // Coordinates of left-most heart
        int startingX = hotbarDimensions(X);
        int startingY = hotbarDimensions(Y) - (gp.tileSize / 2);

        int currentX = startingX; // X coordinate of current heart

        // Draw full hearts, then half hearts, then empty hearts
        for(int heart = 0; heart < numHearts; heart++) {
            if(heart < numFullHearts) {
                ui.g2.drawImage(gp.iManager.getIcon(Icon.FULL_HEART), currentX, startingY, null);
            } else if(heart < (numFullHearts + numHalfHearts)) {
                ui.g2.drawImage(gp.iManager.getIcon(Icon.HALF_HEART), currentX, startingY, null);
            } else {
                ui.g2.drawImage(gp.iManager.getIcon(Icon.EMPTY_HEART), currentX, startingY, null);
            }

            currentX += (gp.tileSize / 2) - gp.scale + 1;
        }
    }

    private void drawHotbar() {
        int hotbarWidth = hotbarDimensions(WIDTH);
        int hotbarHeight = hotbarDimensions(HEIGHT);
        int hotbarX = hotbarDimensions(X);
        int hotbarY = hotbarDimensions(Y);

        ui.drawBox(hotbarX, hotbarY, hotbarWidth, hotbarHeight, false, false);

        // Fake hotbar width and x we'll use for slots, so they're not touching the edge
        int fakeHotbarWidth = hotbarWidth - gp.tileSize;
        int fakeHotbarX = ui.getXForCenteredSubBox(hotbarX, hotbarWidth, fakeHotbarWidth);

        int slotWidth = gp.tileSize;
        int slotHeight = gp.tileSize;
        int slotX; // Will vary depending on the slot
        int slotY = ui.getYForCenteredSubBox(hotbarY, hotbarHeight, slotHeight);
        
        int splitWidth = fakeHotbarWidth / NUM_HOTBAR_SLOTS; // Slots will be centered using this width

        for(int slot = 0; slot < NUM_HOTBAR_SLOTS; slot++) {
            slotX = ui.getXForCenteredSubBox(fakeHotbarX + (splitWidth * slot), splitWidth, slotWidth);

            ui.drawBox(slotX, slotY, slotWidth, slotHeight, gp.player.getHotbarSelection() == (slot + 1), false);
        }
    }

    private void drawHunger() {
        
    }

    private int hotbarDimensions(int dimension) {
        return switch(dimension) {
            case WIDTH -> gp.tileSize * (NUM_HOTBAR_SLOTS + 1);
            case HEIGHT -> gp.tileSize * 3 / 2; // * 1.5
            case X -> (GameEngine.VIRTUAL_SCREEN_WIDTH / 2) - ((gp.tileSize * (NUM_HOTBAR_SLOTS + 1)) / 2);
            case Y -> GameEngine.VIRTUAL_SCREEN_HEIGHT - (gp.tileSize * 3 / 2) - (gp.tileSize / 2);
            default -> 0;
        };
    }
}
