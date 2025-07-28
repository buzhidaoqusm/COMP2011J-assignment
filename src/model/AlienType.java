package model;

public enum AlienType {
    Lander(9, 9, 150, 2), Mutant(7, 6, 300, 2);
    private int width;
    private int height;
    private int score;
    private int speed;

    AlienType(int w, int h, int s, int v) {
        width = w;
        height = h;
        score = s;
        speed = v;
    }

    public int getWidth() {
        return width;
    }

    public int getScore() {
        return score;
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }
}
