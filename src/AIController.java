public class AIController implements Controller {
    public Rect rect;
    public Rect ball;
    private final GameManager gameManager;

    public AIController(Rect rect, Rect ball, GameManager gameManager) {
        this.rect = rect;
        this.ball = ball;
        this.gameManager = gameManager;
    }

    @Override
    public void update(double delta) {
        if (gameManager.isCounting()) return;
        if (ball.y < this.rect.y) {
            moveUp(delta);
        } else if (ball.y + ball.height > this.rect.y + this.rect.height) {
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
}
