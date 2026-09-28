package com.github.eraices.entities;

import com.github.eraices.core.GamePanel;

public class Mob extends Entity {

    public Mob(GamePanel gp, int worldX, int worldY, int speed) {
        super(gp, worldX, worldY, speed);
    }

    @Override
    public void die() {
        giveDrops();
        super.die();
    }
    
    public void giveDrops() {
        // Implemented in child classes
    }
}
