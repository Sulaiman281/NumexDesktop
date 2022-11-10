package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Singleton;
import model.Trade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

public class CalenderTab {

    ScrollPane scrollPane;
    VBox root;

    public CalenderTab(){
        scrollPane = new ScrollPane();
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        scrollPane.setContent(root);

        String query = "SELECT * FROM trade;";
        Connection con = Singleton.getInstance().db.connector();

        try {
            ResultSet resultSet = con.prepareStatement(query).executeQuery();

            while(resultSet.next()){
                Trade trade = new Trade();
                trade.setId(resultSet.getInt("id"));
                trade.setName(resultSet.getString("name"));
                trade.setDates(resultSet.getString("dates"));
                trade.setBeforeAfterDays(resultSet.getString("beforeAfterDays"));

                addTrade(trade);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void addTrade(Trade trade){


        Date[] dates = sortDates(trade.getDates());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (Date date : dates) {
            if(LocalDate.now().isBefore(LocalDate.of( // checking if the trading date is history.
                    Integer.parseInt(format.format(date).split("-")[0]),
                    Integer.parseInt(format.format(date).split("-")[1]),
                    Integer.parseInt(format.format(date).split("-")[2])))) {


                HBox box = new HBox();
                box.setAlignment(Pos.CENTER_LEFT);
                box.setSpacing(20);

                // show the entry date label  after minus the before date like show the upcoming date
                LocalDate d = LocalDate.of(
                        Integer.parseInt(format.format(date).split("-")[0]),
                        Integer.parseInt(format.format(date).split("-")[1]),
                        Integer.parseInt(format.format(date).split("-")[2])
                ).minusDays(Long.parseLong(trade.getBeforeAfterDays().split(",")[0]));

                box.getChildren().add(getLabel("Entry Date: " + d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

                LocalDate after = LocalDate.of(
                        Integer.parseInt(format.format(date).split("-")[0]),
                        Integer.parseInt(format.format(date).split("-")[1]),
                        Integer.parseInt(format.format(date).split("-")[2])
                ).plusDays(Long.parseLong(trade.getBeforeAfterDays().split(",")[1]));
                //add days left.
                long leftDays = ChronoUnit.DAYS.between(d, LocalDate.of(
                        Integer.parseInt(format.format(date).split("-")[0]),
                        Integer.parseInt(format.format(date).split("-")[1]),
                        Integer.parseInt(format.format(date).split("-")[2])
                ));
                box.getChildren().add(getLabel("Days Left: " + leftDays));

                box.getChildren().add(getLabel("Trade Id: " + trade.getId()));

                Button active = getButton("Active");
                active.setOnAction(e -> {
                    // move to the position tab. how I suppose to do it let's just add into database for now. until i understand what's the purpose of it and then will do it more efficient.
                    String query = "INSERT INTO position (entryDate,tradeID,daysLeft) VALUES ('" +
                            format.format(date)+"',"+
                            trade.getId()+","+
                            leftDays+
                            ")";
                    Connection con = Singleton.getInstance().db.connector();
                    try {
                        con.prepareStatement(query).execute();
                    } catch (SQLException ignored) {
                    }
                });
                Button discard = getButton("Discard");
                discard.setOnAction(e -> {
                    // confirm what action should i person
                });
                box.getChildren().addAll(active, discard);
                root.getChildren().add(box);
            }else{
                System.out.println("Date is history: "+format.format(date));
            }
        }
    }

    Date[] sortDates(String str){
        String[] dates = str.split(",");
        Date[] d = new Date[dates.length];
        int index = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (String date : dates) {
            try {
                d[index++] = format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Arrays.sort(d);
        return d;
    }

    Label getLabel(String str){
        Label label = new Label(str);
        label.setFont(Font.font("Arial",16));
        return label;
    }
    Button getButton(String str){
        Button btn = new Button(str);
        btn.setFont(Font.font("Arial",16));
        return btn;
    }

    public ScrollPane getRoot() {
        return scrollPane;
    }
}
