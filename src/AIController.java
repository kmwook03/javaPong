public class AIController {
    public PlayerController playerController;
    public Rect ball;

    public AIController(PlayerController plrController, Rect ball) {
        this.playerController = plrController;
        this.ball = ball;
    }

    public void update(double delta) {
        playerController.update(delta);

        if (ball.y < playerController.rect.y) {
            playerController.moveUp(delta);
        } else if (ball.y + ball.height > playerController.rect.y + playerController.rect.height) {
            playerController.moveDown(delta);
        }
    }
}
