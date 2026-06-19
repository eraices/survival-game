package com.github.eraices.core;

import java.awt.Rectangle;

import com.github.eraices.entities.Entity;
import com.github.eraices.world.Block;
import com.github.eraices.world.Chunk;

public class CollisionChecker {
    private GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkBlockCollision(Entity e) {
        Rectangle hurtbox = e.getHurtBox();

        // All the blocks in these bounds need to be checked
        int startX = Math.floorDiv(hurtbox.x, gp.tileSize);
        int startY = Math.floorDiv(hurtbox.y, gp.tileSize);
        int endX = Math.floorDiv(hurtbox.x + hurtbox.width - 1, gp.tileSize);
        int endY = Math.floorDiv(hurtbox.y + hurtbox.height - 1, gp.tileSize);

        // Loop through each block within the hurbox's bounds
        for(int blockX = startX; blockX <= endX; blockX++) {
            for(int blockY = startY; blockY <= endY; blockY++) {

                // Figure out which block we're checking against
                int chunkX = (int) (Math.floor((double) blockX / Chunk.CHUNK_SIZE));
                int chunkY = (int) (Math.floor((double) blockY / Chunk.CHUNK_SIZE));

                int localX = Math.floorMod(blockX, Chunk.CHUNK_SIZE);
                int localY = Math.floorMod(blockY, Chunk.CHUNK_SIZE);

                Chunk chunk = gp.world.getChunk(chunkX, chunkY);
                int blockID = chunk.getBlockAt(localX, localY);
                
                // Check if this block has collision
                if(Block.hasCollision(blockID)) {
                    return true;
                }
            }
        }

        return false; // No collisions
    }
}
