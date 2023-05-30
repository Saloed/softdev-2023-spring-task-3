package com.go;

import java.util.List;

public class Board implements IBoard {
    public final static int BOARD_SIZE = 13; // Размер игрового поля
    private Stone[][] positions; // Хранит состояние игровой доски

    private double capturedStonesWhite = 0; // Количество выигранных белых очков
    private int capturedStonesBlack = 0; //Количество выигранных черных очков
    private boolean repeatPosition; // Проверка повторяющейся позиции


    // Конструктор
    public Board() {
        positions = new Stone[BOARD_SIZE][BOARD_SIZE];
    }

    // Добавление камня
    @Override
    public boolean addStone(Stone stone) {
        int x = stone.x();
        int y = stone.y();

        if (positions[x][y] != null) {
            return false;
        }

        ICheckSurvivalGroupRule libertiesRule = new LibertiesSurvivalRule();
        ICheckSurvivalGroupRule sameColorRule = new SameColorSurvivalRule();

        positions[x][y] = stone;

        boolean hasLiberties = libertiesRule.check(stone, this);
        boolean hasSameColor = sameColorRule.check(stone, this);

        if (!(hasLiberties || hasSameColor)) {
            if (!repeatPosition && checkNeighbors(stone, libertiesRule, sameColorRule)) {
                repeatPosition = true;
                return true;
            } else {
                positions[x][y] = null;
                repeatPosition = false;
                return false;
            }
        }

        checkNeighbors(stone, libertiesRule, sameColorRule);
        repeatPosition = false;
        return true;
    }

    @Override
    public boolean checkNeighbors(Stone stone, ICheckSurvivalGroupRule checkLiberties, ICheckSurvivalGroupRule checkSameColor) {
        int[] dx = {-1, 1, 0, 0}; // Смещение по горизонтали для каждого направления
        int[] dy = {0, 0, -1, 1}; // Смещение по вертикали для каждого направления

        for (int i = 0; i < 4; i++) {
            int nx = stone.x() + dx[i];
            int ny = stone.y() + dy[i];

            if (isValidBoundary(nx, ny)) {
                Stone neighbor = getPosition(nx, ny);

                if (neighbor != null && neighbor.playerColor() != stone.playerColor()) {
                    if (!(checkLiberties.check(neighbor, this) || checkSameColor.check(neighbor, this))) {
                        SameColorSurvivalRule survivalRule = new SameColorSurvivalRule();
                        survivalRule.check(neighbor, this);
                        removeStoneGroup(survivalRule.getStoneGroup());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Удаление камня с доски
    @Override
    public void removeStone(Stone stone) {
        positions[stone.x()][stone.y()] = null;
    }

    // Метод осуществляющий удаление группы с доски
    @Override
    public void removeStoneGroup(List<Stone> stoneGroup) {
        for (Stone stone : stoneGroup) {
            if (stone.playerColor() == Game.PlayerColor.WHITE)
                capturedStonesBlack++;
            else {
                capturedStonesWhite++;
            }
            removeStone(stone);
        }
    }


    // Очистка игровой доски
    @Override
    public void clearBoard() {
        positions = new Stone[BOARD_SIZE][BOARD_SIZE];
        capturedStonesWhite = 1.5;
        capturedStonesBlack = 0;
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

    @Override
    public double getCapturedStonesWhite() {
        return capturedStonesWhite;
    }

    @Override
    public int getCapturedStonesBlack() {
        return capturedStonesBlack;
    }
}
