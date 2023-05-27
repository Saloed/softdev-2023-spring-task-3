package org.ui;

import java.awt.*;

public class Bar {
    //Класс для панелей снизу

    protected int x, y, width, height;

    public Bar(int x, int y, int width, int height) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.width = width;

    }

    protected void drawButtonFeedback(Graphics g, MyButton b) {
        //Рисует границы на кнопке, чтобы показать, что она интерактивная
        if (b.isMouseOver()) {
            g.setColor(Color.white);
        } else {
            g.setColor(Color.black);
        }
        g.drawRect(b.x, b.y, b.width, b.height);
        if (b.isMousePressed()) {
            g.drawRect(b.x + 1, b.y + 1, b.width - 2, b.height - 2);
            g.drawRect(b.x + 2, b.y + 2, b.width - 4, b.height - 4);
        }
    }

}
