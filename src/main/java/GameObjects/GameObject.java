package GameObjects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static GameEngine.Game.Scale;
import static HelperClasses.Constants.Objects.AmountOfFrames;

public class GameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean active = true;
    protected int animationTick, animationIndex;
    protected int offsetX, offsetY;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void createHitbox(int w, int h) {
        hitbox = new Rectangle2D.Float(x, y, (int) (w * Scale), (int) (h * Scale));
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(new Color(1, 0, 0, 255));
        g.drawRect((int) (hitbox.x - xLvlOffset), (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void resetAll() {
        animationTick = 0;
        animationIndex = 0;
        active = true;
    }

    protected void updateAnimation() {
        animationTick++;
        int speed = 25;
        if (animationTick >= speed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= AmountOfFrames(objectType)) animationIndex = 0;
        }
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
