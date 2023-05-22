package com.brickgame.Games.Tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Piece;

import java.util.ArrayList;

public class Tetromino {

    Piece centerTetromino;
    static float TIME_MOVE, TIME_MOVE_LIMIT = 0.6f;

    ArrayList<Piece> tetromino;
    int type;

    public SpriteBatch batch;
/*

// Цифра - индекс ячейки в фигуре, 0 - центральная.
T:
102
 3

I:
2
1
0
3

J:
 3
 0
21

L:
3
0
12

O:
23
01



S:
 03
21

Z:
30
 12

*/

    int[][] distances = new int[][]{
            new int[]{1, 1, 1}, // T - 0
            new int[]{1, 2, 1}, // I - 1
            new int[]{1, 1, 1},  // J - 2
            new int[]{1, 1, 1}, // L - 3
            new int[]{1, 1, 1}, // O - 4
            new int[]{1, 1, 1}, // S - 5
            new int[]{1, 1, 1} // Z - 6
    };
    Direction[][] directions = new Direction[][]{
            new Direction[]{Direction.LEFT, Direction.RIGHT, Direction.DOWN}, // T - 0
            new Direction[]{Direction.UP, Direction.UP, Direction.DOWN}, // I - 1
            new Direction[]{Direction.DOWN, Direction.LEFT_DOWN, Direction.UP}, // J - 2
            new Direction[]{Direction.DOWN, Direction.RIGHT_DOWN, Direction.UP}, // L - 3
            new Direction[]{Direction.UP, Direction.RIGHT, Direction.RIGHT_UP}, // O - 4
            new Direction[]{Direction.DOWN, Direction.LEFT_DOWN, Direction.RIGHT}, // S - 5
            new Direction[]{Direction.DOWN, Direction.RIGHT_DOWN, Direction.LEFT} // Z - 6
    };


    public Tetromino(SpriteBatch batch, Piece centerTetromino, int type) {
        this.batch = batch;
        this.type = type;
        tetromino = new ArrayList<>();
        tetromino.add(centerTetromino);
        this.centerTetromino = centerTetromino;
        for (int i = 0; i < directions[type].length; i++) {
            int x = (int) (centerTetromino.getX() + directions[type][i].x * distances[type][i]);
            int y = (int) (centerTetromino.getY() + directions[type][i].y * distances[type][i]);
            Piece piece = new Piece(x, y);
            piece.setDirection(directions[type][i]);
            piece.setDistance(distances[type][i]);
            tetromino.add(piece);
        }
    }

    public void move() {
        TIME_MOVE += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && canMoveLeft()) {
            centerTetromino.setX(centerTetromino.getX() - 1);
            updateTetrimino();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && canMoveRight()) {
            centerTetromino.setX(centerTetromino.getX() + 1);
            updateTetrimino();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && canMoveDown()) {
            centerTetromino.setY(centerTetromino.getY() - 1);
            updateTetrimino();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            rotate();
        }
        if (TIME_MOVE >= TIME_MOVE_LIMIT) {
            if (canMoveDown()) {
                for (Piece piece : tetromino) {
                    piece.setY(piece.getY() - 1);
                }
                updateTetrimino();
            } else {
                // Если нижнее перемещение невозможно, создайте новую фигуру и сделайте ее активной
                TetrisGameScreen.board.setTetromino(TetrisGameScreen.currentTetromino);
                TetrisGameScreen.board.clearRows();
                TetrisGameScreen.game.hit.play();
                TetrisGameScreen.createNewTetromino();
            }
            TIME_MOVE = 0;
        }
    }

    private boolean canMoveLeft() {
        for (Piece piece : tetromino) {
            if (piece.getX() - 1 < 0 || TetrisGameScreen.board.board[(int) piece.getX() - 1][(int) piece.getY()] != null) {
                return false; // Не может двигаться влево, если достигнута левая граница экрана или есть другая фигура
            }
        }
        return true;
    }


    private boolean canMoveRight() {
        for (Piece piece : tetromino) {
            if (piece.getX() + 1 > 9 || TetrisGameScreen.board.board[(int) piece.getX() + 1][(int) piece.getY()] != null) {
                return false; // Не может двигаться вправо, если достигнута правая граница экрана
            }
        }
        return true;
    }

    private boolean canMoveDown() {
        for (Piece piece : tetromino) {
            if (piece.getY() - 1 < 0 || TetrisGameScreen.board.board[(int) piece.getX()][(int) piece.getY() - 1] != null) {
                return false; // Не может двигаться вниз, если достигнута нижняя граница экрана
            }
        }
        return true;
    }


    public void rotate() {
        Tetromino tempTetromino = new Tetromino(this.batch, this.centerTetromino, this.type);
        for (int i = 1; i < tempTetromino.tetromino.size(); i++) {
            for (int j = 0; j < tempTetromino.tetromino.get(i).directions.size(); j++) {
                tempTetromino.tetromino.get(i).directions.set(j, tempTetromino.tetromino.get(i).directions.get(j).next());
            }
        }
        tempTetromino.updateTetrimino();
        boolean canRotate = true;
        for (Piece piece : tempTetromino.tetromino) {

            if (piece.getX() < 0 || piece.getX() >= 9 || piece.getY() < 0 || TetrisGameScreen.board.board[(int) piece.getX()][(int) piece.getY()] != null) {
                canRotate = false;
                break;
            }
        }
        if (canRotate) {
            for (int i = 1; i < tetromino.size(); i++) {
                for (int j = 0; j < tetromino.get(i).directions.size(); j++) {
                    tetromino.get(i).directions.set(j, tetromino.get(i).directions.get(j).next());
                }
            }
            updateTetrimino();
        }
    }


    public void updateTetrimino() {
        for (int i = 1; i < tetromino.size(); i++) {
            int dx = 0, dy = 0;

            for (Direction d : tetromino.get(i).directions) {
                dx += tetromino.get(i).getDistance() * d.x;
                dy += tetromino.get(i).getDistance() * d.y;
            }
            tetromino.get(i).setX(centerTetromino.getX() + dx);
            tetromino.get(i).setY(centerTetromino.getY() + dy);
        }
    }


    public void draw() {
        for (Piece piece : tetromino) {
            piece.draw(batch);
        }
    }
}

