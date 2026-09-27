package com.github.eraices.core;

public class Item {
    private String name;
    private int iconID;
    private int maxStackSize;
    private boolean consumable;

    public Item(String name, int iconID, int maxStackSize, boolean consumable) {
        this.name = name;
        this.iconID = iconID;
        this.maxStackSize = maxStackSize;
        this.consumable = consumable;
    }

    public String getName() {
        return name;
    }

    public int getIconID() {
        return iconID;
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
