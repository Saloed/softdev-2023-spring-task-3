package com.app.gui;

import com.app.mail.EmailSender;
import com.app.mail.UserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.stream.Collectors;

public class MessageCreator extends Stage {

    private GridPane mainPane;

    private final EmailSender sender;

    private final Session session;

    public MessageCreator(EmailSender sender, Session session) {
        this.sender = sender;
        this.session = session;
        createMainPane();

        Scene scene = new Scene(mainPane);

        setScene(scene);
        setWidth(Screen.getScreens().get(0).getBounds().getMaxX() / 3);
        setHeight(Screen.getScreens().get(0).getBounds().getMaxY() / 2);
        setTitle("CreationOfMessage");
        setResizable(false);
        show();
    }

    private void createMainPane() {
        mainPane = new GridPane();
        GridPane.setMargin(mainPane, new Insets(30));
        // Setting columns and rows
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setPercentWidth(100/3);
            mainPane.getColumnConstraints().add(column1);
        }
        for (int i = 0; i < 12; i++) {
            RowConstraints row1 = new RowConstraints();
            row1.setPercentHeight(100/12);
            mainPane.getRowConstraints().add(row1);
        }
        //
        Label to = new Label("To");
        to.setTooltip(new Tooltip("Add users in Configs -> All users"));
        mainPane.add(to, 0, 0);
        GridPane.setHalignment(to, HPos.CENTER);
        GridPane.setValignment(to, VPos.CENTER);

        ObservableList<String> listOfUsers = FXCollections.observableArrayList(UserData.getCachedUsers());

        CheckComboBox<String> checkTo = new CheckComboBox<>(listOfUsers);
        checkTo.setTitle("Add users in Configs -> All users");
        mainPane.add(checkTo, 1, 0, 2, 1);
        GridPane.setHalignment(checkTo, HPos.CENTER);
        GridPane.setValignment(checkTo, VPos.CENTER);


        Label cc = new Label("CC");
        mainPane.add(cc, 0, 1);
        GridPane.setHalignment(cc, HPos.CENTER);
        GridPane.setValignment(cc, VPos.CENTER);

        CheckComboBox<String> checkCc = new CheckComboBox<>(listOfUsers);
        checkCc.setTitle("Add users in Configs -> All users");
        mainPane.add(checkCc, 1, 1, 2, 1);
        GridPane.setHalignment(checkCc, HPos.CENTER);
        GridPane.setValignment(checkCc, VPos.CENTER);

        Label bcc = new Label("BCC");
        mainPane.add(bcc, 0, 2);
        GridPane.setHalignment(bcc, HPos.CENTER);
        GridPane.setValignment(bcc, VPos.CENTER);

        CheckComboBox<String> checkBcc = new CheckComboBox<>(listOfUsers);
        checkBcc.setTitle("Add users in Configs -> All users");
        mainPane.add(checkBcc, 1, 2, 2, 1);
        GridPane.setHalignment(checkBcc, HPos.CENTER);
        GridPane.setValignment(checkBcc, VPos.CENTER);

        Label topic = new Label("Topic");
        mainPane.add(topic, 0, 3);
        GridPane.setHalignment(topic, HPos.CENTER);
        GridPane.setValignment(topic, VPos.CENTER);

        TextField topicField = new TextField();
        mainPane.add(topicField, 1, 3, 2, 1);
        GridPane.setHalignment(topicField, HPos.CENTER);
        GridPane.setValignment(topicField, VPos.CENTER);

        TextArea text = new TextArea();
        mainPane.add(text, 0,4, 3, 6);

        ComboBox<String>filesAdded = new ComboBox<>();
        filesAdded.setValue("Files");
        filesAdded.setOnMouseClicked((MouseEvent ev) -> {
            filesAdded.getItems().remove(filesAdded.getValue());
            filesAdded.getSelectionModel().clearSelection();
            filesAdded.setValue("Files");
        });
        mainPane.add(filesAdded, 0,10, 2, 1);
        GridPane.setHalignment(topicField, HPos.CENTER);
        GridPane.setValignment(topicField, VPos.CENTER);

        Button addFiles = new Button("Add files");
        mainPane.add(addFiles, 2,10);
        GridPane.setHalignment(topicField, HPos.RIGHT);
        GridPane.setValignment(topicField, VPos.CENTER);
        addFiles.setOnAction((ActionEvent ev)-> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File selectedFile = fileChooser.showOpenDialog(this);
            if (selectedFile != null) {
                filesAdded.getItems().add(selectedFile.getAbsolutePath());
            }
        });

        Button send = new Button("Send");
        mainPane.add(send, 2,11);
        GridPane.setHalignment(topicField, HPos.RIGHT);
        GridPane.setValignment(topicField, VPos.CENTER);
        send.setOnAction((ActionEvent e) -> {
            sender.setBasic(session, topicField.getText());
            sender.setRecipients(checkTo.getCheckModel().getCheckedItems().stream()
                            .map(obj -> {
                                try {
                                    return new InternetAddress(obj);
                                } catch (AddressException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }).collect(Collectors.toList()),
                    checkCc.getCheckModel().getCheckedItems().stream()
                            .map(obj -> {
                                try {
                                    return new InternetAddress(obj);
                                } catch (AddressException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }).collect(Collectors.toList()),
                    checkBcc.getCheckModel().getCheckedItems().stream()
                            .map(obj -> {
                                try {
                                    return new InternetAddress(obj);
                                } catch (AddressException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }).collect(Collectors.toList()));
            sender.addFilesAndText(filesAdded.getItems(), text.getText());
            this.close();
            sender.sendEmail(session);
        });
    }
}
