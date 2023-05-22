import object.Cactus;
import object.MainCharacter;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CactusTest {

    @Test
    public void testUpdate() {
        MainCharacter mainCharacter = new MainCharacter();
        Cactus cactus = new Cactus(mainCharacter);

        cactus.update();

        assertEquals(200 - mainCharacter.getSpeedX(), cactus.getPosX());
    }

    @Test
    public void testGetBound() {
        MainCharacter mainCharacter = new MainCharacter();
        Cactus cactus = new Cactus(mainCharacter);

        cactus.update();

        Rectangle bound = cactus.getBound();

        assertEquals(cactus.getPosX(), bound.x);
        assertEquals(cactus.getPosY(), bound.y);
        assertEquals(cactus.getWidth(), bound.width);
        assertEquals(cactus.image.getHeight(), bound.height);
    }

    @Test
    public void testSetPosX() {
        MainCharacter mainCharacter = new MainCharacter();
        Cactus cactus = new Cactus(mainCharacter);
        int newX = 150;

        cactus.setPosX(newX);

        assertEquals(newX, cactus.getPosX());
    }

    @Test
    public void testSetPosY() {
        MainCharacter mainCharacter = new MainCharacter();
        Cactus cactus = new Cactus(mainCharacter);
        int newY = 90;

        cactus.setPosY(newY);

        assertEquals(newY, cactus.getPosY());
    }

    @Test
    public void testSetImage() {
        MainCharacter mainCharacter = new MainCharacter();
        Cactus cactus = new Cactus(mainCharacter);
        BufferedImage newImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);

        cactus.setImage(newImage);

        assertEquals(newImage, cactus.image);
    }
}
