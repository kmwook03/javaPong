import javax.swing.JFrame; // 좌측 상단 코너가 (0, 0)임.
import java.awt.*;
import java.awt.event.KeyEvent;

public class Window extends JFrame implements Runnable {

    public Graphics2D g2;
    public KL keyListener = new KL();
    public Rect playerOne, ai, ballRect;
    public Ball ball;
    public PlayerController playerController;
    public AIController aiController;
    public Text leftScoreText, rightScoreText;
    public boolean isRunning = true;
    public GameManager gameManager;

    public Window() {
        this.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT); // 화면 크기 설정
        this.setTitle(Constants.SCREEN_TITLE); // 제목 설정
        this.setResizable(false); // 화면 크기 조절 불가
        this.setVisible(true); // 화면 보이게
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫기 기능
        this.addKeyListener(keyListener); // 키 리스너 추가
        Constants.TOOLBAR_HEIGHT = this.getInsets().top;
        Constants.INSETS_BOTTOM = this.getInsets().bottom;

        g2 = (Graphics2D) this.getGraphics();

        playerOne = new Rect(Constants.HZ_PADDING, Constants.SCREEN_HEIGHT / 2.0, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);
        playerController = new PlayerController(playerOne, keyListener);


        ballRect = new Rect(Constants.SCREEN_WIDTH/2.0, Constants.SCREEN_HEIGHT/2.0, Constants.BALL_WIDTH, Constants.BALL_HEIGHT, Constants.PADDLE_COLOR);

        ai = new Rect(Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH - Constants.HZ_PADDING, Constants.SCREEN_HEIGHT / 2.0, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);
        aiController = new AIController(ai, ballRect);


        leftScoreText = new Text(0, new Font("Times New Roman", Font.PLAIN, Constants.TEXT_SIZE), Constants.TEXT_X_POS, Constants.TEXT_Y_POS);
        rightScoreText = new Text(0, new Font("Times New Roman", Font.PLAIN, Constants.TEXT_SIZE),
                Constants.SCREEN_WIDTH - Constants.TEXT_X_POS - Constants.TEXT_SIZE, Constants.TEXT_Y_POS);

        ball = new Ball(ballRect, playerOne, ai);

        gameManager = new GameManager(ball, playerOne, ai, leftScoreText, rightScoreText);
    }

    public void update(double delta) {
        // 프레임 부드럽게
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        this.draw(dbg);
        g2.drawImage(dbImage, 0, 0, this);

        gameManager.update(delta);

        if (Main.state == Main.GameState.PLAYING) {
            playerController.update(delta);
            aiController.update(delta);
            ball.update(delta, playerController);
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        leftScoreText.draw(g2);
        rightScoreText.draw(g2);

        playerOne.draw(g2);
        ai.draw(g2);

        if (gameManager.isCounting) {
            String countdown = gameManager.getCountDownText();
            Font font = new Font("Times New Roman", Font.BOLD, 100);
            Text text = new Text(countdown, font, Constants.SCREEN_HEIGHT/2.0, Color.WHITE);
            text.draw(g2);
        }
        ballRect.draw(g2);
    }

    public void stop() {
        isRunning = false;
    }

    public void run() {
        double lastFrameTime = 0.0;
        while (isRunning) {
            double time = Time.getTime();
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time; // 프레임 업데이트

            keyListener.update();

            update(deltaTime);
        }
        this.dispose();
    }
}