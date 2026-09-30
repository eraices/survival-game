package com.github.eraices.world;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.github.eraices.entities.Entity;

public class Chunk {
    // Chunks are 16 x 16 blocks
    public static final int CHUNK_SIZE = 16;

    // Coordinates on the chunk grid;
    // e.g. Chunk at (0, 0) includes all blocks
    // from (0, 0) to (15, 15)
    public final int chunkX;
    public final int chunkY;

    public ArrayList<Entity> entityList = new ArrayList<>();

	private ArrayList<Entity> entityAddList = new ArrayList<>();
	private ArrayList<Entity> entityRemoveList = new ArrayList<>();
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

    public void update() {
        // TODO: Implement chunk updates
    }

	public void addEntity(Entity e) {
		entityAddList.add(e);
	}

	public void removeEntity(Entity e) {
		entityRemoveList.add(e);
	}

	public void refreshEntityList() {
		if(!entityRemoveList.isEmpty()) {			// Remove entities that need to be removed
			entityList.removeAll(entityRemoveList);
			entityRemoveList.clear();
		}
		if(!entityAddList.isEmpty()) {				// Add entities tht need to be added
			entityList.addAll(entityAddList);
			entityAddList.clear();
		}
	}

    @Override
    public String toString() {
        return "(" + chunkX + ", " + chunkY + ")";
    }
}
