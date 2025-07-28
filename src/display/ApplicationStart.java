package display;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.DefenderGame;
import ucd.comp2011j.engine.GameManager;
import ucd.comp2011j.engine.ScoreKeeper;

public class ApplicationStart extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, DefenderGame.SCREEN_WIDTH, DefenderGame.SCREEN_HEIGHT);
        PlayerListener playerListener = new PlayerListener();
        playerListener.setListeners(scene);
        MenuListener menuListener = new MenuListener();
        menuListener.setListeners(scene);
        primaryStage.setTitle("Defender");
        DefenderGame game = new DefenderGame(playerListener);
        GameScreen gameScreen = new GameScreen(game);
        MenuScreen menuScreen = new MenuScreen();
        ScoreKeeper scoreKeeper = new ScoreKeeper("scores.txt");
        GameManager gameManager = new GameManager(game, root, menuListener, menuScreen,new AboutScreen(),new ScoreScreen(scoreKeeper), gameScreen, scoreKeeper);
        menuScreen.paint();
        primaryStage.setScene(scene);
        primaryStage.show();
        gameManager.run();
    }
}
