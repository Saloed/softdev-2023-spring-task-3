package com.go;

interface IBoard {
    void addStone(Stone stone, Game game);

    void removeStone(Stone stone);

    void clearBoard();

    Stone getPosition(int x, int y);

    boolean isEmptyPosition(int x, int y);

    boolean isNotEmptyPosition(int x, int y);

    boolean isValidXBoundary(int x);

    boolean isValidYBoundary(int y);

}
