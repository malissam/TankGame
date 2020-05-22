package tankgame.GameObjects;

import tankgame.GameWorld;
import tankgame.HealthBar;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

/**
 *
 * @author  Malissa Murga
 */
public class Tank extends GameObject {

    private final int R = 2;
    private final int ROTATION_SPEED = 2 ;
    private double lastBullet =0;
    private double shieldStart =0;
    private BufferedImage tankImage;
    //private BufferedImage tankImage2;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private boolean shield = false;
    private int shieldHealth = 3;
    private Rectangle lastPosition;
    private int lifeLeft = 3;
    ///public int health;
    public HealthBar healthBar;
    public ArrayList<Bullet> bulletBarrage;


    public Tank(int x, int y, int vx, int vy, int angle, int playerCount)  {
        this.setX(x);
        this.setY(y);
        this.setVx(vx);
        this.setVy(vy);
        this.setAngle(angle);
        this.isActive();
        try {
            if (playerCount == 1) {
               tankImage = read(Tank.class.getClassLoader().getResource("tank1.png"));
               this.image = tankImage;
            }
             if(playerCount == 2){
                tankImage = read(Tank.class.getClassLoader().getResource("tank2.png"));
                this.image = tankImage;
            }

        }catch(Exception e){System.out.print(e);}

        this.bulletBarrage = new ArrayList<>();
        this.rectangle = setRectangle(this.x,this.y,this.image.getWidth(),this.image.getHeight());
       // this.health = 700;
        this.healthBar = new HealthBar(playerCount);
    }

    public void toggleUpPressed() {
        this.UpPressed = true;
    }
    public void toggleDownPressed() {
        this.DownPressed = true;
    }
    public void toggleRightPressed() {
        this.RightPressed = true;
    }
    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }
    public void toggleShootPressed() {
        this.ShootPressed = true;
    }
    public void unToggleUpPressed() {
        this.UpPressed = false;
    }
    public void unToggleDownPressed() {
        this.DownPressed = false;
    }
    public void unToggleRightPressed() {
        this.RightPressed = false;
    }
    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }
    public void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    public void update() {
        lastPosition = getRectangle();
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.ShootPressed && (System.currentTimeMillis()-lastBullet)>500) {
            Bullet bullet = new Bullet(x, y, vx, vy, angle);
            this.bulletBarrage.add(bullet);
            lastBullet = System.currentTimeMillis();
           // System.out.println("shots fired");
        }
        //for each bullet do this...

        this.bulletBarrage.forEach(bullet -> bullet.update());

    }

    private void rotateLeft() {
        this.angle -= this.ROTATION_SPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATION_SPEED;
    }
    private void moveBackwards() {

        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }
    private void moveForwards() {

        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void checkBorder() {
        if (x < 33) {
            x = 33;
        }
        //the 88 is cause of the width of the tank 00 is upperleft corner of image
        if (x >= GameWorld.WORLD_WIDTH - 88) {
            x = GameWorld.WORLD_WIDTH - 88;
        }
        if (y < 33) {
            y = 33;
        }
        if (y >= GameWorld.WORLD_HEIGHT - 88) {
            y = GameWorld.WORLD_HEIGHT - 88;
        }
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.image.getWidth()/2.0 , this.image.getHeight()/2.0);
        Graphics2D g2d = (Graphics2D) g;
        this.bulletBarrage.forEach(bullet -> bullet.drawImage(g));
        g2d.drawImage(this.image, rotation, null);
    }
    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public void collision(GameObject objectColliding) {
        if (objectColliding instanceof Wall || objectColliding instanceof Tank) {
            // this.moveBackwards();
            this.setX(lastPosition.x);
            this.setY(lastPosition.y);
        }
        if (objectColliding instanceof PowerUps) {
            shieldStart = System.currentTimeMillis();
          //  while (System.currentTimeMillis() - shieldStart < 10000) {
                this.shield = true;
                try {
                    this.image = read(Tank.class.getClassLoader().getResource("shieldtank.png"));
                 //   Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.print(e);
                }
            //}
           // this.shield = false;
           // this.image = tankImage;

        }


        if(objectColliding instanceof Bullet){
            //tankImage = this.image; //save the current tank image to return to after being shot

            if(this.shield){ // if there's a shield
                System.out.println(shieldHealth);
                if(this.shieldHealth == 0){ // if its greater than 1 weaken shield
                    this.shield = false; // if its 0 then set shield to false
                }
                else{ this.shieldHealth -= 1;}
            }
            else{  //if theres no shield make a lil explosion and then return to the tank image
                try {
                    this.image = read(Tank.class.getClassLoader().getResource("Explosion_small.gif"));
                    Thread.sleep(50);
                } catch (Exception e) {
                    System.out.println("no explosion");
                }
                this.image = tankImage;
                healthBar.setHealthstatus(100); ///the 100 is subtracted from the health
            }


        }




    }
}
