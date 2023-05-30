package com.app.gui;

import com.app.mail.UserData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

public class MessageReceiver extends Stage {
    private VBox mainPane;

    private final MimeMessage message;

    public MessageReceiver(MimeMessage message) {
        this.message = message;
        try {
            createMainPane();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(mainPane);
        setScene(scene);

        int partOfTheScreen = 3;
        setWidth(Screen.getScreens().get(0).getBounds().getMaxX() / partOfTheScreen);
        setHeight(Screen.getScreens().get(0).getBounds().getMaxY() / partOfTheScreen);
        setTitle("ReceivedMessage");
        setResizable(false);
        show();
    }

    private void createMainPane() throws Exception {
        mainPane = new VBox();
        int gapAndPadding = 10;
        mainPane.setPadding(new Insets(gapAndPadding, gapAndPadding, gapAndPadding, gapAndPadding));
        mainPane.setAlignment(Pos.CENTER);
        //
        Label user = new Label(((InternetAddress) message.getFrom()[0]).getAddress());
        user.setAlignment(Pos.TOP_LEFT);
        mainPane.getChildren().add(user);
        //
        Label date = new Label(message.getSentDate().toString());
        date.setAlignment(Pos.TOP_LEFT);
        mainPane.getChildren().add(date);
        //
        Label subject = new Label(message.getSubject());
        subject.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(subject);
        //
        TextArea text = new TextArea();
        mainPane.getChildren().add(text);
        //
        ComboBox<String> download = new ComboBox<>();
        download.setValue("Files");
        mainPane.getChildren().add(download);

        // Checking whether multipart
        Multipart mp;
        MimeBodyPart[] allParts;
        try {
            mp = (Multipart) message.getContent();
            int count = mp.getCount();
            if (count == 0) return;
            allParts = new MimeBodyPart[count];

            StringBuilder textItSelf = new StringBuilder();
            // Iterating all parts
            for (int i = 0; i < count; i++) {
                MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(i);
                if (part.getContentType().toLowerCase().contains("text/plain")) {
                    textItSelf.append(part.getContent()).append("\n");
                } else {
                    download.getItems().add(UserData.decode(part.getFileName()));
                }
                allParts[i] = part;
            }
            text.setText(UserData.decode(textItSelf.toString()));

            download.setOnAction(event -> {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(this);
                for (MimeBodyPart part : allParts) {
                    try {
                        if (UserData.decode(part.getFileName()).equals(UserData.decode(download.getValue()))) {
                            part.saveFile(selectedDirectory + File.separator + UserData.decode(download.getValue()));
                            break;
                        }
                    } catch (MessagingException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
