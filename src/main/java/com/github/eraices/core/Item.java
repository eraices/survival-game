package com.github.eraices.core;

import java.awt.image.BufferedImage;

public class Item {
    private String name;
    private int maxStackSize;
    private boolean consumable;
    private BufferedImage icon;

    public Item(String name, int maxStackSize, boolean consumable) {
        this.name = name;
        this.maxStackSize = maxStackSize;
        this.consumable = consumable;
    }

    public String getName() {
        return name;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public boolean equals(Item otherItem) {
        if(otherItem == null) {
            return false;
        }
        return this.name == otherItem.name;
    }
}
