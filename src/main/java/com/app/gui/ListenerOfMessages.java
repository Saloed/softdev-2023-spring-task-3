package com.app.gui;

import com.app.mail.EmailReceiver;
import com.sun.mail.imap.IMAPFolder;
import javafx.application.Platform;

import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.MimeMessage;

public class ListenerOfMessages implements MessageCountListener {

    private final EmailReceiver receiver;
    private final IMAPFolder folder;

    public ListenerOfMessages(EmailReceiver receiver, IMAPFolder folder) {
        this.folder = folder;
        this.receiver = receiver;
    }

    @Override
    public void messagesAdded(MessageCountEvent messageCountEvent) {
        MimeMessage msg = (MimeMessage) receiver.getLatestMessage(folder);
        Platform.runLater(() -> new PopupNotification(msg, receiver.getNowSession(),
                "New message", receiver));
    }

    @Override
    public void messagesRemoved(MessageCountEvent messageCountEvent) {}
}
