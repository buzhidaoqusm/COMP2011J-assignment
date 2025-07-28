package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    private List<Enemy> enemies;
    private List<Human> humans;
    private int levelNumber;
    private static final int MAX_LEVEL = 5;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.enemies = new ArrayList<>();
        this.humans = new ArrayList<>();
        initializeLevel();
    }

    private void initializeLevel() {
        Random rand = new Random();
        int numLanders = 4 + (int) (levelNumber * 1.5);
        int numMutants = 1 + levelNumber;
        int numHumans = 8 - levelNumber;
        int backgroundWidth = 1800;

        // The landers should be placed in the sides of the move bounds to avoid collision at the beginning.
        for (int i = 0; i < numLanders; i++) {
            int x = rand.nextInt(backgroundWidth);
            while (x > DefenderGame.SCREEN_WIDTH / 3 && x < DefenderGame.SCREEN_WIDTH * 2 / 3) {
                x = rand.nextInt(backgroundWidth);
            }
            int y = rand.nextInt(DefenderGame.SCREEN_HEIGHT - 200) + 100;
            enemies.add(new Lander(x, y));
        }

        // The mutants should be placed in the sides of the move bounds to avoid collision at the beginning.
        for (int i = 0; i < numMutants; i++) {
            int x = rand.nextInt(backgroundWidth);
            while (x > DefenderGame.SCREEN_WIDTH / 6 && x < DefenderGame.SCREEN_WIDTH * 5 / 6) {
                x = rand.nextInt(backgroundWidth);
            }
            int y = rand.nextInt(DefenderGame.SCREEN_HEIGHT - 200) + 100;
            enemies.add(new Mutant(x, y));
        }

        for (int i = 0; i < numHumans; i++) {
            int x = rand.nextInt(backgroundWidth);
            int y = rand.nextInt(20) + DefenderGame.SCREEN_HEIGHT - 50;
            humans.add(new Human(x, y));
        }
    }

    // Check if any human is falling or saved
    public boolean checkHumans () {
        for (Human human : humans) {
            if (human.isFalling() && human.getY() < DefenderGame.SCREEN_HEIGHT || human.isSaved()) {
                return true;
            }
        }
        return false;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Human> getHumans() {
        return humans;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getMaxLevel() {
        return MAX_LEVEL;
    }
}
