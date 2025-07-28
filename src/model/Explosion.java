package model;

public class Explosion {
    private double x;
    private double y;
    private int duration; // The number of ticks the explosion will last
    private int currentTick; // The current tick of the explosion
    private AlienType enemyType; // The type of enemy that was destroyed

    public Explosion(double x, double y, int duration, AlienType enemyType) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.currentTick = 0;
        this.enemyType = enemyType;
    }

    public void update() {
        currentTick++;
    }

    public boolean isFinished() {
        return currentTick >= duration;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public AlienType getEnemyType() {
        return enemyType;
    }
}