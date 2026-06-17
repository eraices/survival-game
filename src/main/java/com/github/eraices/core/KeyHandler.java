package com.github.eraices.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public static final int NUM_CONTROLS = 10;
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    
    private GamePanel gp;
    private boolean[] pressed = new boolean[NUM_CONTROLS];

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // This won't be used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Press key
        switch(code) {
            case Key.W -> press(UP);
            case Key.A -> press(LEFT);
            case Key.S -> press(DOWN);
            case Key.D -> press(RIGHT);
            case Key.F_11 -> gp.toggleFullscreen();
            default -> {} // Do nothing
        }

        // Handle input in the proper game state
        gp.gsm.getCurrentGameState().handleInput(e.getKeyCode());
    }

    @Override public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        // Release key
        switch(code) {
            case Key.W -> release(UP);
            case Key.A -> release(LEFT);
            case Key.S -> release(DOWN);
            case Key.D -> release(RIGHT);
            default -> {} // Do nothing
        }

        // Handle input in the proper game state
        gp.gsm.getCurrentGameState().handleInput(e.getKeyCode());
    }

    public boolean isPressed(int key) {
        if(key < 0 || key >= NUM_CONTROLS) { // Invalid key
            return false;
        }
        return pressed[key];
    }

    private void press(int key) {
        pressed[key] = true;
    }

    private void release(int key) {
        pressed[key] = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for(int i = UP; i <= RIGHT; i++) {
            sb.append(pressed[i] + (i < RIGHT ? ", " : ""));
        }
        sb.append("]");
        return sb.toString();
    }
}
