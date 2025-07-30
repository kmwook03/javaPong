import javax.swing.*; // 좌측 상단 코너가 (0, 0)임.
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class MainMenuPanel extends JPanel implements Runnable, IPanel {

    private final Runnable onStartGame;

    public Text startGame, exitGame, pong;

    public KL keyListener = new KL();
    public ML mouseListener = new ML();

    private Thread menuThread;

    public ML getMouseListener() {
        return mouseListener;
    }

    public MainMenuPanel(Runnable onStartGameCallback) {
        this.onStartGame = onStartGameCallback;

        // 화면 설정
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
        this.setLayout(null); // Use absolute positioning
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setFocusable(true);

        // UI 텍스트
        Font titleFont = new Font("Times New Roman", Font.PLAIN, Constants.MAIN_MENU_TITLE_SIZE);
        Font menuFont = new Font("Ariel", Font.PLAIN, Constants.MAIN_MENU_TEXT_SIZE);
        this.pong = new Text("javaPong", titleFont, Constants.MAIN_MENU_TITLE_Y, Color.WHITE);
        this.startGame = new Text("Start Game", menuFont, Constants.MAIN_MENU_START_Y, Color.WHITE);
        this.exitGame = new Text("Exit", menuFont, Constants.MAIN_MENU_EXIT_Y, Color.WHITE);

        // 이벤트리스너
        this.addKeyListener(keyListener);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
    }

    @Override
    public void onShow() {
        mouseListener.reset();
        if (menuThread == null || !menuThread.isAlive()) {
            menuThread = new Thread(this);
            menuThread.start();
        }
    }

    @Override
    public void onHide() {
        if (menuThread != null && menuThread.isAlive()) {
            menuThread.interrupt();
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void run() {
        double lastFrameTime = Time.getTime();
        while (!Thread.currentThread().isInterrupted()) {
            double time = Time.getTime();
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time;

            update(deltaTime);
            repaint();
        }
    }

    private void update(double delta) {
        if (mouseListener.isMousePressed()) {
            boolean clickedStart = isWithinBounds(startGame);
            boolean clickedExit = isWithinBounds(exitGame);

            mouseListener.isPressed = false;

            if (clickedStart) {
                System.out.println("Start Game Clicked!");
                if (onStartGame != null) {
                    onStartGame.run();
                }
            } else if (clickedExit) {
                System.out.println("Exit Clicked!");
                System.exit(0);
            }
        }

//        if (keyListener.isKeyPressed(KeyEvent.VK_ENTER)) {
//            System.out.println("Start Game Clicked!");
//            if (onStartGame != null) {
//                onStartGame.run();
//            }
//        }
//
//        if (keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)) {
//            System.out.println("Exit Clicked!");
//            System.exit(0);
//        }

        updateHoverEffect();
    }

    private void updateHoverEffect() {
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

    // 위험한 코드
    // getFontMetrics는 패널에 화면 추가 후 호출하는 것이 더 안전
    // 일단 작동하니 수정은 보류
    private boolean isWithinBounds(Text text) {
        FontMetrics fm = getFontMetrics(text.font);
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