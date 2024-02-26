package com.app.gui;


import javafx.scene.control.TreeItem;

import javax.mail.Folder;
import javax.mail.MessagingException;

public class Dir extends TreeItem<String> {
    private final Folder folder;

    Dir(Folder folder) {
        this.folder = folder;
        this.setValue(folder.getName());
        addSubFolders();
    }

    private void addSubFolders() {
        try {
            if (folder.list().length != 0) {
                for (Folder fold : folder.list()) {
                    Dir newOne = new Dir(fold);
                    this.getChildren().add(newOne);
                }
            }
        } catch (MessagingException e) {
            System.out.println("Couldn't add subfolders" + folder.getName());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return folder.getName();
    }
}
