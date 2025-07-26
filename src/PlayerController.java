import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController implements Controller {
    public Rect rect;
    public KL keyListener;

    private int dashCount = 3;
    public PlayerController(Rect rect, KL keyListener) {
        this.rect = rect;
        this.keyListener = keyListener;
    }

    @Override
    public void update(double delta) {
        if (keyListener != null) { // Player 조작
            // 이동 로직
            if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                moveDown(delta);
            } else if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
                moveUp(delta);
            }
        }
    }

    public boolean isTryingToSmash() {
        return keyListener.isKeyPressed(KeyEvent.VK_Z);
    }

    public void moveUp(double delta) {
        if (rect.y - Constants.PADDLE_SPEED * delta > Constants.TOOLBAR_HEIGHT) {
            if (keyListener.isKeyPressedOnce(KeyEvent.VK_X) && dashCount > 0) { // 대시
                this.rect.y -= Constants.DASH_DISTANCE;
                System.out.println("Dash!");
                dashCount--;
            } else {
                this.rect.y -= Constants.PADDLE_SPEED * delta;
            }
        }
    }

    public void moveDown(double delta) {
        if ((rect.y + Constants.PADDLE_SPEED * delta) + rect.height < Constants.SCREEN_HEIGHT - Constants.INSETS_BOTTOM) {
            if (keyListener.isKeyPressedOnce(KeyEvent.VK_X) && dashCount > 0) { // 대시
                this.rect.y += Constants.DASH_DISTANCE;
                System.out.println("Dash!");
                dashCount--;
            } else {
                this.rect.y += Constants.PADDLE_SPEED * delta;
            }
        }
    }
}
