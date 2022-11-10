package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(Singleton.getInstance().getView("OpeningScreen").getRoot()));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
