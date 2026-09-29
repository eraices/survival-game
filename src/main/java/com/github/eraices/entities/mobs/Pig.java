package com.github.eraices.entities.mobs;

import com.github.eraices.core.GamePanel;
import com.github.eraices.entities.PassiveMob;

public class Pig extends PassiveMob {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 12;
    private static int PIG_SPEED = 2;

    public Pig(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY);
        speed = PIG_SPEED;
        setSpriteSheet("/sprites/Pig", WIDTH, HEIGHT, 4, 4);
    }
    
    @Override
    public void giveDrops() {

    }
}
