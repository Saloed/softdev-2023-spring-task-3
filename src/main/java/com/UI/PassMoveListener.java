package com.UI;

import com.go.Board;
import com.go.Game;
import com.go.Stone;

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

        if (consecutivePasses == 1) {
            int blackCount = 0;
            double whiteCount = 0.5;

            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                for (int y = 0; y < Board.BOARD_SIZE; y++) {
                    Stone stone = board.getPosition(x, y);
                    if (stone != null) {
                        if (stone.color() == Color.BLACK) {
                            blackCount++;
                        } else if (stone.color() == Color.WHITE) {
                            whiteCount++;
                        }
                    }
                }
            }
            String winner = "";
            if (blackCount > whiteCount) {
                winner += "Черные";
            } else {
                winner += "Белые";
            }

            JOptionPane.showMessageDialog(null, winner + " победили с большим отрывом...", "Игра завершена!", JOptionPane.INFORMATION_MESSAGE);
            consecutivePasses = 0;
        }
        previousPlayer = currentPlayer;
    }
}
