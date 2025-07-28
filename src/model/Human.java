package model;

import javafx.geometry.Rectangle2D;

public class Human {
    private boolean isAlive;
    private double x; // Always a positive number between 0 and background width
    private double y;
    private static final int HUMAN_SCALE = 3;
    private Enemy beingCaptured; // The enemy that is targeting the human
    private Enemy isCaptured; // The enemy that has captured the human
    private boolean isSaved = false; // The human has been saved by the player
    private boolean isFalling = false; // Being captured by an enemy, but the enemy has been hit, then the human falls
    private double speed;
    private static final int HUMAN_WIDTH = HUMAN_SCALE * 3;
    private static final int HUMAN_HEIGHT = HUMAN_SCALE * 8;
    private static final int points = 500; // If the human is saved, the player gets 500 points

    public Human(int x, int y) {
        this.x = x;
        this.y = y;
        this.isAlive = true;
        this.beingCaptured = null;
        this.isCaptured = null;
    }

    public void move(int deltaX, int deltaY) {
        x += deltaX;
        y += deltaY;
    }

    public void moveWithBackground(int deltaX, int deltaY, int backgroundSpeed, int direction) {
        x = ((x + deltaX + (backgroundSpeed * direction)) % 1800 + 1800) % 1800;
        y += deltaY;
    }

    // The human is rising due to the inertia of the enemy that is carrying it
    // Then it falls due to gravity
    public void fall() {
        double acceleration = 0.5;
        double speedLimit = 3.5;
        // The process of rising
        if (speed < 0) {
            this.y += speed;
            speed += acceleration;
        } else {
            if (speed < speedLimit) {
                speed += acceleration;
            }
            this.y += speed;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getHumanScale() {
        return HUMAN_SCALE;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public Enemy isBeingCaptured() {
        return beingCaptured;
    }

    public Enemy isCaptured() {
        return isCaptured;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public Rectangle2D getHitBox() {
        return new Rectangle2D(x, y, HUMAN_WIDTH, HUMAN_HEIGHT);
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public void setBeingCaptured(Enemy enemy) {
        beingCaptured = enemy;
    }

    public void setCaptured(Enemy enemy) {
        isCaptured = enemy;
    }

    public void setFalling(boolean falling) {
        isFalling = falling;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPoints() {
        return points;
    }
}