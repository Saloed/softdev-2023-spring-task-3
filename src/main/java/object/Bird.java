package object;

import Util.Animation;
import Util.Resource;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Bird extends Enemy{
    private final Animation flyingBird;
    private final Rectangle rectangle;
    private int posX, posY;
    private final MainCharacter mainCharacter;

    public Bird(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
        flyingBird = new Animation(180);
        flyingBird.addFrame(Resource.getImage("files/bird-fly-1.png"));
        flyingBird.addFrame(Resource.getImage("files/bird-fly-2.png"));
        posX = 200;
        posY = 70;
        rectangle = new Rectangle();
    }

    public Animation getFlyingBird() {
        return flyingBird;
    }

    @Override
    public Rectangle getBound() {
        return rectangle;
    }

    @Override
    public void draw(@NotNull Graphics g) {
        g.drawImage(flyingBird.getFrame(), posX, posY, null);
    }

    @Override
    public void update() {
        flyingBird.update();
        posX -= mainCharacter.getSpeedX();
        if (flyingBird.getFrame().getHeight() == 51) {
            rectangle.x = posX + 12;
            rectangle.y = posY + 15;
            rectangle.width = flyingBird.getFrame().getWidth() - 25;
            rectangle.height = flyingBird.getFrame().getHeight() - 25;
        } else {
            rectangle.x = posX + 12;
            rectangle.y = posY + 15;
            rectangle.width = flyingBird.getFrame().getWidth() - 25;
            rectangle.height = flyingBird.getFrame().getHeight() - 13;
        }
    }

    @Override
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    @Override
    public int getWidth() {
        return flyingBird.getFrame().getWidth();
    }

    @Override
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
