package com.app.gui;

import com.app.mail.EmailReceiver;
import com.app.mail.UserData;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.mail.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MailAdder extends Stage {
    private GridPane mainPane;

    private final MainPane parent;

    MailAdder(MainPane parent, List<EmailReceiver> receiver) {
        super();
        this.parent = parent;

        createMainPane(receiver);

        Scene mainScene = new Scene(mainPane);
        this.setScene(mainScene);

        this.setWidth(Screen.getScreens().get(0).getBounds().getMaxX() / 3);
        this.setHeight(Screen.getScreens().get(0).getBounds().getMaxY() / 2);
        this.setResizable(false);
        this.setTitle("Configs");
        this.show();
    }

    private void createMainPane(List<EmailReceiver> receiver) {
        mainPane = new GridPane();
        GridPane.setMargin(mainPane, new Insets(30));

        List<Labeled> allGridKeyElements = new ArrayList<>();
        List<TextField> allGridValueElements = new ArrayList<>();

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(40);
        mainPane.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(60);
        mainPane.getColumnConstraints().add(column2);

        for (int i = 0; i < 10; i++) {
            RowConstraints row1 = new RowConstraints();
            row1.setPercentHeight(100 / 10);
            mainPane.getRowConstraints().add(row1);
        }

        Label username = new Label("username");
        mainPane.add(username, 0, 0);
        GridPane.setHalignment(username, HPos.CENTER);
        GridPane.setValignment(username, VPos.CENTER);
        allGridKeyElements.add(username);

        Label password = new Label("password");
        mainPane.add(password, 0, 1);
        GridPane.setHalignment(password, HPos.CENTER);
        GridPane.setValignment(password, VPos.CENTER);
        allGridKeyElements.add(password);

        TextField textField = new TextField("Default text");
        mainPane.add(textField, 1, 0);
        GridPane.setHalignment(textField, HPos.CENTER);
        GridPane.setValignment(textField, VPos.CENTER);
        allGridValueElements.add(textField);

        TextField passwordField = new PasswordField();
        mainPane.add(passwordField, 1, 1);
        GridPane.setHalignment(passwordField, HPos.LEFT);
        GridPane.setValignment(passwordField, VPos.CENTER);
        allGridValueElements.add(passwordField);

        String currentPath;
        Properties basicProps = new Properties();
        try {
            currentPath = Paths.get("").toRealPath().toString();
            basicProps.load(new FileInputStream(currentPath + "/src/main/resources/props/basicConfigs.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int x = 2;
        for (Map.Entry<Object, Object> pair : basicProps.entrySet()) {
            Label textF = new Label((String) pair.getKey());
            mainPane.add(textF, 0, x);
            GridPane.setHalignment(textF, HPos.CENTER);
            GridPane.setValignment(textF, VPos.CENTER);
            allGridKeyElements.add(textF);

            TextField passwordF = new TextField((String) pair.getValue());
            mainPane.add(passwordF, 1, x);
            GridPane.setHalignment(passwordF, HPos.LEFT);
            GridPane.setValignment(passwordF, VPos.CENTER);
            allGridValueElements.add(passwordF);
            x++;
        }

        Button save = new Button("Save");
        mainPane.add(save, 1, x);
        save.setOnAction((ActionEvent event) -> {
            Properties props = new Properties();
            try {
                // Put all props in StringBuilder
                StringBuilder propsToWrite = new StringBuilder();
                for (int i = 0; i < 9; i++) {
                    String tx = allGridKeyElements.get(i).getText();
                    String ps = allGridValueElements.get(i).getText();
                    propsToWrite.append(tx).append("=").append(ps).append("\n");
                    props.setProperty(tx, ps);
                }
                // Check if possible to connect
                Session session = Session.getDefaultInstance(props);
                Store store = session.getStore("imaps");
                store.connect(props.getProperty("mail.imap.host"), props.getProperty("username"),
                        props.getProperty("password"));
                store.close();
                // Creation of user
                UserData.saveUser(new Mail(props));
                receiver.add(new EmailReceiver(new Mail(props)));
                // Adding to users.txt
                List<String> cached = UserData.getCachedUsers();
                cached.add(props.getProperty("username"));
                UserData.setCachedUsers(cached);
                //
                parent.refreshChoiceBox();
            } catch (Exception e) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Button ok = new Button("Ok"); ok.setOnAction((ActionEvent ev) -> this.close());
                VBox vbox = new VBox(new Text("Couldn't add email"), ok);
                vbox.setAlignment(Pos.CENTER);
                vbox.setPadding(new Insets(15));

                dialogStage.setScene(new Scene(vbox));
                dialogStage.setResizable(false);
                dialogStage.show();
            }
            this.close();
        });
    }

}
