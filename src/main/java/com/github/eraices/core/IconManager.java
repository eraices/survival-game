package com.github.eraices.core;

import java.awt.image.BufferedImage;

public class IconManager {
    private GamePanel gp;
    private BufferedImage[] icons = new BufferedImage[Icon.NUM_ICONS];

    public IconManager(GamePanel gp) {
        this.gp = gp;
        initIcons();
    }

    public BufferedImage getIcon(int icon) {
        return icons[icon];
    }

    private void initIcons() {
        // Calculate parameters for AssetHandler
        int frameWidth = gp.ogTileSize / 2;     // Dividing these by 2 because icons are
        int frameHeight = gp.ogTileSize / 2;    // 8x8 instead of 16x16
        int rows = 1;
        int cols = icons.length;
        int scaledWidth = frameWidth * gp.scale;
        int scaledHeight = frameHeight * gp.scale;

        // Load icons
        icons = AssetHandler.loadSpriteSheet("/icons/Icons", frameWidth, frameHeight, rows, cols, scaledWidth, scaledHeight)[0];
    }
}
