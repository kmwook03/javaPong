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
        double relativeIntersectY = (paddle.y + (paddle.height / 2.0)) - (this.rect.y + (this.rect.height / 2.0));
        double normalIntersectY = relativeIntersectY / (paddle.height/2.0);
        double theta = normalIntersectY * Constants.MAX_ANGLE;

        return Math.toRadians(theta);
    }

    private void handlePaddleCollision(Rect paddle, Controller controller) {
        double currentSpeed = Math.sqrt(vx * vx + vy * vy); // 벡터 크기 계산
        if (controller instanceof PlayerController playerController) {
            boolean isSmashing = playerController.isTryingToSmash();
            if (isSmashing) {
                System.out.println("Smashing");
                currentSpeed += Constants.SMASH_ACCELERATE;
                if (currentSpeed > Constants.MAX_BALL_SPEED) {
                    currentSpeed = Constants.MAX_BALL_SPEED;
                }
                System.out.println("smashedSpeed: " + currentSpeed);
            }
        }

        System.out.println("currentSpeed: " + currentSpeed);

        double theta = calculateReflectionAngle(paddle);
        double direction = Math.signum(this.vx);

        this.vx = -direction * currentSpeed * Math.cos(theta);
        this.vy = -(Math.sin(theta)) * currentSpeed;
    }

    private boolean collisionWith(Rect paddle) {
        boolean xCondition = this.rect.x <= paddle.x + paddle.width && this.rect.x + this.rect.width >= paddle.x;
        boolean yCondition = this.rect.y + this.rect.height >= paddle.y && this.rect.y <= paddle.y + paddle.height;

        return xCondition && yCondition;
    }

    public void update(double delta, Controller leftController, Controller rightController) {
        if (gameManager.isCounting()) return; // 카운트다운 중에는 공 움직임 처리 안 함

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
            if (this.rect.y + this.rect.height > Constants.SCREEN_HEIGHT) {
                // 벽 충돌시 감속, y축 방향 전환
                this.rect.y = Constants.SCREEN_HEIGHT - this.rect.height;
                this.vy *= Constants.WALL_DAMPING;
                this.vy *= -1;
            }
        } else if (vy < 0) {
            if (this.rect.y < 0) {
                // 벽 충돌시 감속, y축 방향 전환
                this.rect.y = 0;
                this.vy *= Constants.WALL_DAMPING;
                this.vy *= -1;
            }
        }

        // 위치 = 위치 + 속도
        this.rect.x += vx * delta;
        this.rect.y += vy * delta;
    }
}
