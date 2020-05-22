/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import tankgame.GameObjects.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


import static javax.imageio.ImageIO.read;

/**
 * Main driver class of Tank Example.
 * Class is responsible for loading resources and
 * initializing game objects. Once completed, control will
 * be given to infinite loop which will act as our game loop.
 * A very simple game loop.
 * @author anthony-pc
 */
//JPANELL IS BLACK PART OF SCREEN
public class GameWorld extends JPanel  {
    public static final int WORLD_WIDTH =2000;
    public static final int WORLD_HEIGHT =2000;
    public static final int SCREEN_WIDTH = 1290;
    public static final int SCREEN_HEIGHT = 700;
    public BufferedImage world;
    private Graphics2D buffer;
    private JFrame jFrame; //TITLEBAR
    private Tank tankOne;
    private Tank tankTwo;
    private Background background;
    private ArrayList<GameObject> GameObjectsList;

    public static void main(String[] args) {
        GameWorld tankgame = new GameWorld();

        tankgame.init();
        try {
                while (true) {
                    tankgame.tankOne.update();
                    tankgame.tankTwo.update();
                    tankgame.repaint();
                    tankgame.checkCollision();


         //           Collision CollisionBetween = new Collision(GameObjectList.get(0), tankgame.tankTwo);
//                    tankgame.tankOne.collision(tankgame.tankOne,tankgame.tankTwo);
//                    tankgame.tankTwo.collision(tankgame.tankOne,tankgame.tankTwo);

                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void init() {
        this.jFrame = new JFrame("Tank Game");
        this.world = new BufferedImage(GameWorld.WORLD_WIDTH, GameWorld.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage backgroundImage = null;

        GameObjectsList = new ArrayList<>();

        try {
            //Using class loaders to read in resources
            backgroundImage =  read(GameWorld.class.getClassLoader().getResource("Background.bmp"));

            background = new Background(WORLD_WIDTH,WORLD_HEIGHT-50,backgroundImage);
            //location of tank at start
            tankOne = new Tank(33, 33, 0, 0, 0,1);

            tankTwo = new Tank(WORLD_WIDTH-100, 33, 0, 0, 180,2);


            InputStreamReader isr = new InputStreamReader(GameWorld.class.getClassLoader().getResourceAsStream("maps/map1"));
            BufferedReader mapReader = new BufferedReader(isr);
            String row = mapReader.readLine();
            if(row==null){
                throw new IOException("No data in file"); }
            String[] mapInfo = row.split("\t");
            int numCols = Integer.parseInt(mapInfo[0]);
            int numRows = Integer.parseInt(mapInfo[1]);
//////////////fix it: there is an issue with the bottom and right most border. Possibly a ratio issue between image size and world size
            for(int curRow = 0; curRow<numRows-1; curRow++){
                row = mapReader.readLine();
                mapInfo = row.split("\t");
                for(int curCol = 0; curCol<numCols-1; curCol++){
                    switch(mapInfo[curCol]){
                        case"2":
                            Wall wall = new Wall(curCol*31, curRow*31,2); ///y is j
                            GameObjectsList.add(wall);
                            break;
                        case"3":
                        case"9": /// could remove this
                            Wall strongWall = new Wall(curCol*31, curRow*31,3); ///y is j
                            GameObjectsList.add(strongWall);
                            break;
                        case"4":
                            PowerUps powerUp = new PowerUps(curCol*31, curRow*31); ///y is j
                            GameObjectsList.add(powerUp);
                        default:
                            break;
                    }
                }
            }
         //   System.out.println(GameObjectsList.size());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        GameObjectsList.add(tankOne);
        GameObjectsList.add(tankTwo);

        TankControl tankOneControl = new TankControl(tankOne,
                KeyEvent.VK_W,
                KeyEvent.VK_S,
                KeyEvent.VK_A,
                KeyEvent.VK_D,
                KeyEvent.VK_SPACE);
        TankControl tankTwoControl = new TankControl(tankTwo,
                KeyEvent.VK_UP,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_ENTER);

        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        this.jFrame.addKeyListener(tankTwoControl);
        this.jFrame.addKeyListener(tankOneControl);
        this.jFrame.setSize(GameWorld.SCREEN_WIDTH, GameWorld.SCREEN_HEIGHT + 30);
        this.jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);
    }

//    public void checkCollision(){
//        for(int i = 0; i< GameObjectsList.size()-1; i++){
//            if((i!=0) && (tankOne.getRectangle().intersects(GameObjectsList.get(i).getRectangle()))){
//
//                Collision collision = new Collision(tankOne, GameObjectsList.get(i));
//            }
//        }
    public void checkCollision() {
/////TODO: make the repetive parts another method that gets passed the parts that change.

        //goes through each index in the gameobject arraylist
        for (int i = 0; i < GameObjectsList.size(); i++) {
            //checks if that objects active attribute is true. object on the map are active
            if (GameObjectsList.get(i).isActive()) {
                ///if the tank one intersects an object()
                if (i != GameObjectsList.size() - 2 && tankOne.getRectangle().intersects(GameObjectsList.get(i).getRectangle())) {
                    System.out.println(GameObjectsList.get(i));
                    if (GameObjectsList.get(i) instanceof Wall) {

                        tankOne.collision(GameObjectsList.get(i)); //what it collided with
                        GameObjectsList.get(i).collision(tankOne);
                    }
                    if (GameObjectsList.get(i) instanceof PowerUps) {
                        PowerUps myPowerUp = (PowerUps) GameObjectsList.get(i);
                        tankOne.collision(GameObjectsList.get(i)); //what it collided with
                        myPowerUp.collision(tankOne);
                    }
                    if (GameObjectsList.get(i) instanceof Tank) {
                        System.out.println("tank collided tank");
                        Tank myTank = (Tank) GameObjectsList.get(i);
                        tankOne.collision(GameObjectsList.get(i)); //what it collided with
                        myTank.collision(tankOne);
                    }

                }
                if (i != GameObjectsList.size() - 1 && tankTwo.getRectangle().intersects(GameObjectsList.get(i).getRectangle())) {
                    if (GameObjectsList.get(i) instanceof Wall) {
                        //     System.out.println("tank collided wall");
                        Wall myWall = (Wall) GameObjectsList.get(i);
                        tankTwo.collision(GameObjectsList.get(i)); //what it collided with
                        myWall.collision(tankTwo);
                    }
                    if (GameObjectsList.get(i) instanceof PowerUps) {
                        PowerUps myPowerUp = (PowerUps) GameObjectsList.get(i);
                        tankTwo.collision(GameObjectsList.get(i)); //what it collided with
                        myPowerUp.collision(tankTwo);
                    }

                }
                for (int b = 0; b < tankOne.bulletBarrage.size(); b++) {
                    if (tankOne.bulletBarrage.get(b).getRectangle().intersects(GameObjectsList.get(i).getRectangle())) {
                        if (GameObjectsList.get(i) instanceof Wall) {
                            tankOne.bulletBarrage.get(b).collision(GameObjectsList.get(i));
                            GameObjectsList.get(i).collision(tankOne.bulletBarrage.get(b));
                            tankOne.bulletBarrage.remove(b);
                        }
                        if (i != GameObjectsList.size() - 2 && GameObjectsList.get(i) instanceof Tank) {
                            tankOne.bulletBarrage.get(b).collision(GameObjectsList.get(i));
                            GameObjectsList.get(i).collision(tankOne.bulletBarrage.get(b));
                            tankOne.bulletBarrage.remove(b);
                        }
                    }

                }
                for (int b = 0; b < tankTwo.bulletBarrage.size(); b++) {
                    if (tankTwo.bulletBarrage.get(b).getRectangle().intersects(GameObjectsList.get(i).getRectangle())) {
                        if (GameObjectsList.get(i) instanceof Wall) {
                            tankTwo.bulletBarrage.get(b).collision(GameObjectsList.get(i));
                            GameObjectsList.get(i).collision(tankTwo.bulletBarrage.get(b));
                            tankTwo.bulletBarrage.remove(b);
                        }
                        if (i != GameObjectsList.size() - 1 && GameObjectsList.get(i) instanceof Tank) {
                            tankTwo.bulletBarrage.get(b).collision(GameObjectsList.get(i));
                            GameObjectsList.get(i).collision(tankTwo.bulletBarrage.get(b));
                            tankTwo.bulletBarrage.remove(b);
                        }
                    }
                }

            }
            else{
                GameObjectsList.remove(i);
            }
        }
    }
    public int[] checkFollow(){
        int t1CurScreenX,t1CurScreenY, t2CurScreenX, t2CurScreenY;
        int t1x = tankOne.getX();
        int t1y = tankOne.getY();
        int t2x = tankTwo.getX();
        int t2y = tankTwo.getY();
        if(t1x < SCREEN_WIDTH/4){
            t1CurScreenX = 0; }
        else if (t1x > WORLD_WIDTH - SCREEN_WIDTH/4){
            t1CurScreenX = WORLD_WIDTH - SCREEN_WIDTH/2; }
        else{ t1CurScreenX = t1x - SCREEN_WIDTH/4; }

        if(t2x < SCREEN_WIDTH/4){
            t2CurScreenX = 0; }
        else if (t2x > WORLD_WIDTH - SCREEN_WIDTH/4) {
            t2CurScreenX = WORLD_WIDTH - SCREEN_WIDTH/2; }
        else{ t2CurScreenX = t2x - SCREEN_WIDTH/4; }

        if(t1y < SCREEN_HEIGHT/2){
            t1CurScreenY = 0; }
        else if (t1y > WORLD_HEIGHT - SCREEN_HEIGHT/2){
            t1CurScreenY = WORLD_HEIGHT - SCREEN_HEIGHT; }
        else{t1CurScreenY = t1y - SCREEN_HEIGHT/2; }
//////fix it: tank two doesnt go to right most edge///////////////////////
        if(t2y < SCREEN_HEIGHT/2){
            t2CurScreenY = 0; }
        else if (t2y > WORLD_HEIGHT - SCREEN_HEIGHT/2) {
            t2CurScreenY = WORLD_HEIGHT - SCREEN_HEIGHT; }
        else{ t2CurScreenY = t2y - SCREEN_HEIGHT/2; }

        int screenPositions[] = {t1CurScreenX,t1CurScreenY,t2CurScreenX,t2CurScreenY};
        return screenPositions;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        buffer = world.createGraphics();

        this.background.drawImage(buffer);
        this.tankOne.drawImage(buffer);
        this.tankTwo.drawImage(buffer);

        for (int i = 0; i < this.GameObjectsList.size(); i++) {
            this.GameObjectsList.get(i).drawImage(this.buffer);
        }
        g2.fillRect(0,0,2000,2000);
        g2.setBackground(Color.black);

        int screenPosition[] = checkFollow();

        BufferedImage leftHalf= world.getSubimage(screenPosition[0], screenPosition[1], SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        BufferedImage rightHalf = world.getSubimage(screenPosition[2], screenPosition[3], SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        BufferedImage miniMap = world.getSubimage(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        g2.drawImage(leftHalf,0,40,null);
        g2.drawImage(rightHalf,GameWorld.SCREEN_WIDTH/2 + 5 ,40,null);
        tankOne.healthBar.drawImage(g2);
        tankTwo.healthBar.drawImage(g2);
        g2.scale(.1,.1);
//        if((tankOne.getX() < 250) && (tankOne.getY() < 250) ){
//            g2.drawImage(miniMap,(GameWorld.SCREEN_WIDTH/2 - 200) *10,20,null);}
//        else{g2.drawImage(miniMap,20,20,null);}
//
//        if((tankTwo.getX() < 250) && (tankTwo.getY() < 250) ){
//            g2.drawImage(miniMap,(GameWorld.SCREEN_WIDTH - 220) *10,20,null);}
//
//        else{
        g2.drawImage(miniMap,(GameWorld.SCREEN_WIDTH/2 - 105) *10,20,null);}



}
