package model;

import javafx.geometry.Rectangle2D;

import java.util.List;
import java.util.Random;

public class Lander extends Enemy {
    private Human beingCapturedHuman; // The human that the lander is moving towards
    private Human capturedHuman; // The human that the lander has captured
    private Random random;
    private int movingDuration = 0;
    private double angle; // Angle in degrees. The y-axis positive direction is 90 degree, x-axis positive direction is 0 degree

    public Lander(int x, int y) {
        super(x, y, AlienType.Lander);
        this.random = new Random();
    }

    public void captureHuman(List<Human> humans) {
        if ((random.nextInt() % 2000 == 0 || this.beingCapturedHuman != null) && capturedHuman == null) {
            // Find the human in the window that is closest to the lander
            Human closestHuman = getClosestHuman(humans);
            // Move the lander towards the human
            if (closestHuman != null) {
                closestHuman.setBeingCaptured(this);
                this.beingCapturedHuman = closestHuman;
                moveTowardHuman(closestHuman);
            }
        } else if (capturedHuman == null) {
            moveRandomly();
        } else {
            // The lander has captured the human, then move the lander and the human up
            this.setY(this.getY() - this.getSpeed());
            this.capturedHuman.setY((this.capturedHuman.getY() - this.getSpeed()));
            // Check if the human is at the top of the screen
            if (this.capturedHuman.getY() <= 60) {
                this.capturedHuman.setAlive(false);
                this.capturedHuman.setCaptured(null);
                this.capturedHuman = null;
                this.setAlive(false);
            }
        }
    }

    private Human getClosestHuman(List<Human> humans) {
        Human closestHuman = null;
        double minDistance = Double.MAX_VALUE;
        for (Human human : humans) {
            // If the human is in the window, not being captured by another lander, and not already captured
            if (human.getX() > 0 && human.getX() < 768
                    && (human.isBeingCaptured() == null || human.isBeingCaptured() != null && human.isBeingCaptured().equals(this))
                    && human.isCaptured() == null && !human.isSaved()) {
                double distance = Math.sqrt(Math.pow(human.getX() - this.getX(), 2) + Math.pow(human.getY() - this.getY(), 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestHuman = human;
                }
            }
        }
        return closestHuman;
    }

    private void moveTowardHuman(Human human) {
        int speed = this.getSpeed();
        int deviationX = 9;
        int deviationY = 24;
        // Move the lander towards the human, first in the x direction and then in the y direction
        if (human.getX() != this.getX() + deviationX) {
            // Math.min is used to make sure the lander does not move too far.
            // When the distance between the lander and the human is less than the speed, the lander moves to the human directly
            this.setX((int) (this.getX() + Math.min(speed * Math.signum(human.getX() - this.getX() - deviationX), Math.abs(human.getX() - this.getX() - deviationX))));
        } else {
            this.setY((int) (this.getY() + Math.min(speed * Math.signum(human.getY() - this.getY() - deviationY), Math.abs(human.getY() - this.getY() - deviationY))));
        }

        // The lander has captured the human
        if (human.getX() == this.getX() + deviationX && human.getY() == this.getY() + deviationY) {
            this.capturedHuman = human;
            human.setCaptured(this);
            human.setBeingCaptured(null);
            this.beingCapturedHuman = null;
        }
    }

    private void moveRandomly() {
        // The lander is moving towards certain direction
        if (movingDuration != 0) {
            double x = Math.toIntExact(Math.round(this.getX() + this.getSpeed() * Math.cos(Math.toRadians(angle))));
            this.setX(Background.getPositiveModulo(x));
            double y = Math.toIntExact(Math.round(this.getY() + this.getSpeed() * Math.sin(Math.toRadians(angle))));
            if (y > 60 && y < 512) {
                this.setY(y);
                movingDuration--;
            } else {
                // If the lander moves at the top or bottom, end this duration immediately
                movingDuration = 0;
            }
        } else {
            movingDuration = random.nextInt(100) + 50;
            // The lander moves either move in y-axis or x-axis
            if (random.nextInt() % 2 == 0) {
                angle = random.nextInt() % 2 == 0 ? 0 : 180;
            } else {
                // If the lander is under the middle of the screen, it more likely moves towards the top
                if (this.getY() < 286) {
                    angle = random.nextDouble() < Math.pow(286 - this.getY(), 2) / (226 * 226) ? 90 : 270;
                } else {
                    angle = random.nextDouble() < Math.pow(286 - this.getY(), 2) / (226 * 226) ? 270 : 90;
                }
            }
        }
    }


    @Override
    public boolean isHit(Bullet b) {
        // reuse the isHit method from the Enemy class
        if (super.isHit(b)) {
            if (this.beingCapturedHuman != null) {
                this.beingCapturedHuman.setBeingCaptured(null);
                this.beingCapturedHuman = null;
            } else if (this.capturedHuman != null) {
                this.capturedHuman.setFalling(true); // If the lander is hit, the human falls
                this.capturedHuman.setSpeed(-this.getSpeed()); // The primitive speed is towards the negative y-axis
                this.capturedHuman.setCaptured(null);
                this.capturedHuman = null;
            }
            return true;
        }
        return false;
    }

    @Override
    public Rectangle2D getHitBox() {
        return new Rectangle2D(this.getX() + Enemy.getEnemyScale(), this.getY(), Enemy.getEnemyScale() * 7, Enemy.getEnemyScale() * 7);
    }
}