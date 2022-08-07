import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Random;

public class Ball{

    public int size;
    public int x, y;
    public int speed;
    public boolean left, up;
    public int points;
    public int toLose;
    
    GamePanel gp;

    public Ball(GamePanel gp){

        this.gp = gp;
        setDefault();

    }


    public Ball(Ball b) {

        copyBall(b);
        
    }


    public void copyBall(Ball b){

        gp = b.gp;
        size = b.size;
        up = b.up;
        left = b.left;
        x = b.x;
        y = b.y + b.size + 10;
        speed = new Random().nextInt(2)+4;
        points = b.points;
        toLose = b.toLose;

    }


    public void setDefault(){
        
        Random rand = new Random();
        x = rand.nextInt(gp.screenWidth-gp.player.buffer-gp.player.width2-size);
        size = 20;

        //x = (gp.screenWidth/2) - (size/2);
        y = (gp.screenHeight/2) - (size/2);
        //y = gp.screenHeight-(gp.player.y1+gp.player.height1);
        //y = gp.player.y1;
        speed = 5;
        points = 5;
        toLose = 1;

        //left = true;
        //up = false;

        
        int temp = rand.nextInt(10);

        if (temp % 2 == 0){
            left = true;
            if (temp % 3 == 0)
                up = true;
            else
                up = false;
            
        }else{
            left = false;
            if (temp % 3 == 1)
                up = true;
            else
                up = false;
        }

    }


    public void update(){

        int temp = new Random().nextInt(4);

        if (y <= gp.player.y1 + gp.player.height1 && ((x >= gp.player.x1 && (x <= gp.player.x1 + gp.player.width1)) || (x + size >= gp.player.x1 && (x + size <= gp.player.x1 + gp.player.width1)))){
            up = false;
            gp.score += points;
            if (temp >= 2)
                left = !left;
        }
        if (y + size >= gp.screenHeight - (gp.player.y1 + gp.player.height1) && ((x >= gp.player.x1 && (x <= gp.player.x1 + gp.player.width1)) || (x + size >= gp.player.x1 && (x + size <= gp.player.x1 + gp.player.width1)))){
            up = true;
            gp.score += points;
            if (temp >= 2)
                left = !left;
        }
        if (x <= gp.player.x2 + gp.player.width2 && ((y >= gp.player.y2 && (y <= gp.player.y2 + gp.player.height2)) || (y + size >= gp.player.y2 && (y + size <= gp.player.y2 + gp.player.height2)))){
            left = false;
            gp.score += points;
            if (temp >= 2)
                up = !up;
        }
        if (x + size >= gp.screenWidth - (gp.player.x2 + gp.player.width2) && ((y >= gp.player.y2 && (y <= gp.player.y2 + gp.player.height2) || (y + size >= gp.player.y2 && (y + size <= gp.player.y2 + gp.player.height2))))){
            left = true;
            gp.score += points;
            if (temp >= 3)
                up = !up;
        }

        if (y <= gp.player.y1){
            up = false;
            gp.lives -= toLose;;
            if (temp >= 2)
                left = !left;
        }
        if (y >= gp.screenHeight - (gp.player.y1 + gp.player.height1)){ 
            up = true;
            gp.lives -= toLose;
            if (temp >= 2)
                left = !left;
        }
        if (x <= gp.player.x2){
            left = false;
            gp.lives -= toLose;
            if (temp >= 2)
                up = !up;
        }
        if (x >= gp.screenWidth - (gp.player.x2 + gp.player.width2)){
            left = true;
            gp.lives -= toLose;
            if (temp >= 2)
                up = !up;
        }
        
        if (up)
            y -= speed;
        else
            y += speed;

        if (left)
            x -= speed;
        else
            x += speed;
        
    }

    public void draw(Graphics2D g2){

        g2.setColor(Color.red);
        g2.fillRect(x, y, size, size);

    }


}