import object.Bird;
import object.MainCharacter;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class BirdTest {

    @Test
    public void testUpdate() {
        MainCharacter mainCharacter = new MainCharacter();
        Bird bird = new Bird(mainCharacter);

        bird.update();

        assertEquals(200 - mainCharacter.getSpeedX(), bird.getPosX());
    }


    @Test
    public void testSetPosX() {
        MainCharacter mainCharacter = new MainCharacter();
        Bird bird = new Bird(mainCharacter);
        int newX = 150;

        bird.setPosX(newX);

        assertEquals(newX, bird.getPosX());
    }

    @Test
    public void testSetPosY() {
        MainCharacter mainCharacter = new MainCharacter();
        Bird bird = new Bird(mainCharacter);
        int newY = 90;

        bird.setPosY(newY);

        assertEquals(newY, bird.getPosY());
    }
}






