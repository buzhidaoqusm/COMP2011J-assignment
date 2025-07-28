package model;

import javafx.geometry.Rectangle2D;

public class Player implements Hittable{
    private int x;
    private int y;
    private Rectangle2D hitBox;
    private int weaponCountdown; // Countdown to next weapon fire
    private boolean alive = true;
    private boolean invincible = false;
    private int invincibilityCountdown = 0;
    private static final int INVINCIBILITY_DURATION = 100; // Duration of invincibility in ticks
    public static final int SHIP_SCALE = 3;
    public static final int SHIP_HEIGHT = 6 * SHIP_SCALE;
    public static final int SHIP_WIDTH = 18 * SHIP_SCALE;
    public static final int SHIP_SPEED = 8;
    private int bombCount;
    private Human savedHuman;

    public Player(int bombCount) {
        x = DefenderGame.SCREEN_WIDTH / 2 - SHIP_WIDTH / 2;
        y = DefenderGame.SCREEN_HEIGHT / 2 - SHIP_HEIGHT / 2;
        // Only the central part can be seen as hitbox.
        hitBox = new Rectangle2D(x + 3 * SHIP_SCALE, y + SHIP_SCALE, 15 * SHIP_SCALE, 5 * SHIP_SCALE);
        this.bombCount = bombCount;
    }

    public void move(int x1, int y1) {
        Rectangle2D newBox = new Rectangle2D(x + x1 + 3 * SHIP_SCALE, y + y1 + SHIP_SCALE, 15 * SHIP_SCALE, 5 * SHIP_SCALE);
        if (DefenderGame.MOVE_BOUNDS.contains(newBox)) {
            hitBox = newBox;
            this.x += x1;
            this.y += y1;
        } else {
            // Check horizontal movement
            Rectangle2D horizontalBox = new Rectangle2D(x + x1 + 3 * SHIP_SCALE, y + SHIP_SCALE, 15 * SHIP_SCALE, 5 * SHIP_SCALE);
            if (DefenderGame.MOVE_BOUNDS.contains(horizontalBox)) {
                hitBox = horizontalBox;
                this.x += x1;
            }
            // Check vertical movement
            Rectangle2D verticalBox = new Rectangle2D(x + 3 * SHIP_SCALE, y + y1 + SHIP_SCALE, 15 * SHIP_SCALE, 5 * SHIP_SCALE);
            if (DefenderGame.MOVE_BOUNDS.contains(verticalBox)) {
                hitBox = verticalBox;
                this.y += y1;
            }
        }
    }

    public Bullet fire(double angle) {
        Bullet b = null;
        if (weaponCountdown == 0) {
            b = new Bullet(x + 3 * SHIP_SCALE, y + 4 * SHIP_SCALE, angle, "player", 6, 3, 14);
            weaponCountdown = 15;
        }
        return b;
    }

    public void tickWeapon() {
        if (weaponCountdown > 0) {
            weaponCountdown--;
        }
    }

    // Reset player to the center of the screen
    public void resetDestroyed() {
        alive = true;
        x = DefenderGame.SCREEN_WIDTH / 2 - SHIP_WIDTH / 2;
        y = DefenderGame.SCREEN_HEIGHT / 2 - SHIP_HEIGHT / 2;
        hitBox = new Rectangle2D(x + 3 * SHIP_SCALE, y + SHIP_SCALE, 15 * SHIP_SCALE, 5 * SHIP_SCALE);
    }

    public void startInvincibility() {
        invincible = true;
        invincibilityCountdown = INVINCIBILITY_DURATION;
    }

    public void tickInvincibility() {
        if (invincibilityCountdown > 0) {
            invincibilityCountdown--;
            if (invincibilityCountdown == 0) {
                invincible = false;
            }
        }
    }

    public void useBomb() {
        if (bombCount > 0 && weaponCountdown == 0) {
            bombCount--;
            weaponCountdown = 15;
        }
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
        return true;
    }

    @Override
    public boolean isHit(Bullet b) {
        boolean hit = hitBox.intersects(b.getHitBox());
        if (hit) {
            alive = false;
        }
        return hit;
    }

    @Override
    public Rectangle2D getHitBox() {
        return hitBox;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isInvincible() {
        return invincible;
    }
    public int getInvincibilityCountdown() {
        return invincibilityCountdown;
    }
    public int getBombCount() {
        return bombCount;
    }
    public Human getSavedHuman() {
        return savedHuman;
    }

    public void setSavedHuman(Human savedHuman) {
        this.savedHuman = savedHuman;
    }

    public void setAlive(boolean alive) {
        if (!alive) {
            startInvincibility();
        }
        this.alive = alive;
    }


}
