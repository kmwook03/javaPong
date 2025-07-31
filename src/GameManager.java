import javax.swing.*;

public class GameManager {
    private PlayerController playerController;
    private AIController aiController;
    private final Text leftScoreText, rightScoreText;
    private Ball ball; // 이후 주입될 예정
    private boolean isCounting;
    private double countDownTimer;
    private String countDownText;
    private boolean isGameOver;
    private final Runnable onGameOver;

    public GameManager(Text leftScoreText, Text rightScoreText, Runnable onGameOver) {
        this.leftScoreText = leftScoreText;
        this.rightScoreText = rightScoreText;
        this.onGameOver = onGameOver;
        resetGame();
    }

    public void setController(PlayerController playerController, AIController aiController) {
        this.playerController = playerController;
        this.aiController = aiController;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void update(double delta) {
        if (isGameOver) return;
        // 시작 전 카운트
        if (isCounting) {
            resetController();
            resetBall();
            countDownTimer -= delta;
            int seconds = (int) Math.ceil(countDownTimer);
            countDownText = String.valueOf(seconds);
            if (countDownTimer <= 0) {
                isCounting = false;
            }
        }

        if (ball.rect.getX() < 0) {
            handleScore(rightScoreText, "Right");
            startCountDown();
        }
        else if (ball.rect.getX() + ball.rect.getWidth() > Constants.SCREEN_WIDTH) {
            handleScore(leftScoreText, "Left");
            startCountDown();
        }
    }

    private void startCountDown() {
        isCounting = true;
        countDownTimer = 3.0;
        countDownText = "3";
    }

    private void resetBall() {
        if (ball != null) {
            ball.rect.setX(Constants.SCREEN_WIDTH / 2.0);
            ball.rect.setY(Constants.SCREEN_HEIGHT / 2.0);
            ball.resetVelocity();
        }
    }

    private void resetController() {
        if (playerController != null) {
            playerController.reset();
        }
        if (aiController != null) {
            aiController.reset();
        }
    }

    public void resetGame() {
        leftScoreText.text = "0";
        rightScoreText.text = "0";
        isGameOver = false;

        startCountDown();
    }

    private void handleScore(Text scoreText, String winnerName) {
        int currentScore = Integer.parseInt(scoreText.text) + 1;
        scoreText.text = "" + currentScore;

        if (currentScore >= Constants.WIN_SCORE) {
            System.out.println(winnerName + " win");
            isGameOver = true;
            if (onGameOver != null) {
                SwingUtilities.invokeLater(onGameOver);
            }
        } else {
            resetBall();
        }
    }

    public boolean isCounting() {
        return isCounting;
    }

    public String getCountDownText() {
        return countDownText;
    }
}