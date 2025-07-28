package model;

public class Mutant extends Enemy {
    public Mutant(int x, int y) {
        super(x, y, AlienType.Mutant);
    }

    public void chasePlayer(int playerX, int playerY) {
        // Implement specific chase logic for Mutant
        int speed = this.getSpeed();
        double x = this.getX();
        double y = this.getY();
        double deltaX = playerX - x;
        double deltaY = playerY - y;
        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        double x1 = speed * Math.cos(Math.toRadians(angle));
        double y1 = speed * Math.sin(Math.toRadians(angle));
        this.setX((int) (x + x1));
        this.setY((int) (y + y1));
    }
}