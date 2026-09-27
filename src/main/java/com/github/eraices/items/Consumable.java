package com.github.eraices.items;

public class Consumable extends Item {

    private int hunger;

    public Consumable(String name, int iconID, int maxStackSize, int hunger) {
        super(name, iconID, maxStackSize);
        this.hunger = hunger;
    }
    
    public int getHunger() {
        return hunger;
    }
}
