package org.objects;

public class PathPoint {
    //Класс для координаты появления врагов и координат базы
    private int xCord, yCord;
    public PathPoint(int xCord, int yCord) {
        this.xCord = xCord;
        this.yCord = yCord;
    }

    public int getxCord() {
        return xCord;
    }


    public int getyCord() {
        return yCord;
    }

}
