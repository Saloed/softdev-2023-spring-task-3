package org.game.game;

import java.util.Arrays;

import static org.game.game.Constants.*;

public class HexGrid {

    private final int[][] grid;

    public HexGrid() {
        this.grid = new int[Constants.getArraySide()][Constants.getArraySide()];
        filling();
    }

    private void filling() {
        int i = Constants.getSideLength() - 1;
        int j = Constants.getSideLength() - 1;
        for (int q = 0; q < i; q++) {
            for (int r = 0; r < j; r++) {
                this.grid[q][r] = -1;
            }
            j--;
        }
        i = Constants.getSideLength() - 1;
        j = Constants.getSideLength() - 1;
        for (int q = Constants.getArraySide() - 1; q > i; q--) {
            for (int r = Constants.getArraySide() - 1; r > j; r--) {
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
        int[] ret = new int[Constants.getArraySide()];
        for(int q = 0; q < Constants.getArraySide(); q++) {
            ret[q] = grid[q][r];
        }
        return ret;
    }

    public void setColumn(int r, int[] newColumn) {
        for (int q = 0; q < Constants.getArraySide(); q++) {
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
        int[] ret = new int[Constants.getArraySide() - q];
        int r = Constants.getArraySide() - 1;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = grid[q][r];
            q++; r--;
        }
        return ret;
    }

    public void setLowerDiagonal(int q, int[] newDiagonal) {
        int r = Constants.getArraySide() - 1;
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

    public int[][] getArray() {
        return grid;
    }

    public void copyTo(HexGrid copy) {
        for (int q = 0; q < Constants.getArraySide(); q++) {
            for (int r = 0; r < Constants.getArraySide(); r++) {
                copy.setState(q, r, this.getState(q, r));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof HexGrid)) return false;
        HexGrid o = (HexGrid) obj;
        return Arrays.deepEquals(getArray(), o.getArray());
    }
}

