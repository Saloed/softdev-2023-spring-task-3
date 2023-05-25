package com.UI;

import com.go.Board;
import com.go.Game;
import com.go.Champion;

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

            Champion winner = new Champion(board);
            Color winnerColor = winner.getChampion();
            String champion;
            double score = winner.getScore();
            if (winnerColor == Color.BLACK) {
                champion = "Черные";
            } else {
                champion = "Белые";
            }

            JOptionPane.showMessageDialog(null, champion + " победили с отрывом в... \n" +
                    score + " очков! Учитесь!", "Игра завершена!", JOptionPane.INFORMATION_MESSAGE);
            consecutivePasses = 0;
        }
        previousPlayer = currentPlayer;
    }
}
