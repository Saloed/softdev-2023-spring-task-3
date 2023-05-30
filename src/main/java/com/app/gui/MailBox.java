package com.app.gui;

import com.app.mail.EmailReceiver;
import com.app.mail.EmailSender;
import com.sun.mail.imap.IMAPFolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailBox extends VBox {
    private TreeView<String> allMails;
    private Mail mainMail;

    private final EmailSender sender;

    private final EmailReceiver receiver;

    private final MainPane parent;

    private final int messageOnOnePage = 20;

    private int nowPage = 0;

    private Button previous;

    private Button next;

    private TableView<TableMessage> table;

    private ObservableList<TableMessage> messages;

    private void addTreeView(String username) {
        mainMail = new Mail(username);
        // Ugly way of constructing treeview
        TreeItem<String> mainMailString = new TreeItem<>(mainMail.getUserName());
        for (TreeItem<String> i : mainMail.returnStringDirs(receiver)) mainMailString.getChildren().add(i);
        allMails = new TreeView<>(mainMailString);
        this.getChildren().add(allMails);
    }

    MailBox(EmailReceiver receiver, EmailSender sender, String username, MainPane parent) {
        this.receiver = receiver;
        this.sender = sender;
        this.parent = parent;

        addTreeView(username);
        addCreateMailButton();
        addNextAndPreviousButtons();
        addFolderWatcher();

        this.setAlignment(Pos.CENTER);
    }

    private void addNextAndPreviousButtons() {
        next = new Button("Next");
        previous = new Button("Previous");
        this.getChildren().add(next);
        this.getChildren().add(previous);
    }

    private void addCreateMailButton() {
        Button sendMail = new Button("Create Mail");
        sendMail.setOnAction(event -> new MessageCreator(sender, receiver.getNowSession()));
        this.getChildren().add(sendMail);
    }

    private void addTableWithMessages(Message[] allMessages) {
        messages = FXCollections.observableArrayList();
        for (Message message : allMessages) {
            try {
                TableMessage tableMessage = new TableMessage(
                        ((InternetAddress) (message.getFrom()[0])).getAddress(),
                        ((InternetAddress) (message.getAllRecipients()[0])).getAddress(),
                        message.getSubject(),
                        message.getSentDate().toString(),
                        (MimeMessage) message
                );
                messages.add(tableMessage);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        table = new TableView<>(messages);
        table.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            MimeMessage msg = (MimeMessage)table.getSelectionModel().getSelectedItem().getMessage();
            if (event.getButton() == MouseButton.PRIMARY) {
                new MessageReceiver(msg);
            }
            else {
                Platform.runLater(() -> {
                    new PopupNotification(msg, mainMail.getSession(),
                            "Delete", receiver);
                });
            }
            refreshTable();
        });
        TableColumn<TableMessage, String> fromColumn = new TableColumn<>("From");
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        table.getColumns().add(fromColumn);

        TableColumn<TableMessage, String> toColumn = new TableColumn<>("To");
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        table.getColumns().add(toColumn);

        TableColumn<TableMessage, String> subjectColumn = new TableColumn<>("Subject");
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        table.getColumns().add(subjectColumn);

        TableColumn<TableMessage, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.getColumns().add(dateColumn);

        parent.setCenter(table);
    }

    public void refreshTable() {
        addTableWithMessages(receiver.downloadMessagesFromFolder(
                (IMAPFolder) ((MimeMessage)table.getSelectionModel().getSelectedItem().getMessage()).getFolder(),
                nowPage * messageOnOnePage, messageOnOnePage * (nowPage + 1)));
        table.refresh();
    }

    private void addFolderWatcher() {
        MultipleSelectionModel<TreeItem<String>> msm = allMails.getSelectionModel();
        final IMAPFolder[] lastFolder = {null};
        msm.selectedItemProperty().addListener((changed, oldValue, newValue) -> {
            if (!newValue.getValue().equals(mainMail.getUserName())) {
                // If another folder is seleceted => nowPage = 0
                IMAPFolder folder = receiver.getInnerFolder(newValue);
                if (lastFolder[0] == null || !folder.equals(lastFolder[0])) nowPage = 0;
                lastFolder[0] = folder;

                // Thread to receive message
                receiver.getExecutorService().execute(() -> {
                    if (!folder.isOpen()) {
                        receiver.openFolderConnection(folder);
                        int amount;
                        try {
                            amount = folder.getMessageCount();
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                        Platform.runLater(() -> addTableWithMessages(receiver.downloadMessagesFromFolder(folder,
                                nowPage * messageOnOnePage, messageOnOnePage * (nowPage + 1))));

                        // Open next page
                        next.setOnAction(event -> {
                            if ((nowPage + 1) * messageOnOnePage < amount) nowPage++;
                            Platform.runLater(() -> {
                                this.getChildren().remove(table);
                                addTableWithMessages(receiver.downloadMessagesFromFolder(folder,
                                        nowPage * messageOnOnePage,
                                        Math.min(amount, messageOnOnePage * (nowPage + 1))));
                            });
                        });

                        // Previous one
                        previous.setOnAction(event -> {
                            if ((nowPage - 1) * messageOnOnePage >= 0) nowPage--;
                            Platform.runLater(() -> {
                                this.getChildren().remove(table);
                                addTableWithMessages(receiver.downloadMessagesFromFolder(folder,
                                        nowPage * messageOnOnePage,
                                        Math.min(amount, messageOnOnePage * (nowPage + 1))));
                            });
                        });
                    }
                });
            }
        });
    }
}
