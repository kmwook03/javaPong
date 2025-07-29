import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final KL keyListener = new KL();
    private final Rect playerOne;
    private final Rect ai;
    private final Ball ball;
    private final PlayerController playerController;
    private final AIController aiController;
    private final Text leftScoreText;
    private final Text rightScoreText;
    private final GameManager gameManager;

    // keyListener에 접근하기 위한 public getter 메서드 추가
    public KL getKeyListener() {
        return keyListener;
    }

    public GamePanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        this.setFocusable(true);
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));

        playerOne = new Rect(Constants.HZ_PADDING, Constants.SCREEN_HEIGHT / 2.0, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);

        Rect ballRect = new Rect(Constants.SCREEN_WIDTH/2.0, Constants.SCREEN_HEIGHT/2.0, Constants.BALL_WIDTH, Constants.BALL_HEIGHT, Constants.PADDLE_COLOR);

        ai = new Rect(Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH - Constants.HZ_PADDING, Constants.SCREEN_HEIGHT / 2.0, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);

        leftScoreText = new Text(0, new Font("Times New Roman", Font.PLAIN, Constants.TEXT_SIZE),
                Constants.TEXT_X_POS, Constants.TEXT_Y_POS);
        rightScoreText = new Text(0, new Font("Times New Roman", Font.PLAIN, Constants.TEXT_SIZE),
                Constants.SCREEN_WIDTH - Constants.TEXT_X_POS - Constants.TEXT_SIZE, Constants.TEXT_Y_POS);

        gameManager = new GameManager(leftScoreText, rightScoreText); // GameManager 먼저 생성
        ball = new Ball(ballRect, playerOne, ai, gameManager); // Ball 생성 시 gameManager 전달
        gameManager.setBall(ball); // GameManager에 Ball 설정

        playerController = new PlayerController(playerOne, keyListener, gameManager);
        aiController = new AIController(ai, ballRect, gameManager);
    }

    public void startGame() {
        System.out.println("GamePanel.startGame() called!"); // 디버깅 출력 추가
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGame() {
        System.out.println("GamePanel.stopGame() called!"); // 디버깅 출력 추가
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    @Override
    public void run() {
        System.out.println("GamePanel run() method entered!"); // 디버깅 출력 추가
        double lastFrameTime = Time.getTime(); // 현재 시간으로 초기화
        while (!Thread.currentThread().isInterrupted()) {
            double time = Time.getTime();
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time;

            update(deltaTime);
            repaint();

        }
        System.out.println("GamePanel run() method exited!"); // 디버깅 출력 추가
    }

    private void update(double delta) {
        gameManager.update(delta);

        playerController.update(delta);
        aiController.update(delta);
        ball.update(delta, playerController);

        if (gameManager.isGameOver()) {
            cardLayout.show(mainPanel, "MainMenuPanel");
            stopGame();
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
