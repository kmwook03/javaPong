import java.util.Random;

public class Ball {
    public Rect rect;
    public Rect leftPaddle, rightPaddle;
    private final Random rand = new Random();
    private final GameManager gameManager; // GameManager 필드 추가

    private double vy;
    private double vx;

    public Ball(Rect rect, Rect leftPaddle, Rect rightPaddle, GameManager gameManager) { // GameManager 매개변수 추가
        this.rect = rect;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.gameManager = gameManager; // GameManager 초기화
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
    private double calculateReflectionAngle(Rect paddle) {
        double relativeIntersectY = (paddle.getY() + (paddle.getHeight() / 2.0)) - (this.rect.getY() + (this.rect.getHeight() / 2.0));
        double normalIntersectY = relativeIntersectY / (paddle.getHeight()/2.0);
        double theta = normalIntersectY * Constants.MAX_ANGLE;

        return Math.toRadians(theta);
    }

    private void handlePaddleCollision(Rect paddle, Controller controller) {
        double currentSpeed = Math.sqrt(vx * vx + vy * vy); // 벡터 크기 계산

        if (controller.isTryingToSmash()) {
            currentSpeed += Constants.SMASH_ACCELERATE;
            if (currentSpeed > Constants.MAX_BALL_SPEED) {
                currentSpeed = Constants.MAX_BALL_SPEED;
            }
            System.out.println("smashedSpeed: " + currentSpeed);
        } else {
            currentSpeed += Constants.PADDLE_COLLISION_ACCELERATE;
        }

        System.out.println("currentSpeed: " + currentSpeed);

        double theta = calculateReflectionAngle(paddle);
        double direction = Math.signum(this.vx);

        this.vx = -direction * currentSpeed * Math.cos(theta);
        this.vy = -(Math.sin(theta)) * currentSpeed;
    }

    private boolean collisionWith(Rect paddle) {
        boolean xCondition = this.rect.getX() <= paddle.getX() + paddle.getWidth() && this.rect.getX() + this.rect.getWidth() >= paddle.getX();
        boolean yCondition = this.rect.getY() + this.rect.getHeight() >= paddle.getY() && this.rect.getY() <= paddle.getY() + paddle.getHeight();

        return xCondition && yCondition;
    }

    public void update(double delta, Controller leftController, Controller rightController) {
        if (gameManager.isCounting()) return; // 카운트다운 중에는 공 움직임 처리 안 함

        vx *= (1.0 - Constants.AIR_DRAG_COEFFICIENT * delta);
        vy *= (1.0 - Constants.AIR_DRAG_COEFFICIENT * delta);

        // x축 방향 확인
        if (vx < 0) {
            if (collisionWith(this.leftPaddle)) {
                handlePaddleCollision(leftPaddle, leftController);
            }
        } else if (vx > 0) {
            if (collisionWith(this.rightPaddle)) {
                handlePaddleCollision(rightPaddle, rightController);
            }
        }

        // y축 위치 확인
        if (vy > 0) {
            if (this.rect.getY() + this.rect.getHeight() > Constants.SCREEN_HEIGHT) {
                // 벽 충돌시 감속, y축 방향 전환
                this.rect.setY(Constants.SCREEN_HEIGHT - this.rect.getHeight());
                this.vy *= Constants.WALL_DAMPING;
                this.vy *= -1;
            }
        } else if (vy < 0) {
            if (this.rect.getY() < 0) {
                // 벽 충돌시 감속, y축 방향 전환
                this.rect.setY(0);
                this.vy *= Constants.WALL_DAMPING;
                this.vy *= -1;
            }
        }

        // 위치 = 위치 + 속도
        this.rect.setX(this.rect.getX() + vx * delta);
        this.rect.setY(this.rect.getY() + vy * delta);
    }
}
