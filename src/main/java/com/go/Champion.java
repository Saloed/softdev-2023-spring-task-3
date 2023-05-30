package com.go;

import com.go.Game.PlayerColor;

public class Champion {
    private final Board board;
    private final int boardSize;
    private int blackStonesCount;
    private double whiteStonesCount;

    public Champion(Board board) {
        this.board = board;
        this.boardSize = board.getPositions().length;
        this.blackStonesCount = board.getCapturedStonesBlack();
        this.whiteStonesCount = 1.5 + board.getCapturedStonesWhite();
        System.out.println(blackStonesCount + " " + whiteStonesCount);
        countStones();
    }

    public void countStones() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Stone position = board.getPosition(x, y);
                if (!board.isEmptyPosition(x, y)) {
                    if (position.playerColor() == PlayerColor.BLACK) {
                        blackStonesCount++;
                    } else if (position.playerColor() == PlayerColor.WHITE) {
                        whiteStonesCount++;
                    }
                }
            }
        }
    }

    public double getScore() {
        return Math.abs(blackStonesCount - (whiteStonesCount));
    }

    public PlayerColor getChampion() {
        return (blackStonesCount > whiteStonesCount) ? PlayerColor.BLACK : PlayerColor.WHITE;
    }
}