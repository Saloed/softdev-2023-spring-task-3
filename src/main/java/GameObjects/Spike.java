package GameObjects;

import static GameEngine.Game.Scale;

public class Spike extends GameObject{

    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox(32, 14);
        offsetX = 0;
        offsetY = (int) (18 * Scale);
        hitbox.y += offsetY;
    }
}
