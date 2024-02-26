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
    public void init() {
        UserData.setConfigFiles();
        addReceiver();
    }

    @Override
    public void start(Stage mainStage) {
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

    @Override
    public void stop() {
        for (EmailReceiver i : receiver) i.closeConnection();
        sender.closeConnection();
    }

    private void addReceiver() {
        Mail[] users = UserData.getAllUsers();
        receiver = new ArrayList<>();
        for (Mail user : users) {
            if (user != null) receiver.add(new EmailReceiver(user));
        }
    }

    public static void main(String[] args) {
        launch(LaunchApplication.class);
    }
}