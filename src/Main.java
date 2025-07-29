import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent; // 추가
import java.awt.event.KeyListener; // 추가

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(Constants.SCREEN_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            GamePanel gamePanel = new GamePanel(cardLayout, mainPanel);
            MainMenuPanel mainMenuPanel = new MainMenuPanel(cardLayout, mainPanel, gamePanel); // gamePanel 추가

            // JFrame에 KeyListener 추가
            frame.addKeyListener(gamePanel.getKeyListener()); // GamePanel의 keyListener를 직접 사용
            frame.addMouseListener(mainMenuPanel.getMouseListener());

            mainPanel.add(mainMenuPanel, "MainMenuPanel");
            mainPanel.add(gamePanel, "GamePanel");

            frame.add(mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.requestFocusInWindow(); // JFrame에 포커스 요청

            mainMenuPanel.startMenu();

            mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentShown(java.awt.event.ComponentEvent e) {
                    if (e.getComponent() == gamePanel) {
                        // gamePanel.startGame()은 MainMenuPanel에서 직접 호출하므로 여기서는 필요 없음
                    } else if (e.getComponent() == mainMenuPanel) { // MainMenuPanel이 다시 보일 때
                        mainMenuPanel.startMenu();
                        SwingUtilities.invokeLater(() -> { // invokeLater로 감싸기
                            mainMenuPanel.requestFocusInWindow(); // MainMenuPanel에 포커스 요청
                        });
                    }
                }

                @Override
                public void componentHidden(java.awt.event.ComponentEvent e) {
                    if (e.getComponent() == gamePanel) {
                        gamePanel.stopGame();
                    } else if (e.getComponent() == mainMenuPanel) { // MainMenuPanel이 숨겨질 때
                        mainMenuPanel.stopMenu();
                    }
                }
            });
        });
    }
}
