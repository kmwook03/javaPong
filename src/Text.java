import java.awt.*;

public class Text {
    public  String text;
    public Font font;
    public double x, y;
    public double width, height;
    public Color color = Color.WHITE;
    private boolean center;

    public Text(String text, Font font, double x, double y, Color color) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.center = false; // 중앙 정렬 off
    }

    public Text(String text, Font font, double y, Color color) {
        this.font = font;
        this.text = text;
        this.y = y;
        this.color = color;
        this.center = true; // 중앙 정렬 on
    }

    public Text(int text, Font font, double x, double y) {
        this.font = font;
        this.text = "" + text;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        g2.setFont(this.font);
        if (this.center) {
            FontMetrics fm = g2.getFontMetrics();
            this.width = fm.stringWidth(this.text);
            this.height = fm.getHeight();

            this.x = (Constants.SCREEN_WIDTH / 2.0) - (this.width / 2.0);
        }

        g2.drawString(this.text, (float)this.x, (float)this.y);
    }
}
