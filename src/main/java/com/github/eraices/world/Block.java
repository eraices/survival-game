package com.github.eraices.world;

public class Block {
    public static boolean hasCollision(int blockID) {
        return switch(blockID) {
            case BlockID.TREE -> true;
            case BlockID.STONE -> true;
            case BlockID.WATER -> true;
            default -> false;
        };
    }
}
