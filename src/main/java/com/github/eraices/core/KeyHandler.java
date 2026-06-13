package com.github.eraices.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private static final int NUM_CONTROLS = 10;
    
    private boolean[] pressed = new boolean[NUM_CONTROLS];

    @Override
    public void keyTyped(KeyEvent e) {
        // This won't be used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch(code) {
            case Key.W -> press(UP);
            case Key.A -> press(LEFT);
            case Key.S -> press(DOWN);
            case Key.D -> press(RIGHT);
            default -> {} // Do nothing
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch(code) {
            case Key.W -> release(UP);
            case Key.A -> release(LEFT);
            case Key.S -> release(DOWN);
            case Key.D -> release(RIGHT);
            default -> {} // Do nothing
        }
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
