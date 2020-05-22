package tankgame.GameObjects;

import tankgame.GameWorld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.imageio.ImageIO.read;


public class PowerUps extends GameObject {

    private Rectangle rectangle;

    public PowerUps(int x, int y) {

        setX(x);
        setY(y);

        try {
            this.image = read(PowerUps.class.getClassLoader().getResource("shieldpickup.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        this.rectangle = setRectangle(this.x,this.y,this.image.getWidth(),this.image.getHeight());
        this.active = true;
    }

    @Override
    public void update() {
        this.x += this.vx;
        this.y += this.vy;

    }

    @Override
    public void collision(GameObject objectColliding) {
        this.active = false;
    }
}
