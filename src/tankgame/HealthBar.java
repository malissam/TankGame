package tankgame;

import tankgame.GameObjects.GameObject;
import tankgame.GameObjects.Tank;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static javax.imageio.ImageIO.read;

public class HealthBar{
    Rectangle healthbar;
    int healthstatus;
    int playerNumber;
    int lifeLeft;
    BufferedImage tankIcon;
    int playerScreenEdge;
    public HealthBar(int playerNumber){
        this.healthstatus = 525;
        this.playerNumber = playerNumber;
        this.lifeLeft = 3;
        try {
            tankIcon = read(HealthBar.class.getClassLoader().getResource("tankIcon.png"));
        }catch(Exception e) {System.out.print(e);}
        if(playerNumber == 1){playerScreenEdge = 0;}
        if(playerNumber == 2){playerScreenEdge = GameWorld.SCREEN_WIDTH/2 + 95;}
    }

    public void setHealthstatus(int healthHit){
        this.healthstatus = this.healthstatus - healthHit;

        if(this.healthstatus < 50){
            lifeLeft-=1;
            this.healthstatus = 525;
        }
    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.green);
        if(healthstatus < 200){
            g2d.setColor(Color.red);
        }
        //makes different health bars for both sides of the map.
       // if(this.playerNumber == 1) {
            //makes a filled rectangle the size of the players health
                g2d.fillRect(playerScreenEdge,0,healthstatus,30);
            for(int i = 0; i<lifeLeft; i++) {
                g2d.drawImage(tankIcon, playerScreenEdge +i*40, 0, null);
            }
       // }
//        if(this.playerNumber == 2){
//        //    System.out.println("tank2 health works" + this.healthstatus);
//            g2d.fillRect(GameWorld.SCREEN_WIDTH/2 + 95,0, this.healthstatus,30);}
//        for(int i = 0; i<lifeLeft; i++) {
//            g2d.drawImage(tankIcon, GameWorld.SCREEN_WIDTH/2 + 95 + i*40, 0, null);
//        }
        if(this.lifeLeft == 0){
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0,0,GameWorld.SCREEN_WIDTH,GameWorld.SCREEN_HEIGHT);
            g2d.setColor(Color.red);
            g2d.drawString("Game Over Player " + playerNumber, GameWorld.SCREEN_WIDTH/2-100, GameWorld.SCREEN_HEIGHT/2);
            Thread.yield();
           
        }
        }
       // g2d.drawRect(0,0, 200, 40);


    }
