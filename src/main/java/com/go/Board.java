package com.go;

public class Board implements IBoard {

    public final static int BOARD_SIZE = 13;
    public final ICheckSurvivalGroupRule checkLiberties;
    public final ICheckSurvivalGroupRule checkSameColor;

    public Stone[][] positions;

    public Board(ICheckSurvivalGroupRule checkLiberties,
                 ICheckSurvivalGroupRule checkSameColor,
                 int boardSize) {
        this.checkLiberties = checkLiberties;
        this.checkSameColor = checkSameColor;
        positions = new Stone[boardSize][boardSize];
    }

    public void addStone(Stone stone, Game game) {

        if (checkSameColor.check(stone, this) || checkLiberties.check(stone,this)) {
            positions[stone.x()][stone.y()] = stone;
        } else {
            game.move();
            removeStone(stone);
        }

    }

    @Override
    public void removeStone(Stone stone) {
        positions[stone.x()][stone.y()] = null;
    }

    @Override
    public void clearBoard() {
        positions = new Stone[BOARD_SIZE][BOARD_SIZE];
    }

    public Stone[][] getPositions() {
        return positions;
    }

    @Override
    public Stone getPosition(int x, int y) {
        return positions[x][y];
    }

    @Override
    public boolean isEmptyPosition(int x, int y) {
        return getPosition(x, y) == null;
    }

    @Override
    public boolean isValidXBoundary(int x) {
        return x > 0 && x < positions.length - 1;
    }

    @Override
    public boolean isValidYBoundary(int y) {
        return y > 0 && y < positions.length - 1;
    }
}
