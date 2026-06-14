package com.github.eraices.core;

import com.github.eraices.states.*;

public class GameStateManager {
    public enum State {
        PLAY, PAUSE, INVENTORY
    }

    private GamePanel gp;
    private GameState currGameState;
    private GameState[] states = new GameState[State.values().length];

    public GameStateManager(GamePanel gp) {
        this.gp = gp;
        initGameStates();
    }

    public GameState getCurrentGameState() {
        return currGameState;
    }

    public void setCurrGameState(State gameState) {
        currGameState = states[gameState.ordinal()];
    }

    private void initGameStates() {
        states[State.PLAY.ordinal()] = new PlayState(gp);
        states[State.PAUSE.ordinal()] = new PauseState(gp);
        states[State.INVENTORY.ordinal()] = new InventoryState(gp);
    }
}
