package core;

import javafx.application.Application;
import javafx.stage.Stage;
import screens.MainMenuScreen;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setHeight(900);
        primaryStage.setWidth(900);
        
        LevelList l = new LevelList();

        MainMenuScreen mainMenu = new MainMenuScreen(primaryStage, l);
        mainMenu.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
