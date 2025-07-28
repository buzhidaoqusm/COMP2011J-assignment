package display;

import javafx.scene.Scene;

public class PlayerListener {
    private boolean movingLeft;
    private boolean movingRight;
    private boolean movingUp;
    private boolean movingDown;
    private boolean fire;
    private boolean pause;
    private boolean useBomb;

    public void setListeners(Scene s) {
        s.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    movingUp = true;
                    break;
                case DOWN:
                    movingDown = true;
                    break;
                case LEFT:
                    movingLeft = true;
                    movingRight = false;
                    break;
                case RIGHT:
                    movingRight = true;
                    movingLeft = false;
                    break;
                case SPACE:
                    fire = true;
                    break;
                case P:
                    pause = true;
                    break;
                case Z:
                    useBomb = true;
                    break;
                default:
                    break;
            }
        });

        s.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    movingUp = false;
                    break;
                case DOWN:
                    movingDown = false;
                    break;
                case SPACE:
                    fire = false;
                    break;
                case Z:
                    useBomb = false;
                    break;
                default:
                    break;
            }
        });
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isFiring() {
        return fire;
    }

    public boolean isPaused() {
        return pause;
    }

    public void resetPause() {
        pause = false;
    }

    public boolean isUsingBomb() {
        return useBomb;
    }

    public void resetAll() {
        movingUp = false;
        movingDown = false;
        movingRight = false;
        movingLeft = false;
        fire = false;
        pause = false;
        useBomb = false;
    }
}
