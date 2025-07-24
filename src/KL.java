import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KL implements KeyListener {
    private boolean keyPressed[] = new boolean[128]; // ASCII 코드 개수 만큼

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true; // 유저가 누르면 true가 됨
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false; // 떼면 false 됨
    }

    public boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }
}
