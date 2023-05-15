package org.game.game;

import java.util.Random;

import static org.game.game.Constants.*;

public class MainLogic {

    public static int score;
    public static boolean endOfGame;
    public static boolean isThere16384;
    public static Direction direction;
    public static HexGrid grid;

    private static void merged16384() {
        isThere16384 = true;
    }

    public static void init(){
        score = 0;
        endOfGame = false;
        isThere16384 = false;
        direction = Direction.AWAITING;
        grid = new HexGrid();
    }

    public static boolean move() {
        boolean wasMoved = false;
        if(direction != Direction.AWAITING) {
            if(shift(direction)) {
                generateNewTile();
                wasMoved = true;
            }
            direction = Direction.AWAITING;
        }
        return wasMoved;
    }

    public static void input(String key){
        switch (key) {
            case "W" -> direction = Direction.UP_LEFT;
            case "E" -> direction = Direction.UP_RIGHT;
            case "A" -> direction = Direction.LEFT;
            case "D" -> direction = Direction.RIGHT;
            case "Z" -> direction = Direction.DOWN_LEFT;
            case "X" -> direction = Direction.DOWN_RIGHT;
            default -> direction = Direction.AWAITING;
        }
    }

    private static int[] reverse(int[] toReverse) {
        int[] ret = new int[toReverse.length];
        for (int i = 0; i < toReverse.length; i++) {
            ret[i] = toReverse[toReverse.length-i-1];
        }
        return ret;
    }

    private static boolean shift(Direction direction) {
        boolean ret = false;
        switch (direction) {
            case UP_RIGHT:
            case DOWN_LEFT:
                for(int q = COUNT_TILES_Q-1; q > 1; q--) {
                    int[] oldRow = grid.getUpperDiagonal(q);
                    if(direction == Direction.UP_RIGHT) {
                        oldRow = reverse(oldRow);
                    }
                    ShiftRowResult result = shiftRow(oldRow);
                    if(direction == Direction.UP_RIGHT) {
                        result.shiftedRow = reverse(result.shiftedRow);
                    }
                    grid.setUpperDiagonal(q, result.shiftedRow);
                    ret = ret || result.didItMoved;
                }
                for(int q = 1; q < 3; q++) {
                    int[] oldRow = grid.getLowerDiagonal(q);
                    if(direction == Direction.DOWN_LEFT) {
                        oldRow = reverse(oldRow);
                    }
                    ShiftRowResult result = shiftRow(oldRow);
                    if(direction == Direction.DOWN_LEFT) {
                        result.shiftedRow = reverse(result.shiftedRow);
                    }
                    grid.setLowerDiagonal(q, result.shiftedRow);
                    ret = ret || result.didItMoved;
                }
                break;
            case UP_LEFT:
            case DOWN_RIGHT:
                for (int q = 0; q < COUNT_TILES_Q; q++) {
                    int[] oldRow = grid.getColumn(q);
                    if(direction == Direction.DOWN_RIGHT) {
                        oldRow = reverse(oldRow);
                    }
                    ShiftRowResult result = shiftRow(oldRow);
                    if(direction == Direction.DOWN_RIGHT) {
                        result.shiftedRow = reverse(result.shiftedRow);
                    }
                    grid.setColumn(q, result.shiftedRow);
                    ret = ret || result.didItMoved;
                }
                break;
            case LEFT:
            case RIGHT:
                for (int q = 0; q < COUNT_TILES_Q; q++) {
                    int[] oldRow = grid.getLine(q);
                    if(direction == Direction.RIGHT) {
                        oldRow = reverse(oldRow);
                    }
                    ShiftRowResult result = shiftRow(oldRow);
                    if(direction == Direction.RIGHT) {
                        result.shiftedRow = reverse(result.shiftedRow);
                    }
                    grid.setLine(q, result.shiftedRow);
                    ret = ret || result.didItMoved;
                }
                break;
            default:
                ErrorCatcher.shiftFailure();
                break;
        }
        return ret;
    }

    private static class ShiftRowResult {
        boolean didItMoved;
        int[] shiftedRow;
    }

    private static ShiftRowResult shiftRow(int[] oldRow){
        ShiftRowResult ret = new ShiftRowResult();

        int[] oldRowWithoutZeros = new int[oldRow.length]; // without intervening zeros*
        {
            int j = 0;

            for(int i = 0; i < oldRow.length; i++) {
                if(oldRow[i] == -1) {
                    oldRowWithoutZeros[i] = -1;
                    j++;
                    continue;
                }
                if(oldRow[i] != 0) {
                    if(j != i) {
                        ret.didItMoved = true;
                    }
                    oldRowWithoutZeros[j] = oldRow[i];
                    j++;
                }
            }
        }
        ret.shiftedRow = new int[oldRowWithoutZeros.length];
        {
            int j = 0;

            {
                int i = 0;


                while (i < oldRowWithoutZeros.length) {
                    if((i+1 < oldRowWithoutZeros.length) && (oldRowWithoutZeros[i] == oldRowWithoutZeros[i + 1])
                            && oldRowWithoutZeros[i]>0) {
                        ret.didItMoved = true;
                        ret.shiftedRow[j] = oldRowWithoutZeros[i] * 2;
                        score += oldRowWithoutZeros[i] * 2;
                        if(ret.shiftedRow[j] == 16384) merged16384();
                        i++;
                    } else if (oldRowWithoutZeros[i] != -1){
                        ret.shiftedRow[j] = oldRowWithoutZeros[i];
                    } else {
                        ret.shiftedRow[i] = oldRowWithoutZeros[i];
                    }

                    j++;
                    i++;
                }

            }
        }

        return ret;

    }

    public static void generateNewTile(){
        int state = (new Random().nextInt(100) <= CHANCE_OF_LUCKY_SPAWN)
                ? LUCKY_INITIAL_TILE_STATE
                : INITIAL_TILE_STATE;

        int randQ, randR;

        randQ = new Random().nextInt(COUNT_TILES_Q);
        int curQ = randQ;

        randR = new Random().nextInt(COUNT_TILES_R);
        int curR = randR;

        boolean placed = false;
        while(!placed) {
            if(grid.getState(curQ, curR) == 0) {
                grid.setState(curQ, curR, state);
                placed = true;
            } else {
                if(curQ+1 < COUNT_TILES_Q) {
                    curQ++;
                } else {
                    curQ = 0;
                    if(curR+1 < COUNT_TILES_R) {
                        curR++;
                    } else {
                        curR = 0;
                    }
                }
                if((curQ == randQ) && (curR == randR)) {
                    ErrorCatcher.cellCreationFailure();
                }
            }
        }
    }

}
