package com.app.gui;

import com.app.mail.EmailReceiver;
import com.app.mail.EmailSender;
import com.app.mail.UserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainPane extends BorderPane {

    private ChoiceBox<String> mails;

    private ObservableList<String> allMails;

    MainPane(List<EmailReceiver> receiver, EmailSender sender) {
        createCheckBoxMail(receiver, sender);
        setMenuBar(receiver);
    }

    private void createCheckBoxMail(List<EmailReceiver> receiver, EmailSender sender) {
        VBox vbox = new VBox();
        // Creation of checkBox
        allMails = FXCollections.observableArrayList(
                Arrays.stream(UserData.getAllUsers()).map(Mail::toString).collect(Collectors.toList())
        );
        mails = new ChoiceBox<>(allMails);
        mails.setValue("Mails");
        mails.setOnAction(event -> {
            // Remove previous mailBox
            for (Object i : vbox.getChildren()) {
                if (i instanceof MailBox) {
                    vbox.getChildren().remove(i);
                    break;
                }
            }
            // Creation a new one
            for (EmailReceiver i : receiver) {
                if (i.getNowSession().getProperty("username").equals(mails.getValue())) {
                    vbox.getChildren().add(new MailBox(i, sender, mails.getValue(), this));
                    break;
                }
            }
        });
        vbox.getChildren().add(mails);
        vbox.setAlignment(Pos.TOP_CENTER);
        this.setLeft(vbox);
    }

    private void setMenuBar(List<EmailReceiver> receiver) {
        MenuBar menuBar = new MenuBar();

        Menu settings = new Menu("Settings");

        MenuItem addUser = new MenuItem("Add user");
        addUser.setOnAction((ActionEvent event) -> new MailAdder(this, receiver));
        settings.getItems().add(addUser);
        MenuItem deleteUser = new MenuItem("Delete user");
        deleteUser.setOnAction((ActionEvent event) -> {
            // Not necessary right now
        });
        settings.getItems().add(deleteUser);

        Menu configs = new Menu("Configs");
        MenuItem listOfUser = new MenuItem("All users");
        listOfUser.setOnAction((ActionEvent event) -> new CachedUsers());
        configs.getItems().add(listOfUser);

        menuBar.getMenus().add(settings);
        menuBar.getMenus().add(configs);

        this.setTop(menuBar);
    }

    public void refreshChoiceBox() {
        allMails.clear();
        allMails = FXCollections.observableArrayList(
                Arrays.stream(UserData.getAllUsers()).map(Mail::toString).collect(Collectors.toList())
        );
        mails.setItems(allMails);
    }


}
