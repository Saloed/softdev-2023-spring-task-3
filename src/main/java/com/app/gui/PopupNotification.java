package com.app.gui;

import com.app.mail.EmailReceiver;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PopupNotification {

    private GridPane mainPane;

    private final Stage stage;

    private final int partOfTheScreen = 5;

    private final String type;

    private final EmailReceiver receiver;

    public PopupNotification(Message msg, Session nowSession, String type, EmailReceiver receiver) {
        this.stage = new Stage();
        this.type = type;
        this.receiver = receiver;

        setPane(msg, nowSession);
        int gapAndPadding = 10;
        mainPane.setPadding(new Insets(gapAndPadding, gapAndPadding, gapAndPadding, gapAndPadding));
        mainPane.setHgap(gapAndPadding);
        mainPane.setVgap(gapAndPadding);
        mainPane.setAlignment(Pos.CENTER);

        Scene mainScene = new Scene(mainPane);

        stage.setScene(mainScene);
        stage.setTitle(this.type);
        stage.setWidth(Screen.getScreens().get(0).getBounds().getMaxX() / partOfTheScreen);
        stage.setHeight(Screen.getScreens().get(0).getBounds().getMaxY() / partOfTheScreen);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();

        setAutoClose();
    }

    private void setAutoClose() {
        int secondsToWait = 10;
        PauseTransition delay = new PauseTransition(Duration.seconds(secondsToWait));
        delay.setOnFinished(event -> stage.close());
        delay.play();
    }

    private void setPane(Message msg, Session nowSession) {
        mainPane = new GridPane();

        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        mainPane.getColumnConstraints().add(column1);
        mainPane.getColumnConstraints().add(column2);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        mainPane.getRowConstraints().add(row1);
        mainPane.getRowConstraints().add(row2);
        //
        try {
            Text inf = new Text("Subject: " + msg.getSubject() + "\n"
                    + "From: " + ((InternetAddress) msg.getFrom()[0]).getAddress() + "\n"
                    + "To: " + nowSession.getProperty("username")
            );
            mainPane.add(inf, 0, 0, 2, 1);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        //
        if (type.equals("New message")) {
            Button read = new Button("Read");
            read.setOnAction(event -> {
                new MessageReceiver((MimeMessage) msg);
                stage.close();
            });
            mainPane.add(read, 0, 1);
            stage.setX(Screen.getScreens().get(0).getBounds().getMaxX() / partOfTheScreen * (partOfTheScreen - 1));
            stage.setY(Screen.getScreens().get(0).getBounds().getMaxY() / partOfTheScreen * (partOfTheScreen - 1));
        } else {
            Button restore = new Button("Delete");
            restore.setOnAction(event -> {
                receiver.deleteMessageFromFolder(msg);
                stage.close();
            });
            mainPane.add(restore, 0, 1);
        }
        //
        Button close = new Button("Close");
        close.setOnAction(event -> stage.close());
        mainPane.add(close, 1, 1);
    }
}