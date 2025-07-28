package display;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.*;
import ucd.comp2011j.engine.Screen;

import java.util.Random;

public class GameScreen implements Screen {
    private DefenderGame game;
    private Canvas canvas;

    public GameScreen(DefenderGame game) {
        this.game = game;
        this.canvas = new Canvas(DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
    }

    /**
     * Draws with 2D array and colors on the given GraphicsContext.
     *
     * @param gc        the GraphicsContext to draw on
     * @param array     the 2D array to be drawn, the value of the array is the index of the color in the colors array
     * @param x         the x coordinate of the top-left corner of the array
     * @param y         the y coordinate of the top-left corner of the array
     * @param pixelSize the size of the pixel
     * @param colors    the colors to be used in the array
     */
    private void drawWith2DArray(GraphicsContext gc, int[][] array, double x, double y, int pixelSize, Color[] colors) {
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                if (array[row][col] != 0) {
                    gc.setFill(colors[array[row][col]]);
                    gc.fillRect(x + col * pixelSize, y + row * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext gc, Player p) {
        int x = p.getX();
        int y = p.getY();
        int pixelSize = Player.SHIP_SCALE;  // The size of every pixel

        // Define the color layout of the spaceship(0: transparent, 1: gray, 2: magenta, 3: green, 4: red, 5: blue, 6: orange, 7: white) 6*18
        int[][] spaceshipPixels = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 3},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 3, 0, 3, 4},
                {0, 0, 0, 6, 5, 5, 1, 1, 1, 1, 1, 2, 2, 3, 0, 0, 2, 0},
                {3, 7, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3, 0, 0, 3, 5},
                {0, 0, 0, 0, 0, 0, 0, 3, 7, 1, 2, 2, 2, 0, 0, 0, 4, 5}
        };
        Color[] colors = {Color.TRANSPARENT, Color.GRAY, Color.MAGENTA, Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE, Color.WHITE};
        // Draw the spaceship by the color layout
        if (game.getListener().isMovingLeft()) {
            // If the spaceship is moving left, draw the spaceship normally
            drawWith2DArray(gc, spaceshipPixels, x, y, pixelSize, colors);
        } else {
            // If the spaceship is moving right, flip the spaceship horizontally
            for (int row = 0; row < spaceshipPixels.length; row++) {
                for (int col = 0; col < spaceshipPixels[row].length; col++) {
                    if (spaceshipPixels[row][col] != 0) {
                        gc.setFill(colors[spaceshipPixels[row][col]]);
                        gc.fillRect(x + (spaceshipPixels[row].length - col) * pixelSize, y + row * pixelSize, pixelSize, pixelSize);
                    }
                }
            }
        }
    }

    private void drawBullet(GraphicsContext gc, Bullet b) {
        if (b.getName().equals("player")) {
            // draw the player's bullet, whose hitbox is a white rectangle
            gc.setFill(Color.WHITE);
            gc.fillRect(b.getX(), b.getY(), b.getBulletWidth(), b.getBulletHeight());

            // draw the trail of the player's bullet, which is a purple rectangle
            // the length of the trail decreasing as the bullet moves further
            // and the transparency of the trail decreases as the bullet moves further
            int maxTrailLength = 60;
            int originX = b.getOrigin()[0];
            Random rand = new Random();
            // if the bullet shoots to the right
            if (b.getAngle() == 0.0) {
                for (int x = b.getX() - b.getBulletWidth(), i = 0; x > originX && i < maxTrailLength; x -= 4, i++) {
                    double opacity = (double) (x - originX) / (b.getX() - originX);
                    gc.setFill(Color.PURPLE.deriveColor(0, 1, 1, opacity));
                    gc.fillRect(x, b.getY(), 4, b.getBulletHeight());

                    // the trail is not continuous, but with some randomness
                    if (rand.nextDouble() < Math.pow((double) (x - originX) / 60, 2)) {
                        x -= 4;
                        i++;
                    }
                }
            } else {
                // if the bullet shoots to the left
                for (int x = b.getX() + b.getBulletWidth(), i = 0; x < originX && i < maxTrailLength; x += 4, i++) {
                    double opacity = (double) (originX - x) / (originX - b.getX());
                    gc.setFill(Color.PURPLE.deriveColor(0, 1, 1, opacity));
                    gc.fillRect(x, b.getY(), 4, b.getBulletHeight());
                    if (rand.nextDouble() < Math.pow((double) (originX - x) / 60, 2)) {
                        x += 4;
                        i++;
                    }
                }
            }
        } else if (b.getName().equals("lander")) {
            int x = b.getX();
            int y = b.getY();

            gc.setFill(Color.YELLOW);
            gc.fillRect(x, y, 3, 3);
            gc.fillRect(x + 3, y + 3, 3, 3);
            gc.setFill(Color.RED);
            gc.fillRect(x + 3, y, 3, 3);
            gc.fillRect(x, y + 3, 3, 3);
        } else {
            int x = b.getX();
            int y = b.getY();

            gc.setFill(Color.WHITE);
            gc.fillRect(x - 3, y, 3, 3);
            gc.fillRect(x, y + 3, 3, 3);
            gc.fillRect(x + 3, y, 3, 3);
            gc.fillRect(x, y - 3, 3, 3);
            gc.setFill(Color.MAGENTA);
            gc.fillRect(x, y, 3, 3);
        }
    }

