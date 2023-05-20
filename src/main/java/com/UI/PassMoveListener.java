package com.UI;

import com.go.Game;

import javax.swing.*;
import java.awt.*;

class PassMoveListener implements GameButtonsControlPanel.PassMoveListener {

    private final Game game;
    private int k = 0;

    PassMoveListener(Game game) {
        this.game = game;
    }

    @Override
    public void onPassMoveClick() {
        Color currentPlayer = game.getCurrentPlayer();
        k++;
        game.move();

        if (currentPlayer != game.getPreviousPlayer().color()) {
            k = 0;
        }

        if (k == 2) {
            JOptionPane.showMessageDialog(null, "Я выиграл! Ну а вообще, можете посчитать " +
                    "и определить победителя самостоятельно.", "Игра завершена!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
