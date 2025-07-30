import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable, IPanel {

    private Thread gameThread;
    private final Runnable onGameOver;
    private double lastFrameTime;

    private final KL keyListener = new KL();
    private final Rect playerOne;
    private final Rect ai;
    private final Ball ball;
    private final PlayerController playerController;
    private final AIController aiController;
    private final Text leftScoreText;
    private final Text rightScoreText;
    private final GameManager gameManager;

    public GamePanel(Runnable onGameOverCallback) {
        this.onGameOver = onGameOverCallback;

        // 패널 설정
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setFocusable(true);

        // 이벤트 리스너 추가
        this.addKeyListener(keyListener);

        // 게임 객체 생성
        playerOne = new Rect(Constants.HZ_PADDING, Constants.SCREEN_HEIGHT / 2.0, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);
        ai = new Rect(Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH - Constants.HZ_PADDING, Constants.SCREEN_HEIGHT / 2.0, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);
        Rect ballRect = new Rect(Constants.SCREEN_WIDTH/2.0, Constants.SCREEN_HEIGHT/2.0, Constants.BALL_WIDTH, Constants.BALL_HEIGHT, Constants.PADDLE_COLOR);
        // 점수 표시 텍스트 객체 생성
        leftScoreText = new Text(0, new Font("Times New Roman", Font.PLAIN, Constants.TEXT_SIZE),
                Constants.TEXT_X_POS, Constants.TEXT_Y_POS);
        rightScoreText = new Text(0, new Font("Times New Roman", Font.PLAIN, Constants.TEXT_SIZE),
                Constants.SCREEN_WIDTH - Constants.TEXT_X_POS - Constants.TEXT_SIZE, Constants.TEXT_Y_POS);

        // 게임매니저 객체 생성
        gameManager = new GameManager(leftScoreText, rightScoreText); // GameManager 먼저 생성
        ball = new Ball(ballRect, playerOne, ai, gameManager); // Ball 생성 시 gameManager 전달
        gameManager.setBall(ball); // GameManager에 Ball 설정

        // 컨트롤러 객체 생성
        playerController = new PlayerController(playerOne, keyListener, gameManager);
        aiController = new AIController(ai, ballRect, gameManager);
    }

    @Override
    public void onShow() {
        gameManager.resetGame();
        keyListener.reset();

        this.lastFrameTime = Time.getTime();

        if (gameThread == null || !gameThread.isAlive()) {
            System.out.println("GamePanel.startGame() called!");
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void onHide() {
        if (gameThread != null) {
            System.out.println("GamePanel.stopGame() called!");
            keyListener.reset();
            gameThread.interrupt();
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            double time = Time.getTime();
            double deltaTime = time - this.lastFrameTime;
            this.lastFrameTime = time;

            update(deltaTime);
            repaint();
        }
    }

    private void update(double delta) {
        gameManager.update(delta);
        playerController.update(delta);
        aiController.update(delta);
        ball.update(delta, playerController);

        if (gameManager.isGameOver()) {
            if (onGameOver != null) {
                SwingUtilities.invokeLater(onGameOver);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        leftScoreText.draw(g2);
        rightScoreText.draw(g2);
        playerOne.draw(g2);
        ai.draw(g2);
        ball.rect.draw(g2);

        if (gameManager.isCounting()) {
            String countdown = gameManager.getCountDownText();
            Font font = new Font("Times New Roman", Font.BOLD, 100);
            Text text = new Text(countdown, font, Constants.SCREEN_HEIGHT / 2.0, Color.WHITE);
            text.draw(g2);
        }
    }
}
