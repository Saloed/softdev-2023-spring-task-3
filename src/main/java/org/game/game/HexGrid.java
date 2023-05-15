package org.game.game;

import static org.game.game.Constants.*;

public class HexGrid {

    private static int[][] grid;

    public HexGrid() {
        grid = new int[COUNT_TILES_Q][COUNT_TILES_R];

        for (int q = 0; q < COUNT_TILES_Q; q++) {
            for (int r = 0; r < COUNT_TILES_Q; r++) {
                grid[q][r] = 0;
            }
        }

        grid[0][0] = -1;
        grid[0][1] = -1;
        grid[1][0] = -1;
        grid[3][4] = -1;
        grid[4][3] = -1;
        grid[4][4] = -1;
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
        int[] ret = new int[COUNT_TILES_Q];
        for(int q = 0; q < COUNT_TILES_Q; q++) {
            ret[q] = grid[q][r];
        }
        return ret;
    }

    public void setColumn(int r, int[] newColumn) {
        for (int q = 0; q < COUNT_TILES_Q; q++) {
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
        int[] ret = new int[COUNT_TILES_Q-q];
        int r = COUNT_TILES_R-1;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = grid[q][r];
            q++; r--;
        }
        return ret;
    }

    public void setLowerDiagonal(int q, int[] newDiagonal) {
        int r = COUNT_TILES_R-1;
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

    public void print() {
        System.out.print("  ");
        for (int i = 2; i < 5; i++) {
            System.out.print(grid[0][i] + " ");
        }
        System.out.print("\n");
        for (int i = 1; i < 5; i++) {
            System.out.print(" " + grid[1][i]);
        }
        System.out.print("\n");
        for (int i = 0; i < 5; i++) {
            System.out.print(grid[2][i] + " ");
        }
        System.out.print("\n");
        for (int i = 0; i < 4; i++) {
            System.out.print(" " + grid[3][i]);
        }
        System.out.print("\n");
        System.out.print("  ");
        for (int i = 0; i < 3; i++) {
            System.out.print(grid[4][i] + " ");
        }
        System.out.print("\n");
    }
}

