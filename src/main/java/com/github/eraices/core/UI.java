package com.github.eraices.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    private static final float DEFAULT_FONT_SIZE = 20f;

    private GamePanel gp;
    private Graphics2D g2;
    private Font font;

    public UI(GamePanel gp) {
        this.gp = gp;

        // Set the font
        try {
			InputStream is = getClass().getResourceAsStream("/fonts/PokemonGb-RAeo.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(DEFAULT_FONT_SIZE);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
            font = new Font("Arial", Font.PLAIN, (int)DEFAULT_FONT_SIZE); // Fallback font
		}
    }

    public void drawBox(int x, int y, int width, int height, boolean chosen, boolean selected) {
        if (chosen && !selected) {
            g2.setColor(Color.lightGray);
        } else if (selected) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawBorderlessBox(int x, int y, int width, int height, boolean chosen) {
        if (chosen) {
            g2.setColor(Color.lightGray);
        } else {
            g2.setColor(Color.white);
        }

        g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(Color.black);
    }

    public void drawPlayerCoords() {
        int xCoord = gp.player.getXCoord();
        int yCoord = gp.player.getYCoord();

        String text = "(X: " + xCoord + ", Y: " + yCoord + ")";
        int textX = gp.tileSize / 4;
        int textY = gp.tileSize / 4 + this.getTextHeight(text);

        g2.drawString(text, textX, textY);
    }

    public void drawString(String text, int textX, int textY) {
        g2.drawString(text, textX, textY);
    }

    public int getXForCenteredBox(int boxWidth) {
        return (GameEngine.VIRTUAL_SCREEN_WIDTH / 2) - (boxWidth / 2);
    }

    public int getYForCenteredBox(int boxHeight) {
        return (GameEngine.VIRTUAL_SCREEN_HEIGHT / 2) - (boxHeight / 2);
    }

    public int getXForCenteredSubBox(int mainBoxX, int mainBoxWidth, int subBoxWidth) {
        return mainBoxX + (mainBoxWidth / 2) - (subBoxWidth / 2);
    }

    public int getYForCenteredSubBox(int mainBoxY, int mainBoxHeight, int subBoxHeight) {
        return mainBoxY + (mainBoxHeight / 2) - (subBoxHeight / 2);
    }

    public int getXForCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (GameEngine.VIRTUAL_SCREEN_WIDTH / 2) - (length / 2);
    }

    public int getYForCenteredText(String text) {
        int height = (int)g2.getFontMetrics().getAscent();
        return (GameEngine.VIRTUAL_SCREEN_HEIGHT / 2) + (height / 2);
    }

    public int getXForCenteredTextInBox(String text, int boxX, int boxWidth) {
        int width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return boxX + (boxWidth / 2) - (width / 2);
    }

    public int getYForCenteredTextInBox(String text, int boxY, int boxHeight) {
        int height = (int)g2.getFontMetrics().getAscent();
        return boxY + (boxHeight / 2) + (height / 2);
    }
    
    public int getXForRightAlignedText(String text, int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return tailX - length;
	}

	public void setFontSize(int size) {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, size));
	}

    public void setGraphics(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(font);
    }

    private int getTextHeight(String text) {
        return (int)g2.getFontMetrics().getAscent();
    }
}