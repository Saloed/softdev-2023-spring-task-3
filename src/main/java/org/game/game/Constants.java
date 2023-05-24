package org.game.game;

public class Constants {
    private static int SIDE_LENGTH;
    private static int ARRAY_SIDE;
    private static double DIAMETER;
    public static final int CHANCE_OF_LUCKY_SPAWN = 10;
    public static final int LUCKY_INITIAL_TILE_STATE = 4;
    public static final int INITIAL_TILE_STATE = 2;
    public static final int COUNT_INITIAL_TILES = 2;

    public static int getSideLength() {
        return SIDE_LENGTH;
    }

    public static void setSideLength(int len) {
        SIDE_LENGTH = len;
    }

    public static int getArraySide() {
        return ARRAY_SIDE;
    }

    public static void setArraySide(int side) {
        ARRAY_SIDE = side;
    }

    public static double getDiameter() {
        return DIAMETER;
    }

    public static void setDiameter(double diameter) {
        DIAMETER = diameter;
    }

}
