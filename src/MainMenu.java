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
        this.startGame = new Text("Start Game", new Font("Times New Roman", Font.PLAIN, 40), Constants.SCREEN_WIDTH / 2.0 - 140.0, Constants.SCREEN_HEIGHT / 2.0, Color.WHITE);
        this.exitGame = new Text("Exit", new Font("Times New Roman", Font.PLAIN, 40), Constants.SCREEN_WIDTH / 2.0 - 80.0, Constants.SCREEN_HEIGHT / 2.0 + 60, Color.WHITE);
        this.pong = new Text("javaPong", new Font("Times New Roman", Font.PLAIN, 100), Constants.SCREEN_WIDTH / 2.0 - 200, Constants.SCREEN_HEIGHT / 2.0 - 100, Color.WHITE);

        g2 = (Graphics2D)this.getGraphics();
    }

    public void update(double delta) {
        // 프레임 부드럽게
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        this.draw(dbg);
        g2.drawImage(dbImage, 0, 0, this);
        
        if (mouseListener.getMouseX() > startGame.x - 50 && mouseListener.getMouseX() < startGame.x + startGame.width &&
        mouseListener.getMouseY() > startGame.y - 50 && mouseListener.getMouseY() < startGame.y + startGame.height) {
            startGame.color = new Color(158, 158, 158); // 나중에 크기도 커지는 거로 바꿀 예정

            if (mouseListener.isMousePressed()) {
                Main.changeState(1);
            }
        } else {
            startGame.color = Color.WHITE;
        }

        if (mouseListener.getMouseX() > exitGame.x - 50 && mouseListener.getMouseX() < exitGame.x + exitGame.width &&
                mouseListener.getMouseY() > exitGame.y - 20 && mouseListener.getMouseY() < exitGame.y + exitGame.height) {
            exitGame.color = new Color(158, 158, 158); // 나중에 크기도 커지는 거로 바꿀 예정
            if (mouseListener.isMousePressed()) {
                Main.changeState(2);
            }
        } else {
            exitGame.color = Color.WHITE;
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