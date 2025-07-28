package display;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.DefenderGame;
import ucd.comp2011j.engine.Screen;

public class MenuScreen implements Screen {

    private Canvas canvas;

    public MenuScreen() {
        canvas = new Canvas(DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void paint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        int marginTop = 32;
        gc.setFont(new Font("Arial", 36));
        gc.setFill(Color.GREEN);
        gc.fillText("Welcome to Defender!!!!", DefenderGame.SCREEN_WIDTH/2, DefenderGame.SCREEN_HEIGHT / 32 + marginTop);
        gc.setFont(new Font("Arial", 24));
        gc.fillText("To play a game press N", DefenderGame.SCREEN_WIDTH/2, DefenderGame.SCREEN_HEIGHT / 5 + marginTop);
        gc.fillText("To see the controls press A", DefenderGame.SCREEN_WIDTH/2, 2 * DefenderGame.SCREEN_HEIGHT / 5 + marginTop);
        gc.fillText("To see the High scores press H", DefenderGame.SCREEN_WIDTH/2, 3 * DefenderGame.SCREEN_HEIGHT / 5 + marginTop);
        gc.fillText("To exit press X", DefenderGame.SCREEN_WIDTH/2, 4 * DefenderGame.SCREEN_HEIGHT / 5 + marginTop);
    }
}
