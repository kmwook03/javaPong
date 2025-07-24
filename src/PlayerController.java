import java.awt.event.KeyEvent;

public class PlayerController {
    public Rect rect;
    public KL keyListener;
    public PlayerController(Rect rect, KL keyListener) { // keyListener가 존재하면 Player
        this.rect = rect;
        this.keyListener = keyListener;
    }

    public PlayerController(Rect rect) { // 없으면 AI
        this.rect = rect;
        this.keyListener = null;
    }

    public void update(double delta) {
        if (keyListener != null) { // Player 조작
            if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                moveDown(delta);
            } else if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
                moveUp(delta);
            }
        }
    }

    public void moveUp(double delta) {
        if (rect.y - Constants.PADDLE_SPEED * delta > Constants.TOOLBAR_HEIGHT) {
            this.rect.y -= Constants.PADDLE_SPEED * delta;
        }
    }

    public void moveDown(double delta) {
        if ((rect.y + Constants.PADDLE_SPEED * delta) + rect.height < Constants.SCREEN_HEIGHT - Constants.INSETS_BOTTOM) {
            this.rect.y += Constants.PADDLE_SPEED * delta;
        }
    }
}
