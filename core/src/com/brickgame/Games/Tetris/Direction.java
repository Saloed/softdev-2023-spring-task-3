package com.brickgame.Games.Tetris;

public enum Direction {

    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0),
    LEFT_UP(-1, -1),
    LEFT_DOWN(-1, 1),
    RIGHT_UP(1, -1),
    RIGHT_DOWN(1, 1);

    public final int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Direction next() {
        switch (this) {
            case UP:
                return RIGHT;
            case RIGHT:
                return DOWN;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            case RIGHT_UP:
                return RIGHT_DOWN;
            case RIGHT_DOWN:
                return LEFT_DOWN;
            case LEFT_DOWN:
                return LEFT_UP;
            case LEFT_UP:
                return RIGHT_UP;
            default:
                return null;
        }
    }

    public Direction previous() {
        switch (this) {
            case RIGHT:
                return UP;
            case DOWN:
                return RIGHT;
            case LEFT:
                return DOWN;
            case UP:
                return LEFT;
            case RIGHT_DOWN:
                return RIGHT_UP;
            case LEFT_DOWN:
                return RIGHT_DOWN;
            case LEFT_UP:
                return LEFT_DOWN;
            case RIGHT_UP:
                return LEFT_UP;
            default:
                return null;
        }
    }
}
