package com.github.eraices.entities.mobs;

import com.github.eraices.core.GamePanel;
import com.github.eraices.entities.PassiveMob;

public class Pig extends PassiveMob {
    private static final int MAX_HEALTH = 10;
    private static final int WIDTH = 16;
    private static final int HEIGHT = 12;
    private static final int SPEED = 2;
    private static final int NUM_HOTBAR_SLOTS = 0;

    public Pig(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY);
        initSelf(MAX_HEALTH, WIDTH, HEIGHT, SPEED, NUM_HOTBAR_SLOTS);
        setSpriteSheet("/sprites/Pig", WIDTH, HEIGHT, 4, 4);
    }
    
    @Override
    public void giveDrops() {

    }
}
