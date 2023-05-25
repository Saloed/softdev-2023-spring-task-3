package com.UI;

import com.go.Board;
import com.go.Game;

import javax.swing.*;
import java.awt.*;

public class PassMoveListener implements GameButtonsControlPanel.PassMoveListener {

    private final Game game;
    private final Board board;
    private Color previousPlayer;
    private int consecutivePasses = 0;

    PassMoveListener(Game game, Board board) {
        this.game = game;
        this.board = board;
    }

    @Override
    public void onPassMoveClick() {
        Color currentPlayer = game.getCurrentPlayer();

        game.turn();

        if (previousPlayer != null && previousPlayer != currentPlayer) {
            consecutivePasses++;
        } else {
            consecutivePasses = 0;
        }

        if (consecutivePasses == 2) {

            JOptionPane.showMessageDialog(null, " победили с большим отрывом...", "Игра завершена!", JOptionPane.INFORMATION_MESSAGE);
            consecutivePasses = 0;
        }
        previousPlayer = currentPlayer;
    }
}
