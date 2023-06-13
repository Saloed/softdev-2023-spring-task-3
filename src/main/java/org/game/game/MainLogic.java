package org.game.game;

import org.game.view_control.Controller;

import java.util.Random;

public class MainLogic {

    private int score;
    protected boolean isThere16384;
    private Direction direction;
    private HexGrid grid;
    private HexGrid previous;
    private int preScore;
    private FieldOptions fieldOp;
    private Controller controller;

    public HexGrid getGrid(){
        return grid;
    }

    private void merged16384() {
        isThere16384 = true;
        if(controller != null) controller.win = true;
    }

    public boolean isItEnd() {
        HexGrid save = new HexGrid(fieldOp);
        grid.copyTo(save);
        if(shift(Direction.DOWN_RIGHT)) {
            grid = save;
            return false;
        } else if(shift(Direction.UP_RIGHT)) {
            grid = save;
            return false;
        } else if(shift(Direction.RIGHT)) {
            grid = save;
            return false;
        } else if(shift(Direction.UP_LEFT)) {
            grid = save;
            return false;
        } else if(shift(Direction.LEFT)) {
            grid = save;
            return false;
        } else if(shift(Direction.DOWN_LEFT)) {
            grid = save;
            return false;
        }
        return true;
    }

    public void init(FieldOptions fieldOptions, Controller controller){
        fieldOp = fieldOptions;
        score = 0;
        isThere16384 = false;
        direction = Direction.AWAITING;
        grid = new HexGrid(fieldOp);
        previous = new HexGrid(fieldOp);
        this.controller = controller;
    }
    public void init(FieldOptions fieldOptions){
        fieldOp = fieldOptions;
        score = 0;
        isThere16384 = false;
        direction = Direction.AWAITING;
        grid = new HexGrid(fieldOp);
        previous = new HexGrid(fieldOp);
    }

    public void returnPrevious() {
        previous.copyTo(grid);
        score = preScore;
    }

    public boolean move() {
        boolean wasMoved = false;
        if(direction != Direction.AWAITING) {
            HexGrid t = new HexGrid(fieldOp);
            grid.copyTo(t);
            int tS = score;
            if(shift(direction)) {
                generateNewTile();
                wasMoved = true;
                t.copyTo(previous);
                preScore = tS;
            }
            direction = Direction.AWAITING;
        }
        return wasMoved;
    }

    public void input(String key){
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

    private int[] reverse(int[] toReverse) {
        int[] ret = new int[toReverse.length];
        for (int i = 0; i < toReverse.length; i++) {
            ret[i] = toReverse[toReverse.length-i-1];
        }
        return ret;
    }

    protected boolean shift(Direction direction) {
        boolean ret = false;
        switch (direction) {
            case UP_RIGHT:
            case DOWN_LEFT:
                for(int q = fieldOp.getArraySide() - 1; q > 1; q--) {
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
                for(int q = 1; q < fieldOp.getArraySide(); q++) {
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
                for (int q = 0; q < fieldOp.getArraySide(); q++) {
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
                for (int q = 0; q < fieldOp.getArraySide(); q++) {
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

    private ShiftRowResult shiftRow(int[] oldRow){
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
                        if(ret.shiftedRow[j] == 16384 && !isThere16384) merged16384();
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

    public void generateNewTile(){
        int state = (new Random().nextInt(100) <= Constants.CHANCE_OF_LUCKY_SPAWN)
                ? Constants.LUCKY_INITIAL_TILE_STATE
                : Constants.INITIAL_TILE_STATE;

        int randQ, randR;

        randQ = new Random().nextInt(fieldOp.getArraySide());
        int curQ = randQ;

        randR = new Random().nextInt(fieldOp.getArraySide());
        int curR = randR;

        boolean placed = false;
        while(!placed) {
            if(grid.getState(curQ, curR) == 0) {
                grid.setState(curQ, curR, state);
                if(controller != null) controller.setAnim(curQ, curR, 1);
                placed = true;
            } else {
                if(curQ+1 < fieldOp.getArraySide()) {
                    curQ++;
                } else {
                    curQ = 0;
                    if(curR+1 < fieldOp.getArraySide()) {
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

    public int getScore() {
        return score;
    }

}
