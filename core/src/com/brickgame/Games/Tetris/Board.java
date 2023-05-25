package com.brickgame.Games.Tetris;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;

public class Board {

    public Piece[][] board = new Piece[BrickGame.GRID_WIDTH][BrickGame.GRID_HEIGHT];
    private final SpriteBatch batch;

    public Board(SpriteBatch batch) {
        this.batch = batch;
    }

    public boolean isNeedPlayAchives, isNeedIncreaseScore;


    public void setTetromino(Tetromino tetromino) {
        for (Piece piece : tetromino.tetromino) {
            board[(int) piece.getX()][(int) piece.getY()] = new Piece(piece.getX(), piece.getY());
        }
    }

    private void deleteRow(int row) {
        for (int y = row; y < BrickGame.GRID_HEIGHT - 1; y++) {
            for (int x = 0; x < BrickGame.GRID_WIDTH; x++) {
                board[x][y] = board[x][y + 1];
                if (board[x][y] != null) {
                    board[x][y].setY(board[x][y].getY() - 1); // Обновляем координату y объекта Piece после перемещения
                }
            }
        }
        // Очистка верхней строки
        for (int x = 0; x < BrickGame.GRID_WIDTH; x++) {
            board[x][BrickGame.GRID_HEIGHT - 1] = null;
        }
    }

    public void clearRows() {
        for (int y = 0; y < BrickGame.GRID_HEIGHT; y++) {
            boolean fullRow = true; // флаг, показывающий, что вся строка заполнена

            // Проверяем, есть ли пустые клетки в ряду
            for (int x = 0; x < BrickGame.GRID_WIDTH; x++) {
                if (board[x][y] == null) {
                    fullRow = false; // обнаружена пустая клетка
                    break;
                }
            }
            // Если ряд полностью заполнен, удаляем его
            if (fullRow) {
                deleteRow(y);
                y--;
                isNeedIncreaseScore = true;
                isNeedPlayAchives = true;
            }
        }
    }

    public void draw() {
        for (Piece[] pieces : board) {
            for (Piece piece : pieces) {
                if (piece != null) {
                    piece.draw(batch);
                }
            }
        }
    }
}
