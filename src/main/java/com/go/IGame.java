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

    // Метод, который возвращает текущего игрока
    Game.PlayerColor getCurrentPlayer();

    // Метод, который записывает игру в файл
    void saveArrayToFile(File file, Stone[][] arr);

    // Метод, который читает игру из файла
    Stone[][] loadArrayFromFile(File file);
}
