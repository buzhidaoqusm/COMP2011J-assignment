package model;

import display.PlayerListener;
import javafx.geometry.Rectangle2D;
import ucd.comp2011j.engine.Game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefenderGame implements Game {
    private int playerLives;
    private int playerScore;
    private boolean pause = false;
    public static final int SCREEN_WIDTH = 768;
    public static final int SCREEN_HEIGHT = 612;
    private static final int MARGIN_TOP = 60;
    private Player player;
    private PlayerListener listener;
    private List<Bullet> playerBullets;
    private List<Bullet> enemyBullets;
    private ArrayList<Hittable> targets; // List of all entities that can be hit
    public static final Rectangle2D MOVE_BOUNDS = new Rectangle2D(SCREEN_WIDTH / 4, MARGIN_TOP, SCREEN_WIDTH / 2, SCREEN_HEIGHT - MARGIN_TOP);
    public static final Rectangle2D SCREEN_BOUNDS = new Rectangle2D(0, MARGIN_TOP, SCREEN_WIDTH, SCREEN_HEIGHT - MARGIN_TOP);
    private Background background;
    private Level currentLevel;
    private static List<Explosion> explosions;

    public DefenderGame(PlayerListener playerListener) {
        this.listener = playerListener;
        startNewGame();
    }

    // Move the player and fire bullets
    private void movePlayer() {
        if (listener.isFiring()) {
            Bullet b = player.fire(listener.isMovingLeft() ? 180.0 : 0.0);
            if (b != null) {
                playerBullets.add(b);
            }
        }
        int x = 0, y = 0, speed = Player.SHIP_SPEED;
        if (listener.isMovingRight()) {
            x = speed;
        }
        if (listener.isMovingLeft()) {
            x = -speed;
        }
        if (listener.isMovingUp()) {
            y = -speed;
        }
        if (listener.isMovingDown()) {
            y = speed;
        }
        player.move(x, y);
    }

    private void moveBackground() {
        int speed = Background.BACKGROUND_SPEED;
        int direction = 0;
        // After the next move, if right edge of the player is at the right edge of the move bounds
        // and the player is moving right, then move the background left
        if (player.getX() + Player.SHIP_WIDTH + Player.SHIP_SPEED >= MOVE_BOUNDS.getMaxX() && listener.isMovingRight()) {
            background.move(-speed);
            direction = -1;
        } else if (player.getX() <= MOVE_BOUNDS.getMinX() && listener.isMovingLeft()) {
            background.move(speed);
            direction = 1;
        }

        // Wrap around the background
        background.setBackgroundX((int)Background.getPositiveModulo(background.getBackgroundX()));
        background.setDirection(direction);
    }

    // Move with background and special movement for each enemy type, and fire the bullets.
    private void moveEnemies() {
        int backgroundSpeed = Background.BACKGROUND_SPEED;
        int backgroundDirection = background.getDirection();
        for (Enemy enemy : currentLevel.getEnemies()) {
            enemy.moveWithBackground(0, 0, backgroundSpeed, backgroundDirection);
            if (enemy.getX() > 0 && enemy.getX() < SCREEN_WIDTH) {
                Bullet b = enemy.fire(player);
                if (b != null) {
                    enemyBullets.add(b);
                }
                if (enemy instanceof Mutant) {
                    ((Mutant) enemy).chasePlayer(player.getX(), player.getY());
                } else {
                    ((Lander) enemy).captureHuman(currentLevel.getHumans());
                }
            }
        }
    }

    private void moveHumans() {
        int backgroundSpeed = Background.BACKGROUND_SPEED;
        int backgroundDirection = background.getDirection();
        for (Human human : currentLevel.getHumans()) {
            human.moveWithBackground(0, 0, backgroundSpeed, backgroundDirection);
            if (human.isFalling()) {
                human.fall();
            } else if (human.isSaved() && human.getY() < DefenderGame.SCREEN_HEIGHT - 50) {
                human.setX(player.getX() + 8 * Player.SHIP_SCALE);
                human.setY(player.getY() - 4 * Player.SHIP_SCALE);
            } else if (human.isSaved() && human.getY() >= DefenderGame.SCREEN_HEIGHT - 50) {
                human.setSaved(false);
                playerScore += human.getPoints();
                player.setSavedHuman(null);
            }
        }
    }

    private void movePlayerBullets() {
        List<Bullet> remove = new ArrayList<Bullet>();
        for (Bullet playerBullet : playerBullets) {
            if (playerBullet.isAlive() && playerBullet.getHitBox().intersects(SCREEN_BOUNDS)) {
                playerBullet.move();
                playerBullet.moveWithBackground(0, 0, Background.BACKGROUND_SPEED, background.getDirection());
                // Check if the bullet hits any target
                for (Hittable t : targets) {
                    if (t != playerBullet && !t.isPlayer()) {
                        if (t.isHit(playerBullet)) {
                            playerBullet.setAlive(false);
                        }
                    }
                }
            } else {
                remove.add(playerBullet);
            }
        }
        playerBullets.removeAll(remove);
    }

    private void moveEnemyBullets() {
        List<Bullet> remove = new ArrayList<Bullet>();
        for (Bullet b : enemyBullets) {
            if (b.isAlive() && b.getHitBox().intersects(SCREEN_BOUNDS)) {
                b.move();
                b.moveWithBackground(0, 0, Background.BACKGROUND_SPEED, background.getDirection());
                // Check if the bullet hits any target
                for (Hittable t : targets) {
                    if (t != b && t.isPlayer() && !player.isInvincible()) {
                        if (t.isHit(b)) {
                            if (t.isPlayer()) {
                                player.setAlive(false);
                                playerLives--;
                                b.setAlive(false);
                            }
                        }
                    }
                }
            } else {
                remove.add(b);
            }
        }
        enemyBullets.removeAll(remove);
    }

    private void checkPlayerEnemyCollision() {
        for (Enemy enemy : currentLevel.getEnemies()) {
            if (!player.isInvincible() && player.getHitBox().intersects(enemy.getHitBox())) {
                playerLives--;
                player.setAlive(false);
            }
        }
    }

    private void checkPlayerHumanCollision() {
        for (Human human : currentLevel.getHumans()) {
            if (human.isFalling()) {
                if (player.getSavedHuman() == null && player.getHitBox().intersects(human.getHitBox()) && !human.isSaved()) {
                    human.setFalling(false);
                    human.setSaved(true);
                    player.setSavedHuman(human);
                }
            }
        }
    }

    public static void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    // Update the explosions and remove the finished ones
    private void updateExplosions() {
        List<Explosion> finishedExplosions = new ArrayList<>();
        for (Explosion explosion : explosions) {
            explosion.update();
            if (explosion.isFinished()) {
                finishedExplosions.add(explosion);
            }
        }
        explosions.removeAll(finishedExplosions);
    }

    // Use bomb, bomb can destroy all enemies on the screen
    private void useBomb() {
        if (listener.isUsingBomb() && player.getBombCount() > 0) {
            player.useBomb();
            for (Enemy enemy : currentLevel.getEnemies()) {
                // A bomb can be regarded as a bullet with a large hit box. It can increase code reusability.
                if (enemy.getHitBox().intersects(SCREEN_BOUNDS) && enemy.isHit(new Bullet(0, 0, 0, null, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT, 0))) {
                    addExplosion(new Explosion(enemy.getX(), enemy.getY(), 30, enemy.getType()));
                }
            }
        }
    }

    // Move all dead entities with the background and calculate the points
    public void removeDeadEntities() {
        // Remove dead humans
        Iterator<Human> humanIterator = currentLevel.getHumans().iterator();
        while (humanIterator.hasNext()) {
            Human human = humanIterator.next();
            if (!human.isAlive()) {
                System.out.println(playerScore);
                humanIterator.remove();
                this.playerScore -= 300;
                // The human is captured by the lander, and it's mutated, the player decrease 150 points.
                // However, the lander captures the human, when the human is at the top of the screen, the lander is also destroyed.
                // Then the player will get 150 points for destroying the lander. This is not considered in the original code.
                // So the playerScore should be decreased by additional 150 points. Therefore, the playerScore should be decreased by 300 points.
                System.out.println(playerScore);
            } else if (human.isFalling() && human.getY() > SCREEN_HEIGHT) {
                humanIterator.remove();
            }
        }

        // Remove dead enemies
        Iterator<Enemy> enemyIterator = currentLevel.getEnemies().iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (!enemy.isAlive()) {
                enemyIterator.remove();
                playerScore += enemy.getPoints();
            }
        }
    }

    @Override
    public int getPlayerScore() {
        return playerScore;
    }

    @Override
    public void updateGame() {
        if (!isPaused() && getPlayerLives() > 0) {
            player.tickWeapon();
            player.tickInvincibility();
            targets.clear();
            targets.addAll(currentLevel.getEnemies());
            targets.add(player);
            useBomb();
            movePlayer();
            movePlayerBullets();
            moveEnemyBullets();
            moveBackground();
            moveEnemies();
            moveHumans();
            checkPlayerEnemyCollision();
            checkPlayerHumanCollision();
            removeDeadEntities();
            updateExplosions();
        }
    }

    @Override
    public boolean isPaused() {
        return pause;
    }

    @Override
    public void checkForPause() {
        if (listener.isPaused()) {
            pause = !pause;
            listener.resetPause();
        }
    }

    @Override
    public void startNewGame() {
        targets = new ArrayList<Hittable>();
        player = new Player(2);
        playerLives = 3;
        playerScore = 0;
        playerBullets = new ArrayList<Bullet>();
        enemyBullets = new ArrayList<Bullet>();
        currentLevel = new Level(1);
        this.background = new Background();
        this.explosions = new ArrayList<>();
    }

    @Override
    public boolean isLevelFinished() {
        // The level is finished if there are no enemies left and none of the humans are falling
        return currentLevel.getLevelNumber() <= currentLevel.getMaxLevel() && currentLevel.getEnemies().isEmpty() && !currentLevel.checkHumans();
    }

    @Override
    public boolean isPlayerAlive() {
        return player.isAlive();
    }

    @Override
    public void resetDestroyedPlayer() {
        player.setAlive(true);
        playerBullets = new ArrayList<Bullet>();
        enemyBullets = new ArrayList<Bullet>();
    }

    @Override
    public void moveToNextLevel() {
        pause = true;
        currentLevel = new Level(currentLevel.getLevelNumber() + 1);
        player.resetDestroyed();
        playerBullets = new ArrayList<Bullet>();
        enemyBullets = new ArrayList<Bullet>();
        listener.resetAll();
    }

    @Override
    public boolean isGameOver() {
        return !(playerLives > 0 && currentLevel.getLevelNumber() <= currentLevel.getMaxLevel());
    }

    public boolean isWin() {
        return currentLevel.getLevelNumber() > currentLevel.getMaxLevel();
    }

    public Player getShip() {
        return player;
    }

    public List<Bullet> getBullets() {
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        bullets.addAll(playerBullets);
        bullets.addAll(enemyBullets);
        return bullets;
    }

    public Background getBackground() {
        return background;
    }

    public List<Enemy> getEnemies() {
        return currentLevel.getEnemies();
    }

    public List<Human> getHumans() {
        return currentLevel.getHumans();
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public int getLevelNumber() {
        return currentLevel.getLevelNumber();
    }

    public List<Explosion> getExplosions() {
        return explosions;
    }

    public PlayerListener getListener() {
        return listener;
    }
}
