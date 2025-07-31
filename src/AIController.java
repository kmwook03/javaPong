public class AIController implements Controller {
    public Rect rect;
    public Rect ball;
    private final GameManager gameManager;
    private double verticalDirection = 0.0;

    public AIController(Rect rect, Rect ball, GameManager gameManager) {
        this.rect = rect;
        this.ball = ball;
        this.gameManager = gameManager;
    }

    @Override
    public void reset() {
        this.rect.setY(Constants.SCREEN_HEIGHT / 2.0);
    }

    @Override
    public void update(double delta) {
        if (gameManager.isCounting()) {
            this.verticalDirection = 0.0;
            return;
        }

        if (ball.getY() < this.rect.getY()) {
            this.verticalDirection = -1.0;
        } else if (ball.getY() + ball.getHeight() > this.rect.getY() + this.rect.getHeight()) {
            this.verticalDirection = 1.0;
        } else {
            this.verticalDirection = 0.0;
        }

        if (this.verticalDirection < 0) {
            moveUp(delta);
        } else if (this.verticalDirection > 0) {
            moveDown(delta);
        }
    }

    @Override
    public boolean isTryingToSmash() {
        return false;
    }

    public void moveUp(double delta) {
        if (rect.getY() - Constants.PADDLE_SPEED * delta > 0) {
            this.rect.setY(this.rect.getY() - Constants.PADDLE_SPEED * delta);
        }
    }

    public void moveDown(double delta) {
        if ((rect.getY() + Constants.PADDLE_SPEED * delta) + rect.getHeight() < Constants.SCREEN_HEIGHT) {
            this.rect.setY(this.rect.getY() + Constants.PADDLE_SPEED * delta);
        }
    }

    @Override
    public double getVerticalDirection() {
        return this.verticalDirection;
    }
}
