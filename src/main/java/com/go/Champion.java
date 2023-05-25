package com.go;

import java.awt.*;

public class Champion {
    private final int boardSize;
    private final int scoreBlack;
    private final double scoreWhite;

    // Конструктор
    public Champion(Board board) {
        this.boardSize = board.getPositions().length;
        this.scoreBlack = calculateTerritoryScore(Color.BLACK, board) + board.capturedStonesBlack;
        this.scoreWhite = calculateTerritoryScore(Color.WHITE, board) + board.capturedStonesWhite;
    }

    // Подсчет количества очков территории
    private int calculateTerritoryScore(Color color, Board board) {
        int score = 0;
        boolean[][] visited = new boolean[boardSize][boardSize];

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                // Проверка была ли территория посещена и не содержит ли она камень
                if (!visited[x][y] && board.isEmptyPosition(x, y)) {
                        Territory territory = calculateTerritory(x, y, board, visited);
                        if (territory.isSurrounded() && territory.getColor() == color) {
                                score += territory.getSize();
                            }
                        }
                    }
                }
        return score;
    }

    // Вычисление территории по начальным координатам
    private Territory calculateTerritory(int x, int y, Board board, boolean[][] visited) {
        Territory territory = new Territory();
        visitedPosition(x, y, territory, board, visited);
        return territory;
    }

    // Проверка позиций и построение территории
    private void visitedPosition(int x, int y, Territory territory, Board board, boolean[][] visited) {

        if (!board.isValidBoundary(x, y) || visited[x][y]) {
            return;
        }

        visited[x][y] = true;

        int[] dx = {-1, 1, 0, 0};  // Смещения по горизонтали для каждого направления
        int[] dy = {0, 0, -1, 1};  // Смещения по вертикали для каждого направления

        // Рекурсивный поиск соседей
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (board.getPosition(nx, ny) == null) {
                territory.increaseSize();
                visitedPosition(nx, ny, territory, board, visited);
            } else {
                territory.setColor(board.getPosition(x, y).color());
            }
        }
    }

    // Возвращает, на сколько очков произошел отрыв победителя
    public double getScore() {
        return Math.abs(scoreBlack - scoreWhite);
    }

    // Возвращает победителя
    public Color getChampion() {
        return (scoreBlack > scoreWhite) ? Color.BLACK : Color.WHITE;
    }
}
