package com.github.eraices.states;

import java.awt.Graphics2D;

import com.github.eraices.core.GamePanel;

public class PauseState implements GameState {
    private GamePanel gp;

    public PauseState(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void handleInputPress(int keyCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleInput'");
    }

    @Override
    public void handleInputRelease(int keyCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleInputRelease'");
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void draw(Graphics2D g2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }
}
