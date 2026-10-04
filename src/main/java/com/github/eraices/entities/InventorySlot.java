package com.github.eraices.entities;

import com.github.eraices.items.Item;

public class InventorySlot {
    private Item item;
    private int count;

    public InventorySlot(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isFull() {
        return count == item.getMaxStackSize();
    }

    public boolean hasSpaceFor(int count) {
        return this.count + count <= item.getMaxStackSize();
    }

    public void add(Item item) {
        // Only add item if slot is empty, or if slot already has this item
        // and there's enough space for more
        if(isEmpty()) {
            this.item = item;
            count++;
        }
        else if ((this.item.equals(item) && count < item.getMaxStackSize())) {
            count++;
        }
    }

    public void remove() {
        // Only remove if there's something to remove
        if(!isEmpty()) {
            count--;
            if(count == 0) {
                this.item = null;
            }
        }
    }

    public boolean isEmpty() {
        return item == null;
    }
}
