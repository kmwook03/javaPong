public class GameManager {
    private Ball ball;
    private Rect leftPaddle, rightPaddle;
    private Text leftScoreText, rightScoreText;
    private double countDownTimer = 3.5;
    public boolean isCounting = true;

    public GameManager(Ball ball, Rect leftPaddle, Rect rightPaddle, Text leftScoreText, Text rightScoreText) {
        this.ball = ball;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.leftScoreText = leftScoreText;
        this.rightScoreText = rightScoreText;
    }

    public void update(double delta) {
        // 시작 전 카운트
        if (isCounting) {
            countDownTimer -= delta;
            if (countDownTimer <= -0.5) {
                isCounting = false;
                Main.state = Main.GameState.PLAYING;
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
    }

    private void handleScore(Text scoreText, String winnerName) {
        int currentScore = Integer.parseInt(scoreText.text) + 1;
        scoreText.text = "" + currentScore;

        if (currentScore >= Constants.WIN_SCORE) {
            System.out.println(winnerName + " win");
            Main.changeState(Main.GameState.MAIN_MENU);
        } else {
            resetBall();
        }
    }

    public String getCountDownText() {
        int seconds = (int)Math.ceil(countDownTimer);
        if (seconds > 0) {
            return "" + seconds;
        }
        return "Start";
    }
}