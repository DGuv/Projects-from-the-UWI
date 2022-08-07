import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Font;
import java.util.Random;
import java.awt.geom.Area;
import java.awt.Shape;
import java.awt.Rectangle;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16;
    final int scale = 3;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 16;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;
    double drawInterval = 1000000000/FPS;

    Thread gameThread;
    KeyHandler handler = new KeyHandler();

    public int numBalls;

    // TEMPORARY

    int playerX = (screenWidth/2)-(tileSize/2), playerY = (screenHeight/2)-(tileSize/2);
    int playerSpeed = 4;

    boolean collision = false;

    boolean up=true, left=true;

    public Player player = new Player(this, handler);

    public int lives = 5;
    public int score = 0;
    
    public int seconds = 0, minutes = 0;

    public Timer timer;

    public int gameCounter=0;

    public ArrayList<Ball> balls = new ArrayList<Ball>();
    public ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();

    public Timer doublePoints;
    public int dptsCounter=0;
    public boolean doubled = false;
    public String currUpgrades = "";

    public Timer magentaTimer;
    public int magentaCounter=0;

    public boolean mag = false;
    public boolean revert = false;
    public boolean flag = false;

    public int ballLimit = 6;

    public int totalLivesCollected=0;

    public boolean flag2 = false;

    public boolean flag3 = false;
    
    

    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(handler);
        this.setFocusable(true);
        startGameThread();

    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {
        
        timer = new Timer();
        double nextDraw = System.nanoTime() + drawInterval;
        balls.add(new Ball(this));
        numBalls = 1;
        Random rand = new Random();
        int seed;
        
        while(gameThread != null && lives > 0){
            
            seed = rand.nextInt(1000);
            spawnUpgrade(seed);
            
            update();       
            repaint();
            
            try {
                
                double timeRemaining = (nextDraw - System.nanoTime()) / 1000000;
                
                if (timeRemaining < 0)
                    timeRemaining = 0;
            
                Thread.sleep((long) timeRemaining);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

            nextDraw += drawInterval;
            gameCounter++;
        }

    }


    public void spawnUpgrade(int seed) {

        if (seed == 21 || seed == 22 || seed == 69 || seed == 420) {

            upgrades.add(new Upgrade(this));

        }

    }


    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        Font font = new Font("Verdana", Font.BOLD, 14);
        g2.setFont(font);
        g2.setColor(Color.white);
        g2.drawString("Lives: " + lives, 30, 15);
        g2.drawString("Score: " + score, screenWidth - 120, 15);

        if (seconds < 10)
            g2.drawString(minutes + ":0" + seconds, (screenWidth / 2) - 20, 15);
        else
            g2.drawString(minutes + ":" + seconds, (screenWidth / 2) - 20, 15);

        player.draw(g2);
        for (Ball b : balls) {
            b.draw(g2);
        }
        for (Upgrade u : upgrades) {
            u.draw(g2);
        }

        g2.setColor(Color.white);
        g2.drawString("Current Upgrades: " + currUpgrades, 30, screenHeight - (player.buffer - player.height1));
        // ball.draw(g2);
        g2.dispose();

    }


    public void update() {

        player.update();
        for (Ball b : balls) {

            b.update();

        }
        for (Upgrade u : upgrades) {
            u.update();
        }
        
        upgradeCollision();
        newBallCollision();
        despawnUpgrade();
        currUpgrades = "";
        upgradeStringMaker();
        
        
        if (timer.hasSecondPassed())
            if (seconds < 59)
                seconds++;
            else if (seconds >= 59){
                seconds = 0;
                minutes++;
            }
       
        if (magentaTimer != null)
            if (magentaCounter < 30) {

                if (magentaTimer.hasSecondPassed())
                    magentaCounter++;
            } else {
                magentaCounter = 0;
                magentaTimer = null;
                mag = false;
            }

        

        if (doublePoints != null)
        
            if (dptsCounter < 30){
                if (doublePoints.hasSecondPassed())
                    dptsCounter++;
            }else{
                dptsCounter = 0;
                doublePoints = null;
                doubled = false;
            }

        if (!mag && !doubled) {

            if (balls.get(0).points == 10) {
                for (Ball b : balls) {
                    b.points = 5;
                }
            }
        }
        
        if (revert){

            for (Ball b: balls){

                b.toLose = 1;
                b.speed /= 2;

            }

            revert = false;

        }   
        

    }


    public void despawnUpgrade(){
        ArrayList<Upgrade> toRemove =  new ArrayList<Upgrade>();
        for (Upgrade u: upgrades){

            if (u.y + u.size >= screenHeight - (player.y1 + player.height1)){

                toRemove.add(u);

            }

        }
        
        if (!toRemove.isEmpty()) {
            upgrades.removeAll(toRemove);
            toRemove.clear();
        }

    }

    // The credit for the following method goes to Nitbit25 @:
    // https://www.codegrepper.com/code-examples/java/how+to+see+if+a+shape+is+touching+another+shape+in+java
    public static boolean testIntersection(Shape shapeA, Shape shapeB) {
        Area areaA = new Area(shapeA);
        areaA.intersect(new Area(shapeB));
        return !areaA.isEmpty();
    }


    public void upgradeCollision(){

        ArrayList<Upgrade> toRemove = new ArrayList<Upgrade>();
        int bball = 0;
        int temp = 0;
        for (Ball b: balls){
            for (Upgrade u: upgrades){
                if (GamePanel.testIntersection(new Rectangle(u.x,u.y,u.size,u.size), new Rectangle(b.x, b.y, b.size, b.size))){
                    toRemove.add(u);
                    temp = activateUpgrade(u, b);
                    if (temp == 1)
                        bball++;
                    if (temp == 4)
                        bball = numBalls;
                    
                }

            }
            
            if (!toRemove.isEmpty()){
                upgrades.removeAll(toRemove);
                toRemove.clear();
            }
            
        }
        if(numBalls < ballLimit+1){
            for (int i=0; i<bball; i++){
                
                numBalls++;
                if (numBalls >= ballLimit+1){
                    bball = numBalls - ballLimit;
                    numBalls = ballLimit;
                    score += bball * 100;
                    break; 
                }else{
                    balls.add(new Ball(balls.get(i)));
                }
            }
        }
        else{
            bball = numBalls - ballLimit;
            numBalls = ballLimit;
            score += bball * 100;
        }
        bball = 0;
        
        if (mag && !flag){
            for (Ball b1 : balls) {
                b1.toLose = 0;
                b1.speed *= 2;
                
                if (b1.speed > 10)
                    b1.speed = 10;
                
                b1.points = 10;
            }
            flag = true;
        }


    }


    public int activateUpgrade(Upgrade u, Ball b){

        if (u.type == 1){
            if (lives < 20)
                lives++;
            else{
                score += 10;
            }
            
            totalLivesCollected++;
            return 0;

        }

        if (u.type == 2){

            return 1;

        }

        if (u.type == 3){

            doublePoints = new Timer();
            dptsCounter = 0;
            doubled = true;

            flag2 = false;
            //flag3 = true;
            
            for (Ball b1: balls){

                b1.points = 10;

            }

            return 2;

        }

        if (u.type == 4){

            b.speed++;
            if (b.speed > 10){
                score += 20;
                b.speed = 10;
            }
            return 3;

        }

        if (u.type == 5){

            magentaTimer = new Timer();
            magentaCounter = 0;
            
            mag = true;
            flag = false;
            return 4;

         }

         return -999;

    }

    public void newBallCollision(){
        
        if (mag)
            return;

        if (numBalls > 1){
            Ball rem = null;
            boolean coll = false;
            
            for (Ball b: balls){

                if (b.x <= player.x2 || b.x >= screenWidth - (player.x2 + player.width2)  || b.y <= player.y1 || b.y >= screenHeight - (player.y1 + player.height1)){

                    coll = true;
                    rem = b;

                }

            }

            if (coll){
                balls.remove(rem);
                numBalls--;
            }

        } 

    }
    
    public void upgradeStringMaker() {
        if (mag){
            currUpgrades += "Magenta timer: " + (30 - magentaCounter) + ", ";
            if (magentaCounter == 30){
                //doubled = false;
                mag = false;
                revert = true;
                magentaCounter = 0;
            }
        }
        else if (doubled){
            currUpgrades += "Dbl pts timer: " + (30 - dptsCounter) + ", ";
            if (dptsCounter == 30){
                doubled = false;
                //flag3 = false;
                dptsCounter = 0;
            }
                
        }
        
        currUpgrades += "Ball counter: " + numBalls + ", Extra lives collected: " + totalLivesCollected;

    }

}
