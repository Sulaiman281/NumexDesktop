package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Singleton;
import model.Position;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PositionTab {
    ScrollPane scrollPane;
    VBox vBox;

    public PositionTab(){
        initialize();
    }
    void initialize(){
        scrollPane = new ScrollPane();
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        scrollPane.setContent(vBox);

        String query = "SELECT * FROM position;";
        Connection con = Singleton.getInstance().db.connector();

        try {
            ResultSet set = con.prepareStatement(query).executeQuery();

            while(set.next()){
                Position position = new Position();
                position.setId(set.getInt("id"));
                position.setEntryDate(set.getString("entryDate"));
                position.setTradeId(set.getInt("tradeID"));
                position.setDaysLeft(set.getInt("daysLeft"));
                addItem(position);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void addItem(Position position) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);

        box.getChildren().add(getLabel("Entry Date: "+position.getEntryDate()));
        box.getChildren().add(getLabel("Trade ID: "+position.getTradeId()));
        box.getChildren().add(getLabel("Days Left: "+position.getDaysLeft()));

        vBox.getChildren().add(box);
    }

    Label getLabel(String str){
        Label label = new Label(str);
        label.setFont(Font.font("Arial",16));
        return label;
    }

    public ScrollPane getRoot() {
        return scrollPane;
    }
}
