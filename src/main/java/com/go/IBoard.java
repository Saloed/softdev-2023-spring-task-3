package com.go;

import java.util.List;

interface IBoard {
    boolean addStone(Stone stone);

    boolean checkNeighbors(Stone stone, ICheckSurvivalGroupRule checkLiberties, ICheckSurvivalGroupRule checkSameColor);

    void removeStone(Stone stone);

    void removeStoneGroup(List<Stone> stoneGroup);

    void clearBoard();

    Stone getPosition(int x, int y);

    boolean isEmptyPosition(int x, int y);

    boolean isValidBoundary(int x, int y);

    boolean isValidXBoundary(int x);

    boolean isValidYBoundary(int y);

    void setPositions(Stone[][] positions);

    Stone[][] getPositions();

    double getCapturedStonesWhite();

    int getCapturedStonesBlack();
}
