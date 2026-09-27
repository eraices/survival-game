package com.github.eraices.states;

import java.awt.Graphics2D;

// Each game state should handle input, updates,
// and screen rendering in their own ways
public interface GameState {
    public void handleInputPress(int keyCode);
    public void handleInputRelease(int keyCode);
    public void update();
    public void draw(Graphics2D g2);
}
