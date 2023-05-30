package com.example.demoapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

import static com.example.demoapp.MainController.workTime;


public class PlannerController implements Initializable {
    static ObservableList<Plan> observableList1 = FXCollections.observableArrayList(new Plan("work", "programming",1));
    static ObservableList<Plan> observableList2 = FXCollections.observableArrayList();
    public TableView<Plan> tasks;
    public TableColumn<Plan, String> type;
    public TableColumn<Plan, String> desc;
    public Button add;
    public TextField TYPE;
    public TextField DESC;
    public TableView<Plan> doneTasks;
    public TableColumn<Plan, String> doneType;
    public TableColumn<Plan, String> doneDesc;
    public TableColumn <Plan, Integer> timeInMin;
    public TableColumn <Plan, Integer> timeInPom;
    public Button back;
    public Text warning3;
    public Button removeButton;
    public Text planTime;
    public Text doneTime;
    public Text uncompleted;
    public Text completed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.setCellValueFactory(new PropertyValueFactory<>("taskType"));
        desc.setCellValueFactory(new PropertyValueFactory<>("taskDesc"));
        timeInPom.setCellValueFactory(new PropertyValueFactory<>("taskTime"));
        doneType.setCellValueFactory(new PropertyValueFactory<>("taskType"));
        doneDesc.setCellValueFactory(new PropertyValueFactory<>("taskDesc"));
        timeInMin.setCellValueFactory(new PropertyValueFactory<>("taskTime"));
        tasks.setItems(observableList1);
        doneTasks.setItems(observableList2);
        tasks.setEditable(true);
        desc.setCellFactory(TextFieldTableCell.forTableColumn());
        uncompleted.setText(String.valueOf(observableList1.size()));
        planTime.setText(calculateTime(observableList1));
        completed.setText(String.valueOf(observableList2.size()));
        doneTime.setText(calculateTime(observableList2));
    }

    private String calculateTime(ObservableList<Plan> list) {
        int minutes =0;
        for (Plan plan: list){
            minutes+=(plan.getTaskTime()*workTime);
        }
        int hours = minutes/60;
        minutes=minutes%60;
        return String.format("%02d:%02d", hours, minutes);
    }
    @FXML
    public void removeTask(ActionEvent actionEvent) {
        if (!observableList1.isEmpty()){
            observableList1.remove(observableList1.size()-1);
            uncompleted.setText(String.valueOf(observableList1.size()));
            planTime.setText(calculateTime(observableList1));
        }
    }

    @FXML
    public void AddButton() {
        Plan plan = new Plan(TYPE.getText(), DESC.getText(),1);
        if (DESC.getText().length()>30){
            warning3.setText("Please, write description less than in 30 characters");
            DESC.clear();
        } else {
            warning3.setText("");
            if (!DESC.getText().equals("")) {
                tasks.getItems().add(plan);
            } else tasks.getItems().add(new Plan("", "(no description)", 1));
            uncompleted.setText(String.valueOf(observableList1.size()));
            planTime.setText(calculateTime(observableList1));
            TYPE.clear();
            DESC.clear();
        }
    }

    public void onEditStart(TableColumn.CellEditEvent<Plan, String> planStringCellEditEvent) {
        Plan plan = tasks.getSelectionModel().getSelectedItem();
        plan.setTaskDesc(planStringCellEditEvent.getNewValue());
    }

    
    @FXML
    public void switchToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainScene.fxml"));
        Parent root = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pomodoro Tracker");
        stage.setScene(new Scene(root, 700, 600));
        stage.show();

    }

}
