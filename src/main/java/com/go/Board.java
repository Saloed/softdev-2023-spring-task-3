package com.go;


interface IBoard {
    void addStone(Stone stone);
    void removeStone(Stone stone);
    void clearBoard();
}

public class Board implements IBoard {

    public Stone[][] positions;

    public Board(int boardSize) {
        positions = new Stone[boardSize][boardSize];
    }

    public void addStone(Stone stone) {
        int x = stone.x();
        int y = stone.y();
        positions[x][y] = stone;
    }

    @Override
    public void removeStone(Stone stone) {
        int x = stone.x();
        int y = stone.y();
        if (positions[x][y] == stone) {
            positions[x][y] = null;
        }
    }

    @Override
    public void clearBoard() {
        positions = new Stone[13][13];
    }

    public Stone getPosition(int x, int y) {
        return positions[x][y];
    }
}
