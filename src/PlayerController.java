import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController implements Controller {
    public Rect rect;
    public KL keyListener;
    private final GameManager gameManager;

    private boolean isDashing = false;
    private double dashTime = 0.0;
    private double dashDirection = 0.0;
    private double verticalDirection = 0.0;

    public PlayerController(Rect rect, KL keyListener, GameManager gameManager) {
        this.rect = rect;
        this.keyListener = keyListener;
        this.gameManager = gameManager;
    }

    @Override
    public void reset() {
        this.rect.y = Constants.SCREEN_HEIGHT / 2.0;
        this.isDashing = false;
        this.verticalDirection = 0.0;
    }

    @Override
    public void update(double delta) {
        if (gameManager.isCounting()) return;
        if (keyListener != null) { // Player 조작
            // 패들 이동 방향 플래그 설정
            if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
                this.verticalDirection = -1.0;
            } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                this.verticalDirection = 1.0;
            } else {
                this.verticalDirection = 0.0;
            }
            // 대시 로직
            if (keyListener.isKeyPressed(KeyEvent.VK_X) && !isDashing) {
                if (this.verticalDirection != 0) {
                    isDashing = true;
                    dashTime = 0.2;
                    dashDirection = this.verticalDirection;
                }
            }

            if (isDashing) {
                dash(delta);
            } else {
                if (this.verticalDirection > 0) {
                    moveDown(delta);
                } else if (this.verticalDirection < 0) {
                    moveUp(delta);
                }
            }
        }
    }

    public boolean isTryingToSmash() {
        return keyListener.isKeyPressed(KeyEvent.VK_Z);
    }

    @Override
    public double getVerticalDirection() {
        return this.verticalDirection;
    }

    public void moveUp(double delta) {
        if (rect.y - Constants.PADDLE_SPEED * delta > 0) {
            this.rect.y -= Constants.PADDLE_SPEED * delta;
        }
    }

    public void moveDown(double delta) {
        if ((rect.y + Constants.PADDLE_SPEED * delta) + rect.height < Constants.SCREEN_HEIGHT) {
            this.rect.y += Constants.PADDLE_SPEED * delta;
        }
    }

    public void dash(double delta) {
        if (dashTime > 0) {
            double moveAmount = Constants.DASH_SPEED * delta * dashDirection;
            if (dashDirection < 0) { // 위로 대시
                if (this.rect.y + moveAmount > 0) {
                    this.rect.y += moveAmount;
                } else {
                    this.rect.y = 0;
                }
            } else { // 아래로 대시
                if (this.rect.y + this.rect.height + moveAmount < Constants.SCREEN_HEIGHT) {
                    this.rect.y += moveAmount;
                } else {
                    this.rect.y = Constants.SCREEN_HEIGHT - this.rect.height;
                }
            }
            dashTime -= delta;
            System.out.println("Dash!");
        } else {
            isDashing = false;
        }
    }
}
