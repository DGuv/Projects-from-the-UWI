import java.awt.Graphics2D;
import java.util.Random;
import java.awt.Color;

public class Player {
    
    public int width1, width2;
    public int height1, height2;
    public int x1, x2;
    public int y1, y2;
    public int speed;
    public boolean collision;

    int buffer = 30;
    
    GamePanel gp;
    KeyHandler handler;

    public Player(GamePanel gp, KeyHandler handler) {

        this.gp = gp;
        this.handler = handler;

        setDefault();
        
    }

    public void setDefault() {

        speed = 6;
        
        width1 = 120;
        height1 = 20;
        width2 = height1;
        height2 = width1;
        Random rand = new Random();
        // x1 = (gp.screenWidth/2)-(width1/2);
        // y1 = 10;
        //x1 = gp.screenWidth-(buffer+width2+width1);
        x1 = rand.nextInt(gp.screenWidth - ((buffer+width2)*2) - width1) + (buffer+width2);
        y1 = 30;

        
        x2 = 30;
        //y2 = (gp.screenHeight / 2) - (height2 / 2);
        y2 = rand.nextInt(gp.screenHeight- ((buffer + height1) * 2) - height2)  + (buffer + height1);
        //y2 = gp.screenHeight - (buffer+height1+height2);
        collision = false;
        
    }

    public void draw(Graphics2D g2){

        g2.setColor(Color.white);
        
        g2.fillRect(x1, y1, width1, height1);
        g2.fillRect(x1, gp.screenHeight-(y1+height1), width1, height1);

        g2.fillRect(x2, y2, width2, height2);
        g2.fillRect(gp.screenWidth - (x2+width2), y2 , width2, height2);

    }


    public void update(){

        if (handler.leftPressed == true){
            if (x1 - speed - width2 >= speed+buffer)
                x1 -= speed;
            else   
                x1 = height1 + buffer;
        }

        if (handler.rightPressed == true){
            if (x1 + width1 + speed < gp.screenWidth - (x2+width2))
                x1 += speed;
            else
                x1 = gp.screenWidth-width1-(x2+width2);
        }

        if (handler.upPressed == true){
            if (y2 - speed - width2 >= speed+buffer)
                y2 -= speed;
            else
                y2 = width2 + buffer;
            
        }

        if (handler.downPressed == true){
            if (y2 + height1 + speed < gp.screenHeight - height2 - buffer)
                y2 += speed;
            else 
                y2 = gp.screenHeight - height2 - buffer - height1;
            
        } 
    


    }

}
