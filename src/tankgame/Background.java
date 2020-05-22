package tankgame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Background {
    private int screenWidth;
    private int screenHeight;

    private BufferedImage backgroundImage;

    Background(int width, int height, BufferedImage backgroundImage) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.backgroundImage = backgroundImage;

    }

    void drawImage(Graphics g) {
        int j = 0;
        while (j < screenHeight) {
            int i = 0;
            while (i < screenWidth) {
                AffineTransform position = AffineTransform.getTranslateInstance(i, j);
                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(this.backgroundImage, position, null);
                i = i + 300;
            }
            j = j + 200;
        }
    }
}

