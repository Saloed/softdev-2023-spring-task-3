package com.UI;

import com.go.Game;

import javax.swing.*;
import java.io.File;

class SaveGameListener implements GameButtonsControlPanel.SaveGameListener {

    private final Game game;

    SaveGameListener(Game game) {
        this.game = game;
    }

    @Override
    public void onSaveGameClick() {
        JFileChooser fileChooser = new JFileChooser("./Save games");
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            game.saveGame(selectedFile);
        }
    }
}