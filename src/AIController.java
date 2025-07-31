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
        this.rect.y = Constants.SCREEN_HEIGHT / 2.0;
    }

    @Override
    public void update(double delta) {
        if (gameManager.isCounting()) {
            this.verticalDirection = 0.0;
            return;
        }

        if (ball.y < this.rect.y) {
            this.verticalDirection = -1.0;
        } else if (ball.y + ball.height > this.rect.y + this.rect.height) {
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

    @Override
    public double getVerticalDirection() {
        return this.verticalDirection;
    }
}
