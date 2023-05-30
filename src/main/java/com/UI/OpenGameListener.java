package com.UI;

import com.go.Game;

import javax.swing.*;
import java.io.File;

class OpenGameListener implements GameButtonsControlPanel.OpenGameListener {

    private final Game game;
    private final GamePanel gamePanel;

    OpenGameListener(Game game, GamePanel gamePanel) {
        this.game = game;
        this.gamePanel = gamePanel;
    }

    @Override
    public void onOpenGameClick() {
        JFileChooser fileChooser = new JFileChooser("./Save games");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            game.returnGame(selectedFile);
            gamePanel.repaint();
        }
    }
}