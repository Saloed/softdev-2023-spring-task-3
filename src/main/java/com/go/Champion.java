package com.go;

import java.awt.*;

public class Champion {
    private final Board board;
    private final int boardSize;
    private int blackStonesCount;
    private double whiteStonesCount;

    public Champion(Board board) {
        this.board = board;
        this.boardSize = board.getPositions().length;
        this.blackStonesCount = 0;
        this.whiteStonesCount = 6.5;
        countStones();
    }

    public void countStones() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Stone position = board.getPosition(x, y);
                if (!board.isEmptyPosition(x, y)) {
                    if (position.color() == Color.BLACK) {
                        blackStonesCount++;
                    } else if (position.color() == Color.WHITE) {
                        whiteStonesCount++;
                    }
                }
            }
        }
    }

    public double getScore() {
        return Math.abs(blackStonesCount - (whiteStonesCount + 6.5));
    }

    public Color getChampion() {
        return (blackStonesCount > whiteStonesCount + 6.5) ? Color.BLACK : Color.WHITE;
    }
}