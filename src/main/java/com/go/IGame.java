package com.go;

interface IGame {
    void newGame();

    void saveGame();

    void returnGame();

    boolean addStoneByCords(int x, int y);

    void turn();

    Stone[][] getBoardContent(Board board);

    int getBoardSize();
}
