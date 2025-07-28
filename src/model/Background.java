package model;

public class Background {
    private int backgroundX; // X coordinate of the background, and it's always a positive number between 0 and backgroundWidth
    public static final int BACKGROUND_Y = DefenderGame.SCREEN_HEIGHT - 90;
    public static final int BACKGROUND_SPEED = 4;
    public static final int BACKGROUND_WIDTH = 1800;
    public static final int PIXEL_SIZE = 3;
    private int direction; // 1 for right, -1 for left

    void move(int x1) {
        this.backgroundX += x1;
    }

    // This method is used to calculate the new x coordinate of the background after moving
    public static double getPositiveModulo(double x) {
        return ((x % BACKGROUND_WIDTH) + BACKGROUND_WIDTH) % BACKGROUND_WIDTH;
    }
    public int getBackgroundX() {
        return backgroundX;
    }
    public int getDirection() {
        return direction;
    }
    public void setBackgroundX(int backgroundX) {
        this.backgroundX = backgroundX;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
}
