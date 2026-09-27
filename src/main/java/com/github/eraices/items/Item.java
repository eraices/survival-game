package com.github.eraices.items;

public class Item {
    private String name;
    private int iconID;
    private int maxStackSize;

    public Item(String name, int iconID, int maxStackSize) {
        this.name = name;
        this.iconID = iconID;
        this.maxStackSize = maxStackSize;
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

    public boolean equals(Item otherItem) {
        if(otherItem == null) {
            return false;
        }
        return this.name == otherItem.name;
    }
}
