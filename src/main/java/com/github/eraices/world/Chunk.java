package com.github.eraices.world;

public class Chunk {
    // Chunks are 16 x 16 blocks
    public static final int CHUNK_SIZE = 16;

    // Coordinates on the chunk grid;
    // e.g. Chunk at (0, 0) includes all blocks
    // from (0, 0) to (15, 15)
    public final int chunkX;
    public final int chunkY;

    private int[][] blocks;

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.blocks = new int[CHUNK_SIZE][CHUNK_SIZE];
    }

    public int getBlockAt(int localX, int localY) {
        return blocks[localX][localY];
    }

    public void setBlockAt(int localX, int localY, int blockID) {
        blocks[localX][localY] = blockID;
    }
}
