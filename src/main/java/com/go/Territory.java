package com.go;

import java.awt.*;

class Territory {
    private Color color;
    private int size;
    private final boolean surrounded;

    public Territory() {
        this.color = null;
        this.size = 0;
        this.surrounded = true;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void increaseSize() {
        this.size++;
    }

    public boolean isSurrounded() {
        return surrounded;
    }
}
