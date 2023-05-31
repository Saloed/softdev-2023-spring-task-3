package com.app.mail;


import com.app.gui.ListenerOfMessages;
import com.app.gui.Mail;
import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EmailReceiver {

    private Store store;

    private Session nowSession;

    private ExecutorService executorService;

    public Store getStore() {
        return store;
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Session getNowSession() {
        return nowSession;
    }

    public void setNowSession(Session nowSession) {
        this.nowSession = nowSession;
    }


    public void setNotifications() {
        this.executorService = Executors.newFixedThreadPool(getMainFolders().size() + 1);
        for (IMAPFolder folder : this.getMainFolders()) {
            openFolderConnection(folder);
            executorService.execute(() -> {
                folder.addMessageCountListener(new ListenerOfMessages(this, folder));
                try {
                    while (true) {
                        folder.idle(true);
                    }
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public EmailReceiver(Mail mail) {
        setNowSession(mail.getSession());
        openStoreConnection();
        setNotifications();
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
}
