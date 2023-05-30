package com.app.mail;


import com.app.gui.Mail;
import com.app.gui.PopupNotification;
import com.sun.mail.imap.IMAPFolder;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EmailReceiver {

    private Store store;

    private Session nowSession;

    private ExecutorService executorService;

    public EmailReceiver() {
    }

    private EmailReceiver getItSelf() {
        return this;
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Session getNowSession() {
        return nowSession;
    }

    public EmailReceiver setNowSession(Session nowSession) {
        this.nowSession = nowSession;
        return this;
    }


    public void setNotifications() {
        this.executorService = Executors.newFixedThreadPool(getMainFolders().size() + 1);
        for (IMAPFolder folder : this.getMainFolders()) {
            setNotificationOnFolder(folder);
        }
    }

    public void setNotificationOnFolder(IMAPFolder folder) {
        openFolderConnection(folder);
        executorService.execute(() -> {
            folder.addMessageCountListener(new MessageCountListener() {
                @Override
                public void messagesAdded(MessageCountEvent messageCountEvent) {
                    MimeMessage msg = (MimeMessage) getLatestMessage(folder);
                    Platform.runLater(() -> new PopupNotification(msg, nowSession, "New message", getItSelf()));
                }

                @Override
                public void messagesRemoved(MessageCountEvent messageCountEvent) {}
            });
            try {
                while (true) {
                    folder.idle(true);
                }
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public EmailReceiver startNotifications(Mail mail) {
        setNowSession(mail.getSession());
        openStoreConnection();
        setNotifications();
        return this;
    }

    public void deleteMessageFromFolder(Message message) {
        try {
            message.setFlag(Flags.Flag.DELETED, true);
            message.getFolder().expunge();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        if (store != null) for (IMAPFolder folder : getMainFolders()) closeFolderConnection(folder);
        closeStoreConnection();
        if (executorService != null && !executorService.isShutdown()) executorService.shutdownNow();
    }


    public void openStoreConnection() {
        try {
            store = nowSession.getStore("imaps");
            store.connect(nowSession.getProperty("mail.imap.host"),
                    nowSession.getProperty("username"), nowSession.getProperty("password"));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeStoreConnection() {
        if (store != null && store.isConnected()) {
            try {
                store.close();
            } catch (MessagingException e) {
                System.out.println("Can't close store connection");
                throw new RuntimeException(e);
            }
        }
    }

    public void openFolderConnection(IMAPFolder folder) {
        try {
            if (!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }
        } catch (MessagingException e) {
            System.out.println("Can't open folder connection");
            throw new RuntimeException(e);
        }
    }

    public void closeFolderConnection(IMAPFolder folder) {
        if (folder != null && folder.isOpen()) {
            try {
                folder.close();
            } catch (MessagingException e) {
                System.out.println("Can't close folder connection");
                throw new RuntimeException(e);
            }
        }
    }

    public Message[] downloadMessagesFromFolder(IMAPFolder folderName, int from, int to) {
        try {
            Message[] messages = folderName.getMessages();
            int length = Math.min(to - from, messages.length);
            Message[] toReturn = new Message[length];
            int a = 0;
            for (int i = messages.length - from - 1; i >= messages.length - length - from; i--) {
                toReturn[a++] = messages[i];
            }
            return toReturn;
        } catch (MessagingException e) {
            System.out.println("Can't download messages");
            throw new RuntimeException(e);
        }

    }

    public Message getLatestMessage(Folder folder) {
        try {
            Message[] messages = folder.getMessages();
            if (messages.length != 0) return messages[messages.length - 1];
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<IMAPFolder> getMainFolders() {
        List<IMAPFolder> allFolders = new ArrayList<>();
        try {
            for (Folder folder : store.getDefaultFolder().list()) {
                if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                    allFolders.add((IMAPFolder) folder);
                }
            }
        } catch (MessagingException e) {
            System.out.println("Can't get all folders");
            throw new RuntimeException(e);
        }
        return allFolders;
    }

    public IMAPFolder getInnerFolder(TreeItem<String> name) {
        String sb = name.getValue();
        while (name.getParent().getParent() != null) {
            name = name.getParent();
            sb = name.getValue() + "/" + sb;
        }
        try {
            return (IMAPFolder) store.getFolder(sb);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
