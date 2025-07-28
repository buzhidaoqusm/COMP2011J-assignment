package model;

import javafx.geometry.Rectangle2D;

public class Bullet implements Hittable {
    private int x;
    private int y;
    private int[] origin; // The position where bullet is shoot
    private double angle; // Angle in degrees
    private boolean alive = true;
    private Rectangle2D hitBox;
    private String name;
    private int bulletWidth;
    private int bulletHeight;
    private int bulletSpeed;

    public Bullet(int x, int y, double angle, String name, int bulletWidth, int bulletHeight, int bulletSpeed) {
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.origin = new int[]{x, y};
        this.name = name;
        this.bulletWidth = bulletWidth;
        this.bulletHeight = bulletHeight;
        this.bulletSpeed = bulletSpeed;
        hitBox = new Rectangle2D(x, y, bulletWidth, bulletHeight);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move() {
        x = Math.toIntExact(Math.round(x + bulletSpeed * Math.cos(Math.toRadians(angle))));
        y = Math.toIntExact(Math.round(y + bulletSpeed * Math.sin(Math.toRadians(angle))));
        hitBox = new Rectangle2D(x, y, bulletWidth, bulletHeight);
    }

    public void moveWithBackground(int deltaX, int deltaY, int backgroundSpeed, int direction) {
        x = (int) Background.getPositiveModulo(x + deltaX + (backgroundSpeed * direction));
        y += deltaY;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isHit(Bullet b) {
        boolean hit = hitBox.intersects(b.getHitBox());
        if (hit) {
            alive = false;
            b.alive = false;
        }
        return hit;
    }

    @Override
    public Rectangle2D getHitBox() {
        return hitBox;
    }

    public String getName() {
        return name;
    }

    public double getAngle() {
        return angle;
    }

    public int[] getOrigin() {
        return origin;
    }

    public int getBulletWidth() {
        return bulletWidth;
    }

    public int getBulletHeight() {
        return bulletHeight;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
