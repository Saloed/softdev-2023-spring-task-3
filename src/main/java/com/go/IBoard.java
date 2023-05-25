package com.go;

interface IBoard {
    boolean addStone(Stone stone);
    void removeStone(Stone stone);

    void clearBoard();

    Stone getPosition(int x, int y);

    boolean isEmptyPosition(int x, int y);

    boolean isValidBoundary(int x, int y);

    boolean isValidXBoundary(int x);

    boolean isValidYBoundary(int y);

    void setPositions(Stone[][] positions);

    Stone[][] getPositions();
}
