package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import main.Singleton;
import view.CalenderTab;
import view.PositionTab;

public class MainContainer {

    @FXML
    private Tab dashboard;

    @FXML
    private Tab position;

    @FXML
    private Tab calender;

    @FXML
    private Tab alarms;

    @FXML
    private Tab messages;

    @FXML
    private Tab futures_spreads;

    @FXML
    private Tab options_spreads;

    @FXML
    private Tab config;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private Label label5;

    @FXML
    private Label label4;

    @FXML
    void initialize(){
        config.setOnSelectionChanged(e->{
            if(config.isSelected())
                config.setContent(
                        Singleton.getInstance().getView("Config").getRoot()
                );
        });

        calender.setOnSelectionChanged(e->{
            if(calender.isSelected()){
                calender.setContent(new CalenderTab().getRoot());
            }
        });

        position.setOnSelectionChanged(e->{
            if(position.isSelected()){
                position.setContent(new PositionTab().getRoot());
            }
        });
    }

}
