import java.awt.event.KeyEvent;

public class PlayerController implements Controller {
    public Rect rect;
    public KL keyListener;
    private final GameManager gameManager;

    private boolean isDashing = false;
    private double dashTime = 0.0;
    private double dashDirection = 0.0;
    private double dashCoolTime = 0.0;
    private double verticalDirection = 0.0;

    public PlayerController(Rect rect, KL keyListener, GameManager gameManager) {
        this.rect = rect;
        this.keyListener = keyListener;
        this.gameManager = gameManager;
    }

    @Override
    public void reset() {
        this.rect.setY(Constants.SCREEN_HEIGHT / 2.0);
        this.isDashing = false;
        this.verticalDirection = 0.0;
    }

    @Override
    public void update(double delta) {
        if (gameManager.isCounting()) return;

        if (dashCoolTime > 0.0) {
            dashCoolTime -= delta;
            if (dashCoolTime < 0.0) { // 쿨타임 음수 방지
                dashCoolTime = 0.0;
            }
        }

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
            if (keyListener.isKeyPressed(KeyEvent.VK_X) && !isDashing && dashCoolTime == 0.0) {
                if (this.verticalDirection != 0) {
                    isDashing = true;
                    dashTime = 0.2;
                    dashDirection = this.verticalDirection;
                    dashCoolTime = 3.0;
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

    @Override
    public boolean isTryingToSmash() {
        System.out.println("Smash!");
        return keyListener.isKeyPressed(KeyEvent.VK_Z);
    }

    @Override
    public double getVerticalDirection() {
        return this.verticalDirection;
    }

    public void moveUp(double delta) {
        if (rect.getY() - Constants.PADDLE_SPEED * delta > 0) {
            double currentY = this.rect.getY() - Constants.PADDLE_SPEED * delta;
            this.rect.setY(currentY);
        }
    }

    public void moveDown(double delta) {
        if ((rect.getY() + Constants.PADDLE_SPEED * delta) + rect.getHeight() < Constants.SCREEN_HEIGHT) {
            double currentY = this.rect.getY() + Constants.PADDLE_SPEED * delta;
            this.rect.setY(currentY);
        }
    }

    public void dash(double delta) {
        if (dashTime > 0) {
            double moveAmount = Constants.DASH_SPEED * delta * dashDirection;
            if (dashDirection < 0) { // 위로 대시
                if (this.rect.getY() + moveAmount > 0) {
                    this.rect.setY(this.rect.getY() + moveAmount);
                } else {
                    this.rect.setY(0.0);
                }
            } else { // 아래로 대시
                if (this.rect.getY() + this.rect.getHeight() + moveAmount < Constants.SCREEN_HEIGHT) {
                    this.rect.setY(this.rect.getY() + moveAmount);
                } else {
                    this.rect.setY(Constants.SCREEN_HEIGHT - this.rect.getHeight());
                }
            }
            System.out.println("Dash!");
            dashTime -= delta;
        } else {
            isDashing = false;
        }
    }
}
