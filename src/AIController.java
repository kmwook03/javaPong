public class AIController implements Controller {
    public Rect rect;
    public Rect ball;

    public AIController(Rect rect, Rect ball) {
        this.rect = rect;
        this.ball = ball;
    }

    @Override
    public void update(double delta) {
        if (ball.y < this.rect.y) {
            moveUp(delta);
        } else if (ball.y + ball.height > this.rect.y + this.rect.height) {
            moveDown(delta);
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
