package com.github.eraices.core;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class AssetHandler {

    // Loads a sprite sheet, slices it into frames, then scales each frame
    public static BufferedImage[][] loadSpriteSheet
     (String fileName, int frameWidth, int frameHeight, int rows, int cols, int scaledWidth, int scaledHeight) {
        BufferedImage[][] sprites = new BufferedImage[rows][cols];

        try {
            BufferedImage spriteSheet = ImageIO.read
             (Objects.requireNonNull(AssetHandler.class.getResource("/sprites/" + fileName + ".png")));

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    BufferedImage sprite = spriteSheet.getSubimage
                     (col * frameWidth, row * frameHeight, frameWidth, frameHeight);
                    sprites[row][col] = scaleImage(sprite, scaledWidth, scaledHeight);
                }
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading sprite sheet " + fileName + ".png");
            e.printStackTrace();
        }

        return sprites;
    }

    public static BufferedImage scaleImage(BufferedImage original, int scaledWidth, int scaledHeight) {
        // Makes sure sprites with transparency are transparent, and those without aren't
        int type = (original.getType() == BufferedImage.TYPE_CUSTOM)
                    ? BufferedImage.TYPE_INT_ARGB : original.getType();

        // Create an 'empty' image
		BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, type);
		Graphics2D g2 = scaledImage.createGraphics();

        // Draw the scaled image
		g2.drawImage(original, 0, 0, scaledWidth, scaledHeight, null);
		g2.dispose();
		
		return scaledImage;
	}
}
