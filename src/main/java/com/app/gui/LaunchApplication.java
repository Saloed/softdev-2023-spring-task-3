package com.app.gui;

import com.app.mail.EmailReceiver;
import com.app.mail.EmailSender;
import com.app.mail.UserData;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class LaunchApplication extends Application {

    private List<EmailReceiver> receiver;

    private EmailSender sender;

    @Override
    public void start(Stage mainStage) {
        addReceiver();

        sender = new EmailSender();
        MainPane mainPane = new MainPane(receiver, sender);
        MainScene mainScene = new MainScene(mainPane);

        mainStage.setScene(mainScene);
        mainStage.setTitle("MailClient");
        mainStage.setWidth(Screen.getScreens().get(0).getBounds().getMaxX() / 1.5);
        mainStage.setHeight(Screen.getScreens().get(0).getBounds().getMaxY() / 1.5);
        mainStage.centerOnScreen();
        mainStage.setResizable(false);
        mainStage.show();
    }

    private void addReceiver() {
        Mail[] users = UserData.getAllUsers();
        receiver = new ArrayList<>();
        for (int i = 0; i < users.length; i++) {
            receiver.add(new EmailReceiver());
            receiver.get(i).startNotifications(users[i]);
        }
    }

    @Override
    public void stop() {
        for (EmailReceiver i : receiver) i.closeConnection();
        sender.closeConnection();
    }

    public static void main(String[] args) {
        launch(LaunchApplication.class);
    }
}