package tankgame.GameObjects;

import tankgame.GameWorld;

import java.awt.*;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class Bullet extends GameObject{

 //   private Rectangle rectangle;
    private final int R = 7;

    Bullet(int x, int y, int vx, int vy, int angle) {
        setX(x);
        setY(y);
        setVx(vx);
        setVy(vy);
        setAngle(angle);
        try {
            this.image = read(Bullet.class.getClassLoader().getResource("Bullet.png"));
            //System.out.println("bullet image loaded");
        } catch (
                IOException ex) {
            System.out.println(ex.getMessage());
        }
        this.rectangle = new Rectangle(x, y, this.image.getWidth(), this.image.getHeight());
    }


    public void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    public void checkBorder() {
        if(x<30){
            this.active = false;
        }

        if(x >= GameWorld.WORLD_WIDTH - 88) {
           // x = GameWorld.WORLD_WIDTH - 88;
            this.active = false;
        }
        if (y < 30) {
            this.active = false;
        }
        if (y >= GameWorld.WORLD_HEIGHT - 80) {
           // y = GameWorld.WORLD_HEIGHT - 80;
            this.active = false;
        }
    }

    @Override
    public void update() {
        moveForwards();
    }

    @Override
    public void collision(GameObject objectColliding) {

//        try {
//            this.image = read(Bullet.class.getClassLoader().getResource("Explosion_small.gif"));
//            Thread.sleep(100);
//        }catch(Exception e){System.out.println("no explosion");}
        this.active = false;
    }
}
