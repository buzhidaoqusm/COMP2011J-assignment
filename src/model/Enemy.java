package model;

import javafx.geometry.Rectangle2D;

import java.util.Random;

public abstract class Enemy implements Hittable{
    private double x; // x is always a positive number
    private double y;
    private int speed;
    private boolean alive;
    private Rectangle2D hitBox;
    private static final int ENEMY_SCALE = 3;
    private AlienType type;
    private Random rand = new Random();
    private int ENEMY_WIDTH;
    private int ENEMY_HEIGHT;

    Enemy (int x, int y, AlienType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.ENEMY_HEIGHT = type.getHeight();
        this.ENEMY_WIDTH = type.getWidth();
        this.alive = true;
        this.speed = type.getSpeed();
    }

    public void moveWithBackground(int deltaX, int deltaY, int backgroundSpeed, int direction) {
        x = Background.getPositiveModulo(x + deltaX + (backgroundSpeed * direction));
        y += deltaY;
    }

    public Bullet fire(Player player) {
        int playerX = player.getX();
        int playerY = player.getY();
        int playerWidth = Player.SHIP_WIDTH;
        int playerHeight = Player.SHIP_HEIGHT;
        Bullet bullet = null;
        if (rand.nextInt() % 280 == 0) {
            double deltaX = playerX + playerWidth / 2 - this.x;
            double deltaY = playerY + playerHeight / 2 - this.y;
            double angle = Math.toDegrees(Math.atan2(deltaY, deltaX)); // calculate angle between player and enemy
            if (type.equals(AlienType.Lander)) {
                bullet = new Bullet((int) this.x, (int) this.y, angle, "lander", 6, 6, 10);
            } else {
                bullet = new Bullet((int) this.x, (int) this.y, angle, "mutant", 9, 9, 10);
            }
        }
        return bullet;
    }
    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public int getPoints() {
        return type.getScore();
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isHit(Bullet b) {
        boolean hit = getHitBox().intersects(b.getHitBox());
        if (hit) {
            alive = false;
            DefenderGame.addExplosion(new Explosion(x, y, 30, type));
        }
        return hit;
    }

    @Override
    public Rectangle2D getHitBox() {
        return new Rectangle2D(x, y, ENEMY_SCALE * type.getWidth(), ENEMY_SCALE * type.getHeight());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public AlienType getType() {
        return type;
    }

    public static int getEnemyScale() {
        return ENEMY_SCALE;
    }

    public int getSpeed() {
        return speed;
    }

    public void setX(double x) {
        this.x = ((x % 1800) + 1800) % 1800;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
