package com.go;

import java.io.File;

interface IGame {
    void newGame();

    Stone getLastFixedStone();

    boolean addStoneByCords(int x, int y);

    void turn();

    Stone[][] getBoardContent(Board board);

    int getBoardSize();

    // Метод, который сохраняет игру
    void saveGame(File file);

    // Метод, который возвращает сохраненную игру из файла
    void returnGame(File file);
}
