public class Main {
    public static int state = 0;
    public static Thread mainThread;
    public static MainMenu menu;
    public static Window window;

    public static void main(String[] args) {
        menu = new MainMenu();
        mainThread = new Thread(menu);
        mainThread.start();
    }

    public static void changeState(int newState) {
        if (newState == 1 && state == 0) {
            menu.stop();
            state = newState;
            window = new Window();
            mainThread = new Thread(window);
            mainThread.start();
        } else if (newState == 0 && state == 1) {
            window.stop();
            state = newState;
            main(new String[0]);
        } else if (newState == 2) {
            if (window != null) window.stop();
            if (menu != null) menu.stop();
        }
    }
}
