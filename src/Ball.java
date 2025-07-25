import java.util.Random;

public class Ball {
    public Rect rect;
    public Rect leftPaddle, rightPaddle;

    private Random rand = new Random();
    // velocity x, y
    private double vy;
    private double vx;

    public Ball(Rect rect, Rect leftPaddle, Rect rightPaddle) {
        this.rect = rect;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        resetVelocity();
    }

    public void resetVelocity() {
        // 공 방향 랜덤
        double randDirection = rand.nextBoolean() ? -1.0 : 1.0;
        // 공 각도 랜덤
        double randAngle = (rand.nextDouble() * Math.PI / 2.0) - (Math.PI / 4.0);

        this.vx = randDirection * Math.abs(Constants.BALL_SPEED * Math.cos(randAngle));
        this.vy = Constants.BALL_SPEED * Math.sin(randAngle);
    }
    private double calculateNewVelocityAngle(Rect paddle) {
        double relativeIntersectY = (paddle.y + (paddle.height/2.0)) - (this.rect.y + (this.rect.y / 2.0));
        double normalIntersectY = relativeIntersectY / (paddle.height/2.0);
        double theta = normalIntersectY * Constants.MAX_ANGLE;

        return Math.toRadians(theta);
    }

    private double[] calculateNewVelocity(Rect paddle) {
        double theta = calculateNewVelocityAngle(paddle);
        double newSign = -1.0 * Math.signum(this.vx);
        double newVx = newSign * Math.abs((Math.cos(theta)) * Constants.BALL_SPEED);
        double newVy = (-Math.sin(theta)) * Constants.BALL_SPEED;

        return new double[]{newVx, newVy};
    }

    private boolean collisionWith(Rect paddle) {
        boolean xCondition = this.rect.x <= paddle.x + paddle.width && this.rect.x + this.rect.width >= paddle.x;
        boolean yCondition = this.rect.y + this.rect.height >= paddle.y && this.rect.y <= paddle.y + paddle.height;

        return xCondition && yCondition;
    }

    public void update(double delta) {
        // x축 방향 확인
        if (vx < 0) {
            if (collisionWith(this.leftPaddle)) {
                double[] newVelocity = calculateNewVelocity(leftPaddle);
                this.vx = newVelocity[0];
                this.vy = newVelocity[1];
            }
        } else if (vx > 0) {
            if (collisionWith(this.rightPaddle)) {
                double[] newVelocity = calculateNewVelocity(rightPaddle);
                this.vx = newVelocity[0];
                this.vy = newVelocity[1];
            }
        }

        // y축 위치 확인
        if (vy > 0) {
            if (this.rect.y + this.rect.height > Constants.SCREEN_HEIGHT) {
                // y축 방향 전환
                this.vy *= -1;
            }
        } else if (vy < 0) {
            if (this.rect.y < Constants.TOOLBAR_HEIGHT) {
                // y축 방향 전환
                this.vy *= -1;
            }
        }

        // 위치 = 위치 + 속도
        this.rect.x += vx * delta;
        this.rect.y += vy * delta;
    }
}
