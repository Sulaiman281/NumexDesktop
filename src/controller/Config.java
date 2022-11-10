package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import main.Singleton;
import model.Trade;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

public class Config {

    @FXML
    private TextField tradeName;

    @FXML
    private Label monthsYear_label;

    @FXML
    private GridPane gridDates;

    @FXML
    private TextField bDays;

    @FXML
    private TextField aDays;

    final int width = 60, height = 50;
    int currentMonth;
    int currentYear;

    String dates = "";

    @FXML
    void initialize(){

        Singleton s = Singleton.getInstance();

        s.numField(bDays);
        s.numField(aDays);

        // add heading into the grid of days of weeks.
        gridDates.setHgap(10);

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        currentYear = Integer.parseInt(date.split("-")[0]);
        currentMonth = Integer.parseInt(date.split("-")[1]);
        updateCalendar();
    }

    private void updateCalendar() {
        gridDates.getChildren().clear();
        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        currentMonth = yearMonth.getMonthValue();
        int daysOfMonth = yearMonth.lengthOfMonth();
        monthsYear_label.setText(getMonth(yearMonth.getMonthValue())+" "+yearMonth.getYear());

        gridDates.add(getHBtn("Mon"),0,0);
        gridDates.add(getHBtn("Tues"),1,0);
        gridDates.add(getHBtn("Wed"),2,0);
        gridDates.add(getHBtn("Thur"),3,0);
        gridDates.add(getHBtn("Fri"),4,0);
        gridDates.add(getHBtn("Sat"),5,0);
        gridDates.add(getHBtn("Sun"),6,0);

        int row = 1;
        for(int d = 0; d< daysOfMonth; d++){
            LocalDate localDate = LocalDate.of(currentYear, currentMonth, d+1);
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            AtomicBoolean isSelected = new AtomicBoolean(false);
            Button b = getDateBtn((d+1)+"");
            if(dates.contains(localDate.toString())) {
                selectedStyleBtn(b);
                isSelected.set(true);
            }else{
                normalStyleBtn(b);
                isSelected.set(false);
            }
            b.setOnAction(e->{
                if(dates.split(",").length == 20) return;
                if(!isSelected.get()){
                    isSelected.set(true);
                    selectedStyleBtn(b);
                    dates = dates.concat(localDate+",");
                }else{
                    isSelected.set(false);
                    normalStyleBtn(b);
                    dates = dates.replace(localDate+",","");
                }
            });
            gridDates.add(box(b),dayOfWeek.getValue()-1,row);
            if(dayOfWeek.getValue() == 7) row++;
        }
    }

    @FXML
    void nextMonth(ActionEvent event) {
        if(currentMonth == 12){
            currentYear++;
            currentMonth = 1;
        }else{
            currentMonth++;
        }
        updateCalendar();
    }

    @FXML
    void previousMonth(ActionEvent event) {
        if(currentMonth == 1){
            currentYear--;
            currentMonth = 12;
        }else{
            currentMonth--;
        }
        updateCalendar();
    }

    @FXML
    void add_trade(ActionEvent event) {
        if(tradeName.getText().isEmpty() || bDays.getText().isEmpty() || aDays.getText().isEmpty() || dates.isEmpty()){
            // one of the field is empty
            return;
        }
        Trade trade = new Trade();
        trade.setName(tradeName.getText());
        trade.setBeforeAfterDays(bDays.getText()+","+aDays.getText());
        trade.setDates(dates);

        Singleton singleton = Singleton.getInstance();
        try {
            Connection con = singleton.db.connector();
            con.prepareStatement(trade.getQuery()).execute();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    Label getHBtn(String str){
        Label label = new Label(str);
        label.setFont(Font.font("Arial",16));
        label.setPrefWidth(100);
        label.setPrefHeight(60);
        label.setAlignment(Pos.CENTER);
        label.setStyle(
                "-fx-background-color: #10a0cc;\n"+
                        "-fx-text-fill: white;\n"+
                        "-fx-background-radius: 40;"
        );
        return label;
    }
    Button getDateBtn(String txt){
        Button btn = new Button(txt);
        btn.setFont(Font.font("Arial",16));
        return btn;
    }
    void normalStyleBtn(Button btn){
        btn.setStyle(
                "-fx-background-color: white;\n"+
                        "-fx-text-fill: black;\n"+
                        "-fx-background-radius: 200;"
        );
    }
    void selectedStyleBtn(Button btn){
        btn.setStyle(
                "-fx-background-color: #10e376;\n"+
                        "-fx-text-fill: white;\n"+
                        "-fx-background-radius: 200;"
        );
    }
    HBox box(Node node){
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPrefWidth(width);
        box.setPrefHeight(height);
        box.getChildren().addAll(node);
        return box;
    }

    String getDay(int x){
        switch (x){
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tues";
            case 4:
                return "Wed";
            case 5:
                return "Thurs";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
        }
        return null;
    }
    String getMonth(int x){
        switch (x){
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return null;
    }

}
