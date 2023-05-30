package com.app.gui;

import com.app.mail.UserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CachedUsers extends Stage {
    private GridPane mainPane;
    
    CachedUsers() {
        setMainPane();
        Scene scene = new Scene(mainPane);

        setScene(scene);
        setWidth(Screen.getScreens().get(0).getBounds().getMaxX() / 6);
        setHeight(Screen.getScreens().get(0).getBounds().getMaxY() / 4);
        setTitle("ListOfCachedUsers");
        setResizable(false);
        show();
    }

    private void setMainPane() {
        mainPane = new GridPane();
        ObservableList<String> cached = FXCollections.observableArrayList(UserData.getCachedUsers());
        ListView<String> listOfUsers = new ListView<>(cached);
        MultipleSelectionModel<String> usersSelectionModel = listOfUsers.getSelectionModel();
        listOfUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        mainPane.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(25);
        mainPane.getColumnConstraints().add(column2);

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(25);
        mainPane.getColumnConstraints().add(column3);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(80);
        mainPane.getRowConstraints().add(row1);

        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);
        mainPane.getRowConstraints().add(row2);

        mainPane.add(listOfUsers, 0, 0, 3, 1);

        TextField addField = new TextField("Enter new mail");
        mainPane.add(addField, 0, 1);

        Button addButton = new Button("Add");
        mainPane.add(addButton, 1, 1);
        addButton.setOnAction((ActionEvent ev) -> {
            String text = addField.getText();
            if (!text.isEmpty()) cached.add(text);
            addField.clear();
            UserData.setCachedUsers(listOfUsers.getItems());
        });

        Button deleteButton = new Button("Delete");
        mainPane.add(deleteButton, 2, 1);

        deleteButton.setOnAction((ActionEvent ev) -> {
            for (String item : usersSelectionModel.getSelectedItems()) {
                cached.remove(item);
            }
            UserData.setCachedUsers(listOfUsers.getItems());
        });
    }
}
