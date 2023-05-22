package object;

import Util.Resource;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cactus extends Enemy{

    public BufferedImage image;
    private int posX, posY;
    private final Rectangle rectangle;
    private final MainCharacter mainCharacter;

    public Cactus(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
        image = Resource.getImage("files/cactus1.png");
        posX = 200;
        posY = 70;
        rectangle = new Rectangle();
    }

    public void update() {
        posX -= mainCharacter.getSpeedX();
        rectangle.x = posX;
        rectangle.y = posY;
        rectangle.width = image.getWidth();
        rectangle.height = image.getHeight();
    }

    @Override
    public  Rectangle getBound() {
        return rectangle;
    }

    @Override
    public void draw(@NotNull Graphics g) {
        g.drawImage(image, posX, posY, null);
    }

    @Override
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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
        return  image.getWidth();
    }
}
