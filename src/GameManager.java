public class GameManager {
    private final Text leftScoreText, rightScoreText;
    private Ball ball; // 이후 주입될 예정
    private boolean isCounting;
    private double countDownTimer;
    private String countDownText;
    private boolean isGameOver;

    public GameManager(Text leftScoreText, Text rightScoreText) {
        this.leftScoreText = leftScoreText;
        this.rightScoreText = rightScoreText;
        resetGame();
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void update(double delta) {
        // 시작 전 카운트
        if (isCounting) {
            countDownTimer -= delta;
            int seconds = (int) Math.ceil(countDownTimer);
            countDownText = String.valueOf(seconds);
            if (countDownTimer <= 0) {
                isCounting = false;
                resetBall();
            }
        }

        if (ball.rect.x < 0) {
            handleScore(rightScoreText, "Right");
            startCountDown();
        }
        else if (ball.rect.x + ball.rect.width > Constants.SCREEN_WIDTH) {
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
        ball.rect.x = Constants.SCREEN_WIDTH / 2.0;
        ball.rect.y = Constants.SCREEN_HEIGHT / 2.0;
        ball.resetVelocity();
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
        } else {
            resetBall();
        }
    }

    public boolean isCounting() {
        return isCounting;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public String getCountDownText() {
        return countDownText;
    }
}