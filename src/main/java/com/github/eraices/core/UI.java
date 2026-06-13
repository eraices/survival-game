package com.github.eraices.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class UI {

    private Graphics2D g2;

    public UI() {
        // Constructor can initialize fonts or UI state variables later
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

    // Allows GamePanel to hand over the active graphics before drawing
    public void setGraphics(Graphics2D g2) {
        this.g2 = g2;
    }
}