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

    private boolean isSmashing = false;
    private double smashTime = 0.0;
    private double originalX;
    private double smashCoolTime = 0.0;

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
        this.isSmashing = false;
        this.smashTime = 0.0;
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

        if (smashCoolTime > 0.0) {
            smashCoolTime -= delta;
            if (smashCoolTime < 0.0) {
                smashCoolTime = 0.0;
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
            // 스매시 로직
            if (keyListener.isKeyPressed(KeyEvent.VK_Z) && !isSmashing && smashCoolTime == 0.0) {
                isSmashing = true;
                smashTime = Constants.SMASH_DURATION;
                originalX = this.rect.getX();
                smashCoolTime = 3.0;
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

            if (isSmashing) {
                smash(delta);
            }
        }
    }

    @Override
    public boolean isTryingToSmash() {
        System.out.println("Smash!");
        return isSmashing;
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

    public void smash(double delta) {
        if (smashTime > 0) {
            double halfSmashTime = Constants.SMASH_DURATION / 2.0;
            double progress;
            if (smashTime > halfSmashTime) {
                progress = (Constants.SMASH_DURATION - smashTime) / halfSmashTime;
            } else {
                progress = smashTime / halfSmashTime;
            }
            this.rect.setX(originalX + Constants.SMASH_DISTANCE * progress);
            smashTime -= delta;
        } else {
            isSmashing = false;
            this.rect.setX(originalX);
        }
    }
}
