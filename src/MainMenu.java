import javax.swing.JFrame; // 좌측 상단 코너가 (0, 0)임.
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainMenu extends JFrame implements Runnable {

    public Graphics2D g2;
    public KL keyListener = new KL();
    public ML mouseListener = new ML();
    public Text startGame, exitGame, pong;
    public boolean isRunning = true;

    public MainMenu() {
        this.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT); // 화면 크기 설정
        this.setTitle(Constants.SCREEN_TITLE); // 제목 설정
        this.setResizable(false); // 화면 크기 조절 불가
        this.setVisible(true); // 화면 보이게
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫기 기능
        this.addKeyListener(keyListener); // 키 리스너 추가
        this.addMouseListener(mouseListener); // 마우스 리스너 추가
        this.addMouseMotionListener(mouseListener); // 마우스 모션 리스너 추가

        Font titleFont = new Font("Times New Roman", Font.PLAIN, Constants.MAIN_MENU_TITLE_SIZE);
        Font menuFont = new Font("Ariel", Font.PLAIN, Constants.MAIN_MENU_TEXT_SIZE);

        this.pong = new Text("javaPong", titleFont, Constants.MAIN_MENU_TITLE_Y, Color.WHITE);
        this.startGame = new Text("Start Game", menuFont, Constants.MAIN_MENU_START_Y, Color.WHITE);
        this.exitGame = new Text("Exit", menuFont, Constants.MAIN_MENU_EXIT_Y, Color.WHITE);


        g2 = (Graphics2D)this.getGraphics();
    }

    public void update(double delta) {
        // 프레임 부드럽게
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        this.draw(dbg);
        g2.drawImage(dbImage, 0, 0, this);

        // 마우스 조작
        if (mouseListener.getMouseX() > startGame.x - 50 && mouseListener.getMouseX() < startGame.x + startGame.width &&
        mouseListener.getMouseY() > startGame.y - 50 && mouseListener.getMouseY() < startGame.y + startGame.height - 10) {
            startGame.color = new Color(158, 158, 158); // 나중에 크기도 커지는 거로 바꿀 예정

            if (mouseListener.isMousePressed()) {
                Main.changeState(Main.GameState.COUNT_DOWN);
            }
        } else {
            startGame.color = Color.WHITE;
        }

        if (mouseListener.getMouseX() > exitGame.x - 100 && mouseListener.getMouseX() < exitGame.x + exitGame.width + 100 &&
                mouseListener.getMouseY() > exitGame.y - 30 && mouseListener.getMouseY() < exitGame.y + exitGame.height) {
            exitGame.color = new Color(158, 158, 158); // 나중에 크기도 커지는 거로 바꿀 예정
            if (mouseListener.isMousePressed()) {
                Main.changeState(Main.GameState.EXIT);
            }
        } else {
            exitGame.color = Color.WHITE;
        }

        // 키보드 조작
        if (keyListener.isKeyPressed(KeyEvent.VK_ENTER)) {
            Main.changeState(Main.GameState.COUNT_DOWN);
        }

        if (keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            Main.changeState(Main.GameState.EXIT);
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        startGame.draw(g2);
        exitGame.draw(g2);
        pong.draw(g2);
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

            update(deltaTime);
        }
        this.dispose();
    }
}