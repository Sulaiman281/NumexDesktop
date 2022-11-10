package main;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import services.Database;

import java.io.IOException;

public class Singleton {

    public Database db;

    private static final Singleton singleton = new Singleton();

    private Singleton(){
        db = new Database();
    }
    public static Singleton getInstance(){
        return singleton;
    }

    public FXMLLoader getView(String _file){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/"+_file+".fxml"));
        try{
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    public void numField(TextField tf){
        tf.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                    ///   System.out.println("The char you entered is not a number");
                    t.consume();
                }
            }
        });
    }
}
