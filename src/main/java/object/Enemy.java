package object;

import java.awt.*;

public abstract class Enemy {
    public abstract Rectangle getBound();
    public abstract void draw(Graphics g);
    public abstract void update();

    public abstract void setPosX(int posX);

    public abstract int  getPosX();
    public abstract int getWidth();
}
