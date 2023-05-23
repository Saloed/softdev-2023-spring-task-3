package com.go;

public class Board implements IBoard {

    public final static int BOARD_SIZE = 13; // Размер игрового поля

    private Stone[][] positions; // Хранит состояние игровой доски

    // Конструктор
    public Board() {
        positions = new Stone[BOARD_SIZE][BOARD_SIZE];
    }

    // Добавление камня
    @Override
    public boolean addStone(Stone stone) {
        if (positions[stone.x()][stone.y()] != null) {
            return false;
        }

        LibertiesSurvivalRule checkLiberties = new LibertiesSurvivalRule();
        SameColorSurvivalRule checkSameColor = new SameColorSurvivalRule();

        if (checkLiberties.check(stone, this) || checkSameColor.check(stone, this)) {
            positions[stone.x()][stone.y()] = stone;
            return true;
        }
        return false;
    }

    // Удаление камня с доски
    @Override
    public void removeStone(Stone stone) {
        positions[stone.x()][stone.y()] = null;
    }

    // Очистка игровой доски
    @Override
    public void clearBoard() {
        positions = new Stone[BOARD_SIZE][BOARD_SIZE];
    }

    // Возврат состояния конкретной позиции
    @Override
    public Stone getPosition(int x, int y) {
        return positions[x][y];
    }

    // Проверка пустоты координаты
    @Override
    public boolean isEmptyPosition(int x, int y) {
        return getPosition(x, y) == null;
    }

    // Проверка допустимости обращения к координате
    @Override
    public boolean isValidXBoundary(int x) {
        return x > 0 && x < positions.length - 1;
    }

    // Проверка допустимости обращения к координате
    @Override
    public boolean isValidYBoundary(int y) {
        return y > 0 && y < positions.length - 1;
    }

    // Установка состояния игрового поля
    @Override
    public void setPositions(Stone[][] positions) {
        this.positions = positions;
    }

    // Возврат состояния игрового поля
    @Override
    public Stone[][] getPositions() {
        return positions;
    }
}
