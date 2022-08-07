import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener{

    public boolean upPressed = false;
    public boolean downPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;

    @Override
    public void keyTyped(KeyEvent e) {
        
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            
            leftPressed = true;

        }

        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {

            rightPressed = true;

        }

        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {

            upPressed = true;

        }

        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {

            downPressed = true;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {

            leftPressed = false;

        }

        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {

            rightPressed = false;

        }

        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {

            upPressed = false;

        }

        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {

            downPressed = false;

        }
        
    }
   
}