    /**
     * Draws the draw the slash in background on the given GraphicsContext.
     *
     * @param gc        the GraphicsContext to draw on
     * @param x         the background to be drawn
     * @param y         the background to be drawn
     * @param direction the direction of the slash, the first element is the x direction, the second element is the y direction
     * @param length    the length of the slash
     * @param pixelSize the size of the pixel
     */
    private void drawSlash(GraphicsContext gc, int x, int y, int[] direction, int length, int pixelSize) {
        gc.setFill(Color.ORANGE);
        for (int i = 0; i < length; i++) {
            gc.fillRect(x + direction[0] * pixelSize * i, y + direction[1] * pixelSize * i, pixelSize, pixelSize);
        }

    }

    int PositiveModulo(int i, int n) {
        // 200 is for reserving space for the negative number
        // So that the background can show correctly. Otherwise, the left part of the background will be empty often.
        return (i % n + n + 200) % n - 200;
    }

    /**
     * Draws the mountain range as background on the given GraphicsContext.
     */
    private void drawBackground(GraphicsContext gc, Background bg) {
        int pixelSize = Background.PIXEL_SIZE;
        int backgroundX = bg.getBackgroundX();
        int backgroundY = Background.BACKGROUND_Y;
        int backgroundWidth = Background.BACKGROUND_WIDTH;

        gc.setFill(Color.ORANGE);
        gc.fillRect(PositiveModulo(backgroundX, backgroundWidth), backgroundY, 60 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 60 * pixelSize, backgroundWidth), backgroundY, new int[]{1, 1}, 13, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 73 * pixelSize, backgroundWidth), backgroundY + 13 * pixelSize, new int[]{1, -1}, 35, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 108 * pixelSize, backgroundWidth), backgroundY - 22 * pixelSize, new int[]{1, 1}, 22, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 130 * pixelSize, backgroundWidth), backgroundY, 40 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 170 * pixelSize, backgroundWidth), backgroundY, new int[]{1, -1}, 6, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 176 * pixelSize, backgroundWidth), backgroundY - 6 * pixelSize, new int[]{1, 1}, 14, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 190 * pixelSize, backgroundWidth), backgroundY + 8 * pixelSize, new int[]{1, -1}, 30, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 220 * pixelSize, backgroundWidth), backgroundY - 22 * pixelSize, new int[]{1, 1}, 18, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 238 * pixelSize, backgroundWidth), backgroundY - 4 * pixelSize, new int[]{1, -1}, 10, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 248 * pixelSize, backgroundWidth), backgroundY - 14 * pixelSize, new int[]{1, 1}, 14, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 262 * pixelSize, backgroundWidth), backgroundY, 25 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 287 * pixelSize, backgroundWidth), backgroundY, new int[]{1, 1}, 4, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 291 * pixelSize, backgroundWidth), backgroundY + 4 * pixelSize, 40 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 331 * pixelSize, backgroundWidth), backgroundY + 4 * pixelSize, new int[]{1, -1}, 4, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 335 * pixelSize, backgroundWidth), backgroundY, 20 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 355 * pixelSize, backgroundWidth), backgroundY, new int[]{1, 1}, 4, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 359 * pixelSize, backgroundWidth), backgroundY + 4 * pixelSize, 25 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 384 * pixelSize, backgroundWidth), backgroundY + 4 * pixelSize, new int[]{1, -1}, 6, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 390 * pixelSize, backgroundWidth), backgroundY - 2 * pixelSize, new int[]{1, 1}, 6, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 396 * pixelSize, backgroundWidth), backgroundY + 4 * pixelSize, 20 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 416 * pixelSize, backgroundWidth), backgroundY + 4 * pixelSize, new int[]{1, -1}, 6, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 422 * pixelSize, backgroundWidth), backgroundY - 2 * pixelSize, new int[]{1, 1}, 6, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 428 * pixelSize, backgroundWidth), backgroundY + 4 * pixelSize, 30 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 458 * pixelSize, backgroundWidth), backgroundY + 4 * pixelSize, new int[]{1, -1}, 20, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 478 * pixelSize, backgroundWidth), backgroundY - 16 * pixelSize, new int[]{1, 1}, 12, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 490 * pixelSize, backgroundWidth), backgroundY - 4 * pixelSize, new int[]{1, -1}, 6, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 496 * pixelSize, backgroundWidth), backgroundY - 10 * pixelSize, new int[]{1, 1}, 10, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 506 * pixelSize, backgroundWidth), backgroundY, new int[]{1, -1}, 15, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 521 * pixelSize, backgroundWidth), backgroundY - 15 * pixelSize, new int[]{1, 1}, 15, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 536 * pixelSize, backgroundWidth), backgroundY, 30 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 566 * pixelSize, backgroundWidth), backgroundY, new int[]{1, -1}, 6, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 572 * pixelSize, backgroundWidth), backgroundY - 6 * pixelSize, 15 * pixelSize, pixelSize);
        drawSlash(gc, PositiveModulo(backgroundX + 587 * pixelSize, backgroundWidth), backgroundY - 6 * pixelSize, new int[]{1, 1}, 6, pixelSize);
        gc.fillRect(PositiveModulo(backgroundX + 593 * pixelSize, backgroundWidth), backgroundY, 7 * pixelSize, pixelSize);
    }

    void drawEnemyLander(GraphicsContext gc, Enemy es) {
        Color[] colors = {Color.TRANSPARENT, Color.GREEN, Color.YELLOW};
        // 0: transparent, 1: green, 2: yellow
        int[][] enemyShipPixels = {
                {0, 0, 0, 2, 2, 2, 0, 0, 0},
                {0, 0, 1, 2, 2, 2, 1, 0, 0},
                {0, 1, 1, 0, 1, 1, 0, 1, 0},
                {0, 1, 1, 0, 1, 1, 0, 1, 0},
                {0, 0, 1, 2, 1, 2, 1, 0, 0},
                {0, 1, 0, 0, 1, 0, 0, 1, 0},
                {1, 0, 0, 0, 1, 0, 0, 0, 1},
        };
        double x = es.getX();
        double y = es.getY();
        int pixelSize = Enemy.getEnemyScale();
        drawWith2DArray(gc, enemyShipPixels, x, y, pixelSize, colors);
    }

    void drawEnemyMutant(GraphicsContext gc, Enemy es) {
        Color[] colors = {Color.TRANSPARENT, Color.RED, Color.GREEN};
        // 0: transparent, 1: red, 2: green
        int[][] enemyShipPixels = {
                {0, 0, 1, 0, 0},
                {0, 1, 1, 1, 0},
                {1, 2, 1, 2, 1},
                {0, 1, 1, 1, 0}
        };
        double x = es.getX();
        double y = es.getY();
        int pixelSize = Enemy.getEnemyScale();
        drawWith2DArray(gc, enemyShipPixels, x, y, pixelSize, colors);
    }

    void drawHuman(GraphicsContext gc, Human h) {
        Color[] colors = {Color.TRANSPARENT, Color.MAGENTA, Color.GREEN, Color.BROWN, Color.YELLOW};
        // 0: transparent, 1: magenta, 2: green, 3: brown, 4: yellow
        int[][] humanPixels = {
                {2, 2, 0},
                {4, 2, 0},
                {4, 2, 1},
                {1, 3, 1},
                {1, 3, 1},
                {0, 3, 0},
                {0, 3, 0},
                {0, 3, 0}
        };
        double x = h.getX();
        double y = h.getY();
        int pixelSize = h.getHumanScale();
        drawWith2DArray(gc, humanPixels, x, y, pixelSize, colors);
    }

    /**
     * Draws the 2D array on the given GraphicsContext with an offset.
     *
     * @param gc        the GraphicsContext to draw on
     * @param array     the 2D array to be drawn, the value of the array is the index of the color in the colors array
     * @param colors    the colors to be used in the array
     * @param x         the x coordinate of the top-left corner of the array
     * @param y         the y coordinate of the top-left corner of the array
     * @param deltaX    the x offset of the array
     * @param deltaY    the y offset of the array
     * @param pixelSize the size of the pixel
     * */
    private void movePixelBlock(GraphicsContext gc, int[][] array, Color[] colors, double x, double y, double deltaX, double deltaY, int pixelSize) {
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                if (array[row][col] != 0) {
                    gc.setFill(colors[array[row][col]]);
                    gc.fillRect(x + col * pixelSize + deltaX, y + row * pixelSize + deltaY, pixelSize, pixelSize);
                }
            }
        }
    }

    /**
     * Draws the explosion blocks on the given GraphicsContext.
     *
     * @param gc        the GraphicsContext to draw on
     * @param x         the x coordinate of the top-left corner of the explosion
     * @param y         the y coordinate of the top-left corner of the explosion
     * @param currentTick the current tick of the explosion
     * @param speed     the speed of the explosion
     * @param colors    the colors to be used in the explosion
     * */
    private void drawExplosionBlocks(GraphicsContext gc, double x, double y, int currentTick, double speed, Color[] colors) {
        movePixelBlock(gc, new int[][]{{1, 0}, {1, 1}}, colors, x, y, currentTick * speed, -2 * currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{1, 0}, {1, 1}}, colors, x, y, 2 * currentTick * speed, currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{1, 1}, {1, 0}}, colors, x, y, 2 * currentTick * speed, -currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{1, 1}, {1, 0}}, colors, x, y, currentTick * speed, 2 * currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{1, 1}, {0, 1}}, colors, x, y, -currentTick * speed, 2 * currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{1, 1}, {0, 1}}, colors, x, y, -2 * currentTick * speed, currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{0, 1}, {1, 1}}, colors, x, y, -2 * currentTick * speed, -currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{0, 1}, {1, 1}}, colors, x, y, -currentTick * speed, -2 * currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{2, 2}, {2, 2}}, colors, x, y, currentTick * speed, currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{2, 2}, {2, 2}}, colors, x, y, -currentTick * speed, -currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{2, 2}, {2, 2}}, colors, x, y, -currentTick * speed, currentTick * speed, 3);
        movePixelBlock(gc, new int[][]{{2, 2}, {2, 2}}, colors, x, y, currentTick * speed, -currentTick * speed, 3);
    }

    private void drawExplosion(GraphicsContext gc, Explosion explosion) {
        double x = explosion.getX();
        double y = explosion.getY();
        int currentTick = explosion.getCurrentTick();
        double speed = 1.2;

        // The difference of explosion between lander and mutant is only the color
        Color[] colors;
        if (explosion.getEnemyType().equals(AlienType.Lander)) {
            colors = new Color[]{Color.TRANSPARENT, Color.GREEN, Color.YELLOW};
        } else {
            colors = new Color[]{Color.TRANSPARENT, Color.RED, Color.YELLOW};
        }
        drawExplosionBlocks(gc, x, y, currentTick, speed, colors);
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void paint() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
        if (game != null) {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
            Player player = game.getShip();
            // If the player is invincible, the spaceship will blink
            if (!(player.isInvincible() && player.getInvincibilityCountdown() / 2 % 2 == 0)) {
                drawPlayer(gc, game.getShip());
            }
            drawBackground(gc, game.getBackground());
            gc.setFill(Color.GREEN);
            gc.fillRect(0, 60, DefenderGame.SCREEN_WIDTH, 4);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.TOP);
            gc.setFont(new Font("Arial", 24));
            gc.fillText("Lives: " + game.getPlayerLives(), 0, 0);
            gc.fillText("Bombs: " + player.getBombCount(), 0, 30);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("Level: " + game.getLevelNumber(), DefenderGame.SCREEN_WIDTH / 2, 0);
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText("Score: " + game.getPlayerScore(), DefenderGame.SCREEN_WIDTH, 0);
            for (Bullet bullet : game.getBullets()) {
                drawBullet(gc, bullet);
            }
            for (Enemy enemy : game.getEnemies()) {
                if (enemy.getType().equals(AlienType.Lander)) {
                    drawEnemyLander(gc, enemy);
                } else {
                    drawEnemyMutant(gc, enemy);
                }
            }
            for (Human human : game.getHumans()) {
                drawHuman(gc, human);
            }
            for (Explosion explosion : game.getExplosions()) {
                drawExplosion(gc, explosion);
            }
            if (game.isPaused()) {
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(new Font("Arial", 24));
                gc.setFill(Color.WHITE);
                gc.fillText("Press 'p' to continue", DefenderGame.SCREEN_WIDTH / 2, DefenderGame.SCREEN_HEIGHT / 2);
            } else if (game.isPlayerAlive() && game.isWin()) {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(new Font("Arial", 48));
                gc.setFill(Color.WHITE);
                gc.fillText("You Have Passed All the Levels!", DefenderGame.SCREEN_WIDTH / 2, DefenderGame.SCREEN_HEIGHT / 2);
            } else if (game.getPlayerLives() <= 0) {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(new Font("Arial", 48));
                gc.setFill(Color.WHITE);
                gc.fillText("Game Over", DefenderGame.SCREEN_WIDTH / 2, DefenderGame.SCREEN_HEIGHT / 2);
            }
        }
    }
}
