import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private final JFrame window;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final Map<String, IPanel> panels = new HashMap<>();
    private IPanel currentPanel;

    public static final String MAIN_MENU_PANEL = "MainMenuPanel";
    public static final String GAME_PANEL = "GamePanel";

    public Main() {
        window = new JFrame("Java Pong");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout); // 메인 패널에서 패널들을 바꾸며 출력할 예정

        // 패널 생성
        IPanel mainMenuPanel = new MainMenuPanel(() -> showPanel(GAME_PANEL));
        IPanel gamePanel = new GamePanel(() -> showPanel(MAIN_MENU_PANEL));
        // 패널 등록
        panels.put(MAIN_MENU_PANEL, mainMenuPanel);
        panels.put(GAME_PANEL, gamePanel);

        mainPanel.add(mainMenuPanel.getPanel(), MAIN_MENU_PANEL);
        mainPanel.add(gamePanel.getPanel(), GAME_PANEL);

        window.add(mainPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        showPanel(MAIN_MENU_PANEL);
    }

    public void showPanel(String panelName) {
        if (currentPanel != null) {
            currentPanel.onHide();
        }

        currentPanel = panels.get(panelName);
        if (currentPanel == null) {
            System.err.println("No panel name " + panelName);
            return;
        }

        cardLayout.show(mainPanel, panelName);
        currentPanel.getPanel().requestFocusInWindow();
        currentPanel.onShow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
