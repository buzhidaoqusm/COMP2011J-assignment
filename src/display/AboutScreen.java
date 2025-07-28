package display;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.DefenderGame;
import ucd.comp2011j.engine.Screen;

public class AboutScreen implements Screen {
    private Canvas canvas;

    public AboutScreen() {
        canvas = new Canvas(DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void paint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.setFill(Color.GREEN);
        gc.fillText("Defender Controls", DefenderGame.SCREEN_WIDTH / 2, 64);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Arial", 20));
        int start = 128;
        int gap = 30;
        gc.fillText("Move Up", 1 * DefenderGame.SCREEN_WIDTH / 6, start + 0 * gap);
        gc.fillText("↑", 4 * DefenderGame.SCREEN_WIDTH / 6, start + 0 * gap);
        gc.fillText("Move Down", 1 * DefenderGame.SCREEN_WIDTH / 6, start + 1 * gap);
        gc.fillText("↓", 4 * DefenderGame.SCREEN_WIDTH / 6, start + 1 * gap);
        gc.fillText("Move Left", 1 * DefenderGame.SCREEN_WIDTH / 6, start + 2 * gap);
        gc.fillText("←", 4 * DefenderGame.SCREEN_WIDTH / 6, start + 2 * gap);
        gc.fillText("Move Right", 1 * DefenderGame.SCREEN_WIDTH / 6, start + 3 * gap);
        gc.fillText("→", 4 * DefenderGame.SCREEN_WIDTH / 6, start + 3 * gap);
        gc.fillText("Fire", 1 * DefenderGame.SCREEN_WIDTH / 6, start + 4 * gap);
        gc.fillText("space bar", 4 * DefenderGame.SCREEN_WIDTH / 6, start + 4 * gap);
        gc.fillText("Use Bomb", 1 * DefenderGame.SCREEN_WIDTH / 6, start + 5 * gap);
        gc.fillText("z", 4 * DefenderGame.SCREEN_WIDTH / 6, start + 5 * gap);
        gc.fillText("Play/Pause", 1 * DefenderGame.SCREEN_WIDTH / 6, start + 6 * gap);
        gc.fillText("p", 4 * DefenderGame.SCREEN_WIDTH / 6, start + 6 * gap);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.fillText("Press 'M' to return to the Main Menu", DefenderGame.SCREEN_WIDTH / 2, 416);
    }
}
