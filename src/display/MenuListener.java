package display;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import ucd.comp2011j.engine.MenuCommands;

public class MenuListener implements MenuCommands {
    private boolean about;
    private boolean exit;
    private boolean score;
    private boolean menu;
    private boolean newGame;

    public void setListeners(Scene s) {
        s.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent e) {
                if ("A".equalsIgnoreCase(e.getCharacter())) {
                    about = true;
                } else if ("X".equalsIgnoreCase(e.getCharacter())) {
                    exit = true;
                } else if ("H".equalsIgnoreCase(e.getCharacter())) {
                    score = true;
                } else if ("M".equalsIgnoreCase(e.getCharacter())) {
                    menu = true;
                } else if ("N".equalsIgnoreCase(e.getCharacter())) {
                    newGame = true;
                }
            }
        });
    }
    @Override
    public boolean hasPressedNewGame() {
        return newGame;
    }

    @Override
    public boolean hasPressedAboutScreen() {
        return about;
    }

    @Override
    public boolean hasPressedHighScoreScreen() {
        return score;
    }

    @Override
    public boolean hasPressedMenu() {
        return menu;
    }

    @Override
    public boolean hasPressedExit() {
        return exit;
    }

    @Override
    public void resetKeyPresses() {
        menu = false;
        about = false;
        newGame = false;
        score = false;
        exit = false;
    }
}
