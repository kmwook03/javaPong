public class Main {
    public static GameState state = GameState.MAIN_MENU;
    public static Thread mainThread;
    public static MainMenu mainMenu;
    public static Window window;

    public enum GameState {
        MAIN_MENU,
        COUNT_DOWN,
        PLAYING,
        GAME_OVER,
        EXIT
    }

    public static void main(String[] args) {
        mainMenu = new MainMenu();
        mainThread = new Thread(mainMenu);
        mainThread.start();
    }

    public static void changeState(GameState newState) {
        state = newState;

        switch (newState) {
            case MAIN_MENU -> {
                mainMenu = new MainMenu();
                if (window != null) window.stop();
                new Thread(mainMenu).start();
            }
            case COUNT_DOWN, PLAYING -> {
                if (mainMenu != null) mainMenu.stop();
                window = new Window();
                new Thread(window).start();
            }
            case GAME_OVER -> {

            }
            case EXIT -> {
                if (mainMenu != null) mainMenu.stop();
                if (window != null) window.stop();
                System.exit(0);
            }
        }
    }
}
