package com.github.eraices.world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.github.eraices.core.AssetHandler;
import com.github.eraices.core.GamePanel;

public class WorldManager {
    private Map<String, Chunk> loadedChunks = new HashMap<>(); // Holds already-generated chunks
    
    private GamePanel gp;
    private BufferedImage[] blockTextures;
    private long seed = 1654861354861351L;
    private double frequency = 0.05;

    public WorldManager(GamePanel gp) {
        this.gp = gp;
        seed = System.nanoTime();
        initBlockTextures();
    }

    /**
     * Returns a chunk. If no chunk exists at these coordinates,
     * generate one.
     */
    public Chunk getChunk(int chunkX, int chunkY) {
        String key = chunkX + "," + chunkY;

        if(loadedChunks.containsKey(key)) {
            return loadedChunks.get(key);
        }

        // Chunk doesn't exist yet; generate it
        Chunk newChunk = generateChunk(chunkX, chunkY);
        loadedChunks.put(key, newChunk);

        return newChunk;
    }

    public void draw(Graphics2D g2) {
        // Calculate how many pixels are in a whole chunk
        int chunkPixelSize = Chunk.CHUNK_SIZE * gp.tileSize;

        // Calculate starting/ending chunk coordinates of chunks that will be drawn
        int startChunkX = (int) Math.floor((double) (gp.player.getWorldX() - gp.player.getScreenX()) / chunkPixelSize);
        int endChunkX = (int) Math.floor((double) (gp.player.getWorldX() + gp.player.getScreenX()) / chunkPixelSize) + 1;
        int startChunkY = (int) Math.floor((double) (gp.player.getWorldY() - gp.player.getScreenY()) / chunkPixelSize);
        int endChunkY = (int) Math.floor((double) (gp.player.getWorldY() + gp.player.getScreenY()) / chunkPixelSize) + 1;

        Chunk chunk;

        // Loop through each visible chunk
        for(int currChunkX = startChunkX; currChunkX <= endChunkX; currChunkX++) {
            for(int currChunkY = startChunkY; currChunkY <= endChunkY; currChunkY++) {
                chunk = getChunk(currChunkX, currChunkY);

                // Now loop through each block in this chunk
                for(int localX = 0; localX < Chunk.CHUNK_SIZE; localX++) {
                    for(int localY = 0; localY < Chunk.CHUNK_SIZE; localY++) {

                        // Determine which block this is
                        int blockID  = chunk.getBlockAt(localX, localY);

                        // If it's air, skip drawing
                        if(blockID == BlockID.AIR) {
                            continue;
                        }

                        // Convert local coordinates to world coordinates
                        int worldX = (currChunkX * chunkPixelSize) + (localX * gp.tileSize);
                        int worldY = (currChunkY * chunkPixelSize) + (localY * gp.tileSize);

                        // Convert world coordinates to screen coordinates
                        int screenX = worldX - gp.player.getWorldX() + gp.player.getScreenX();
                        int screenY = worldY - gp.player.getWorldY() + gp.player.getScreenY();

                        // Draw this block
                        g2.drawImage(blockTextures[blockID], screenX, screenY, null);
                    }
                }
            }
        }
    }

    private Chunk generateChunk(int chunkX, int chunkY) {
        Chunk chunk = new Chunk(chunkX, chunkY);

        // x and y represent chunk-relative coordinates
        for(int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for(int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                // Calculate world coordinates to determine the block
                int worldX = (chunkX * Chunk.CHUNK_SIZE) + x;
                int worldY = (chunkY * Chunk.CHUNK_SIZE) + y;

                // Now determine the block
                int blockID = generateBlock(worldX, worldY);
                chunk.setBlockAt(x, y, blockID);
            }
        }

        return chunk;
    }

    private int generateBlock(int worldX, int worldY) {
        double noise = OpenSimplex2S.noise2(seed, worldX * frequency, worldY * frequency);

        // Map the continuous noise value to specific block types
        if (noise < -0.40) {
            return BlockID.WATER;
        } else if (noise < -0.25) {
            return BlockID.SAND;
        } else if (noise < -0.05) {
            return BlockID.DIRT;
        } else if (noise < 0.55) {
            return BlockID.GRASS;
        } else {
            return BlockID.TREE;
        }
    }

    private void initBlockTextures() {
        blockTextures = new BufferedImage[BlockID.NUM_BLOCKS];

        // Calculate parameters for AssetHandler
        int frameWidth = gp.ogTileSize;
        int frameHeight = gp.ogTileSize;
        int rows = 1;
        int cols = blockTextures.length - 1; // - 1 because no air texture
        int scaledWidth = gp.tileSize;
        int scaledHeight = gp.tileSize;

        // Load tileset
        BufferedImage[] tileset = AssetHandler.loadSpriteSheet("/textures/Tileset", frameWidth, frameHeight, rows, cols, scaledWidth, scaledHeight)[0];

        // Start at ID 1 because ID 0 is air,
        // which has no texture
        for(int blockID = 1; blockID < blockTextures.length; blockID++) {
            blockTextures[blockID] = tileset[blockID - 1]; // - 1 because no air texture
        }
    }
}
