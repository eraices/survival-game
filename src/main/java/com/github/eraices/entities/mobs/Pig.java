package com.github.eraices.entities.mobs;

import com.github.eraices.core.GamePanel;
import com.github.eraices.entities.PassiveMob;

public class Pig extends PassiveMob {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 12;

    public Pig(GamePanel gp, int worldX, int worldY, int speed) {
        super(gp, worldX, worldY, speed);
        setSpriteSheet("/sprites/Pig", WIDTH, HEIGHT, 4, 4);
    }
    
    @Override
    public void giveDrops() {

    }
}
