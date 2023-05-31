package Sprites;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static GameEngine.Game.Scale;

public abstract class Entity {
    protected float x, y;
    protected int w, h;
    protected float speed;
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackHitbox;

    protected int animationIndex, animationTick, animationSpeed;
    protected int action;

    protected float airSpeed;
    protected boolean inAir = false;

    protected int maxHealth;
    protected int currentHealth;

    protected boolean attackChecked;

    public Entity(float x, float y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    protected void createHitbox(float w, float h) {
        hitbox = new Rectangle2D.Float(x, y, (int) (w * Scale), (int) (h * Scale));
    }

    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(new Color(1, 0, 0, 255));
        g.drawRect((int) (hitbox.x - xLvlOffset), (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void drawAttackHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.BLACK);
        g.drawRect((int) attackHitbox.x - xLvlOffset, (int) attackHitbox.y, (int) attackHitbox.width, (int) attackHitbox.height);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getAction() {
        return action;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
}
