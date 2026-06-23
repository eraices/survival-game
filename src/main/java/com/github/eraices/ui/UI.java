package com.github.eraices.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

import com.github.eraices.core.GameEngine;
import com.github.eraices.core.GamePanel;
import com.github.eraices.core.Icon;

public class UI {
    protected static final float DEFAULT_FONT_SIZE = 20f;

    protected GamePanel gp;
    protected Graphics2D g2;
    protected Font font;
    protected Color boxColor = new Color(16, 20, 31); // Black
    protected Color borderColor = new Color(168, 181, 178); // Gray
    protected Color textColor = new Color(235, 237, 233); // White
    protected Color chosenColor = new Color(168, 181, 178); // Gray
    protected Color selectedColor = new Color(87, 114, 119); // Dark gray

    private HUD hud;

    public UI(GamePanel gp) {
        this.gp = gp;
        hud = new HUD(gp, this);

        // Set the font
        try {
			InputStream is = getClass().getResourceAsStream("/fonts/PokemonGb-RAeo.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(DEFAULT_FONT_SIZE);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
            font = new Font("Arial", Font.PLAIN, (int)DEFAULT_FONT_SIZE); // Fallback font
		}
    }

    public void drawHUD() {
        hud.draw();
    }

    public void drawBox(int x, int y, int width, int height, boolean chosen, boolean selected) {
        if (chosen && !selected) {
            g2.setColor(chosenColor);
        } else if (selected) {
            g2.setColor(selectedColor);
        } else {
            g2.setColor(boxColor);
        }
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(gp.scale));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawBorderlessBox(int x, int y, int width, int height, boolean chosen) {
        if (chosen) {
            g2.setColor(Color.lightGray);
        } else {
            g2.setColor(boxColor);
        }

        g2.fillRoundRect(x, y, width, height, 35, 35);
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

    protected int getTextHeight(String text) {
        return (int)g2.getFontMetrics().getAscent();
    }

    protected void drawPlayerCoords() {
        int xCoord = gp.player.getXCoord();
        int yCoord = gp.player.getYCoord();

        String text = "(X: " + xCoord + ", Y: " + yCoord + ")";
        int textX = gp.tileSize / 4;
        int textY = gp.tileSize / 4 + this.getTextHeight(text);

        g2.setColor(textColor);
        g2.drawString(text, textX, textY);
    }
}