import javax.swing.*; // 좌측 상단 코너가 (0, 0)임.
import java.awt.*;
import java.util.Objects;

public class MainMenuPanel extends JPanel implements Runnable {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final GamePanel gamePanel; // GamePanel 필드 추가

    public Text startGame, exitGame, pong;

    public KL keyListener = new KL();
    public ML mouseListener = new ML();

    private Thread menuThread;
    private boolean isRunning = true;

    public ML getMouseListener() {
        return mouseListener;
    }

    // 생성자 수정: gamePanel 매개변수 추가
    public MainMenuPanel(CardLayout cardLayout, JPanel mainPanel, GamePanel gamePanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.gamePanel = gamePanel; // gamePanel 초기화

        this.setOpaque(true);
        this.setBackground(Color.BLACK);
        this.setLayout(null); // Use absolute positioning
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));

        Font titleFont = new Font("Times New Roman", Font.PLAIN, Constants.MAIN_MENU_TITLE_SIZE);
        Font menuFont = new Font("Ariel", Font.PLAIN, Constants.MAIN_MENU_TEXT_SIZE);

        this.pong = new Text("javaPong", titleFont, Constants.MAIN_MENU_TITLE_Y, Color.WHITE);
        this.startGame = new Text("Start Game", menuFont, Constants.MAIN_MENU_START_Y, Color.WHITE);
        this.exitGame = new Text("Exit", menuFont, Constants.MAIN_MENU_EXIT_Y, Color.WHITE);

        this.addKeyListener(keyListener);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);

        this.setFocusable(true);
    }

    public void startMenu() {
        isRunning = true;
        mouseListener.reset(); // 마우스 리스너 상태 초기화
        menuThread = new Thread(this);
        menuThread.start();
    }

    public void stopMenu() {
        isRunning = false;
        if (menuThread != null) {
            menuThread.interrupt();
        }
    }

    @Override
    public void run() {
        double lastFrameTime = Time.getTime();
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            double time = Time.getTime();
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time;

            update(deltaTime);
            repaint();

        }
    }

    private void update(double delta) {

        if (mouseListener.isMousePressed()) {
            if (isWithinBounds(startGame)) {
                System.out.println("Start Game Clicked!");
                cardLayout.show(mainPanel, "GamePanel");
                gamePanel.startGame(); // 직접 startGame() 호출
                stopMenu();
            } else if (isWithinBounds(exitGame)) {
                System.out.println("Exit Clicked!");
                System.exit(0);
            }
        }

        Color hoverColor = new Color(158, 158, 158);
        Color originalColor = Color.WHITE;

        if (isWithinBounds(startGame)) {
            if (!Objects.equals(startGame.color, hoverColor)) {
                startGame.color = hoverColor;
            }
        } else {
            if (!Objects.equals(startGame.color, originalColor)) {
                startGame.color = originalColor;
            }
        }

        if (isWithinBounds(exitGame)) {
            if (!Objects.equals(exitGame.color, hoverColor)) {
                exitGame.color = hoverColor;
            }
        } else {
            if (!Objects.equals(exitGame.color, originalColor)) {
                exitGame.color = originalColor;
            }
        }
    }

    private boolean isWithinBounds(Text text) {
        FontMetrics fm = mainPanel.getFontMetrics(text.font);
        double textWidth = fm.stringWidth(text.text);
        double textX = (Constants.SCREEN_WIDTH / 2.0) - (textWidth / 2.0);
        double textHeight = fm.getHeight();

        return mouseListener.getMouseX() >= textX && mouseListener.getMouseX() <= textX + textWidth &&
                mouseListener.getMouseY() >= text.y - textHeight && mouseListener.getMouseY() <= text.y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        pong.draw(g2);
        startGame.draw(g2);
        exitGame.draw(g2);
    }
}