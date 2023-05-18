package org.game.game;

import static org.game.game.Constants.*;

public class HexGrid {

    private final int[][] grid;

    public HexGrid() {
        this.grid = new int[ARRAY_SIDE][ARRAY_SIDE];
        filling();
    }

    private void filling() {
        int i = SIDE_LENGTH - 1;
        int j = SIDE_LENGTH - 1;
        for (int q = 0; q < i; q++) {
            for (int r = 0; r < j; r++) {
                this.grid[q][r] = -1;
            }
            j--;
        }
        i = SIDE_LENGTH - 1;
        j = SIDE_LENGTH - 1;
        for (int q = ARRAY_SIDE - 1; q > i; q--) {
            for (int r = ARRAY_SIDE - 1; r > j; r--) {
                this.grid[q][r] = -1;
            }
            j++;
        }
    }

    public int getState(int q, int r) {
        return grid[q][r];
    }

    public void setState(int q, int r, int state) {
        grid[q][r] = state;
    }

    public int[] getLine(int q) {
        return grid[q];
    }

    public void setLine(int q, int[] newLine) {
        grid[q] = newLine;
    }

    public int[] getColumn(int r) {
        int[] ret = new int[ARRAY_SIDE];
        for(int q = 0; q < ARRAY_SIDE; q++) {
            ret[q] = grid[q][r];
        }
        return ret;
    }

    public void setColumn(int r, int[] newColumn) {
        for (int q = 0; q < ARRAY_SIDE; q++) {
            grid[q][r] = newColumn[q];
        }
    }

    public int[] getUpperDiagonal(int q) {
        int[] ret = new int[q+1];
        for(int r = 0; r < ret.length; r++) {
            ret[r] = grid[q][r];
            q--;
        }
        return ret;
    }

    public int[] getLowerDiagonal(int q) {
        int[] ret = new int[ARRAY_SIDE-q];
        int r = ARRAY_SIDE - 1;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = grid[q][r];
            q++; r--;
        }
        return ret;
    }

    public void setLowerDiagonal(int q, int[] newDiagonal) {
        int r = ARRAY_SIDE - 1;
        for (int j : newDiagonal) {
            grid[q][r] = j;
            q++; r--;
        }
    }

    public void setUpperDiagonal(int q, int[] newDiagonal) {
        for (int r = 0; r < newDiagonal.length; r++) {
            grid[q][r] = newDiagonal[r];
            q--;
        }
    }

    protected int[][] getArray() {
        return grid;
    }
}

