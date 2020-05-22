package tankgame.GameObjects;

import tankgame.GameWorld;

import java.awt.*;
import java.io.IOException;

import static javax.imageio.ImageIO.read;


public class Wall extends GameObject {
    private int wallHealth;
    private Rectangle rectangle;

    public Wall(int x, int y, int  wallHealth) {
        this.setX(x);
        this.setY(y);
        this.active = true;
        this.wallHealth = wallHealth;

        try {
            if(wallHealth==3) {
                this.image = read(Wall.class.getClassLoader().getResource("darkWall.gif"));
            }
            else if(wallHealth<3){
                this.image = read(Wall.class.getClassLoader().getResource("lightWall.gif"));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    //    rectangle = new Rectangle(x,y,this.image.getWidth(),this.image.getHeight());
         this.rectangle = setRectangle(x,y,this.image.getWidth(),this.image.getHeight());
    }

    @Override
    public void update() {
    }

    @Override
    public void collision(GameObject objectColliding) {
        if(this.wallHealth < 3 && objectColliding instanceof Bullet){
            if(wallHealth == 1){
                try {
                    this.image = read(Bullet.class.getClassLoader().getResource("Explosion_small.gif"));
                    Thread.sleep(50);
                }catch(Exception e){System.out.println("no explosion");}
                    this.active = false;
            }
            wallHealth -=1;
        }

    }
}
