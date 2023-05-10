package Sprites;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int w, h;
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    protected void createHitbox(float x, float y, float w, float h) {
        hitbox = new Rectangle2D.Float(x, y, w, h);
    }


    protected void drawHitbox(Graphics g) {
        g.setColor(new Color(0, 0, 0, 0));
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
