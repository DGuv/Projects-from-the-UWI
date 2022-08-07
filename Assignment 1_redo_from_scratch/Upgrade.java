import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;


public class Upgrade {
    public int type;
    public Color color;
    public int x, y;
    public int speed;
    public GamePanel gp;
    public int size;
    
    public Upgrade(GamePanel gp){

        this.gp = gp;
        setDefault();

    }

    public void setDefault(){
        
        Random rand = new Random();
        int temp = rand.nextInt(10);

        // 0, 1, 2 = extra life - green
        // 3 = extra ball - cyan
        // 4, 5 = double points for 30 seconds - yellow
        // 6, 7, 8 = faster ball - blue
        // 9 = lose no lives + double balls + double points + double ball speed for 30 seconds - magenta

        if (temp < 3){

            color = Color.green;
            type = 1;
            speed = 4;

        }

        // if one of the balls hit the wall it disappears
        else if (temp < 4){

            color = Color.cyan;
            type = 2;
            speed = 2;

        }

        else if (temp < 6){

            color = Color.yellow;
            type = 3;
            speed = 3;

        }

        else if (temp < 9){

            color = Color.blue;
            type = 4;
            speed = 4;

        }
        
        else{

            color = Color.magenta;
            type = 5;
            speed = 2;

        }

        x = rand.nextInt(gp.screenWidth - ((gp.player.x2+gp.player.width2)*2) - gp.player.width1) + (gp.player.x2 + gp.player.width2);
        y = gp.player.y1+gp.player.height1+5;
        size = 15;

    }

    public void draw(Graphics2D g2){

        g2.setColor(color);
        g2.fillOval(x, y, size, size);

    }


    public void update(){

        if (gp.gameCounter % 4 == 0){

            y +=speed;

        }

    }

}
