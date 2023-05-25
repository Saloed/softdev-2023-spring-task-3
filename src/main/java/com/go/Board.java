package com.go;

public class Board implements IBoard {
    public final static int BOARD_SIZE = 13; // Размер игрового поля
    private Stone[][] positions; // Хранит состояние игровой доски

    public double capturedStonesWhite = 6.5; // Количество выигранных белых очков
    public int capturedStonesBlack = 0; //Количество выигранных черных очков


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

        ICheckSurvivalGroupRule checkLiberties = new LibertiesSurvivalRule();
        ICheckSurvivalGroupRule checkSameColor = new SameColorSurvivalRule();

        positions[stone.x()][stone.y()] = stone;

        if (!(checkLiberties.check(stone, this) || checkSameColor.check(stone, this))) {
            positions[stone.x()][stone.y()] = null;
            return false;
        }

        int[] dx = {-1, 1, 0, 0};  // Смещение по горизонтали для каждого направления
        int[] dy = {0, 0, -1, 1};  // Смещение по вертикали для каждого направления

        // Проверка как ход повлиял на соседние позиции
        for (int i = 0; i < 4; i++) {
            int nx = stone.x() + dx[i];
            int ny = stone.y() + dy[i];
            if (isValidBoundary(nx, ny)) {
                Stone neighbor = getPosition(nx, ny);
                if (neighbor != null && neighbor.color() != stone.color()) {
                    if (!(checkLiberties.check(neighbor, this) || checkSameColor.check(neighbor, this))) {
                        positions[nx][ny] = null;
                    }
                }
            }
        }
        return true;
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

    //Проверяет допустимость обращения к позиции
    @Override
    public boolean isValidBoundary(int x, int y) {
        return x >= 0 && x < positions.length && y >= 0 && y < positions.length;
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
