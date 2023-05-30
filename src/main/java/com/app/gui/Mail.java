package com.app.gui;

import com.app.mail.EmailReceiver;
import com.app.mail.UserData;
import com.sun.mail.imap.IMAPFolder;
import javafx.scene.control.TreeItem;

import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Mail {

    public String getUserName() {
        return userName;
    }

    private Session session;


    public Session getSession() {
        return session;
    }

    private String userName;


    public Mail(String userName) {
        this.userName = userName;
        this.session = UserData.getUser(userName).getSession();
    }

    public Mail(Properties props) {
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        props.getProperty("username"), props.getProperty("password"));
            }
        });
        this.userName = session.getProperty("username");
    }

    public List<TreeItem<String>> returnStringDirs(EmailReceiver receiver) {
        List<TreeItem<String>> listToReturn = new ArrayList<>();
        for (IMAPFolder folder : receiver.getMainFolders()) {
            listToReturn.add(new Dir(folder));
        }
        return listToReturn;
    }

    public Mail() {
    }

    @Override
    public String toString() {
        return userName;
    }

}
