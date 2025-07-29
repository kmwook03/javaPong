public class GameManager {
    private Ball ball; // 이후 주입될 예정
    private final Text leftScoreText, rightScoreText;
    private double countDownTimer = 3.0;
    private boolean isCounting = true;
    private boolean isGameOver = false;

    public GameManager(Text leftScoreText, Text rightScoreText) {
        this.leftScoreText = leftScoreText;
        this.rightScoreText = rightScoreText;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void update(double delta) {
        // 시작 전 카운트
        if (isCounting) {
            countDownTimer -= delta;
            if (countDownTimer <= -0.5) {
                isCounting = false;
            }
            return;
        }

        if (ball.rect.x < 0) handleScore(rightScoreText, "Right");
        else if (ball.rect.x + ball.rect.width > Constants.SCREEN_WIDTH) handleScore(leftScoreText, "Left");
    }

    private void resetBall() {
        this.ball.rect.x = Constants.SCREEN_WIDTH / 2.0;
        this.ball.rect.y = Constants.SCREEN_HEIGHT / 2.0;
        ball.resetVelocity();
        isCounting = true;
        countDownTimer = 3.0;
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
        int seconds = (int)Math.ceil(countDownTimer);
        if (seconds > 0) {
            return "" + seconds;
        }
        return "Start";
    }
}