public class Time {
    public static double timeStarted = System.nanoTime();

    public static double getTime() { return (System.nanoTime() - timeStarted) * 1E-9; } // 나노초를 초로 환산하기 위해 1E-9를 곱함

}
