package GameObjects;

import java.awt.*;

import static GameEngine.Game.Scale;

public class Money extends GameObject {
    private float offset;
    private final int maxOffset;
    private int dir = 1;

    public Money(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox(16, 16);
        hitbox.y += (int) (10 * Scale);
        hitbox.x += (int) (9 * Scale);
        maxOffset = (int) (8 * Scale);
    }

    public void update() {
        updateAnimation();
        updatePosition();
    }

    private void updatePosition() {
        offset += (0.055f * Scale * dir);
        if (offset >= maxOffset) dir = -1;
        else if (offset < 0) dir = 1;

        hitbox.y = y + offset;
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(new Color(1, 0, 0, 255));
        g.drawRect((int) (hitbox.x - xLvlOffset), (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
}
