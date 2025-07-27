import java.awt.Color;

public class Constants {
    /* -- 스크린 관련 상수 */
    // screen size
    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 720;
    // screen title
    public static final String SCREEN_TITLE = "javaPong";

    /* -- 엔티티 관련 상수 -- */
    public static final double PADDLE_WIDTH = 10;
    public static final double PADDLE_HEIGHT = 100;
    public static final Color PADDLE_COLOR = Color.WHITE;
    public static final double BALL_WIDTH = 10;
    public static final double BALL_HEIGHT = 10;

    public static final double PADDLE_SPEED = 350;
    public static final double DASH_SPEED = 900;
    public static final double BALL_SPEED = 400;
    public static final double SMASHED_SPEED = 100;
    public static final double MAX_ANGLE = 45;

    // 수직 패딩
    public static final double HZ_PADDING = 40;

    public static double TOOLBAR_HEIGHT;
    public static double INSETS_BOTTOM;

    /* -- 텍스트 관련 상수 -- */
    public static final int MAIN_MENU_TEXT_SIZE = 40;
    public static final int MAIN_MENU_TITLE_SIZE = 100;
    public static final double MAIN_MENU_Y_CENTER = SCREEN_HEIGHT / 2.0;
    public static final double MAIN_MENU_TITLE_Y = MAIN_MENU_Y_CENTER - 120;
    public static final double MAIN_MENU_START_Y = MAIN_MENU_Y_CENTER;
    public static final double MAIN_MENU_EXIT_Y = MAIN_MENU_Y_CENTER + 80;

    public static final int TEXT_Y_POS = 100;
    public static final int TEXT_X_POS = 80;
    public static final int TEXT_SIZE = 40;
    public static final int WIN_SCORE = 3;
}
