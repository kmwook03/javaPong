import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KL implements KeyListener {
    private boolean[] keyPressed = new boolean[128]; // ASCII 코드 개수 만큼
    private boolean[] prevKeyPressed = new boolean[128]; // 이전 키 상태 저장

    public void update() {
        // 현재 키 상태를 이전 키 상태로 복사
        System.arraycopy(this.keyPressed, 0, this.prevKeyPressed, 0, this.keyPressed.length);
    }

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
        if (keyCode < keyPressed.length) {
            return keyPressed[keyCode];
        }
        return false;
    }

    public boolean isKeyPressedOnce(int keyCode) {
        return this.keyPressed[keyCode] && !this.prevKeyPressed[keyCode];
    }
}
