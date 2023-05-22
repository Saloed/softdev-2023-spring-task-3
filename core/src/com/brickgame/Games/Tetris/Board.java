package com.brickgame.Games.Tetris;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Piece;

public class Board {

    Piece[][] board = new Piece[10][20];
    SpriteBatch batch;


    public Board(SpriteBatch batch) {
        this.batch = batch;
    }


    public void setTetromino(Tetromino tetromino) {
        for (Piece piece : tetromino.tetromino) {
            board[(int) piece.getX()][(int) piece.getY()] = new Piece(piece.getX(), piece.getY());
        }
    }

    public void deleteRow(int row) {
        for (int y = row; y < 19; y++) {
            for (int x = 0; x < 10; x++) {
                board[x][y] = board[x][y + 1];
                if (board[x][y] != null) {
                    board[x][y].setY(board[x][y].getY()- 1); // Обновляем координату y объекта Piece после перемещения
                }
            }
        }
        // Очистка верхней строки
        for (int x = 0; x < 10; x++) {
            board[x][19] = null;
        }
    }

    public void clearRows() {
        for (int y = 0; y < 20; y++) {
            boolean fullRow = true; // флаг, показывающий, что вся строка заполнена

            // Проверяем, есть ли пустые клетки в ряду
            for (int x = 0; x < 10; x++) {
                if (board[x][y] == null) {
                    fullRow = false; // обнаружена пустая клетка
                    break;
                }
            }
            // Если ряд полностью заполнен, удаляем его
            if (fullRow) {
                deleteRow(y);
                y--;
                TetrisGameScreen.sidePanel.score.increaseScore();
                TetrisGameScreen.game.achives.play();
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
