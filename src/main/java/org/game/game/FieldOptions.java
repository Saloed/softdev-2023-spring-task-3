package org.game.game;

public class FieldOptions {

    private int sideLength;
    private int arraySide;
    private double diameter;

    public void setSideLength(int len) {
        sideLength = len;
    }

    public int getSideLength() {
        return sideLength;
    }

    public int getArraySide() {
        return arraySide;
    }

    public void setArraySide(int side) {
        arraySide = side;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

}
