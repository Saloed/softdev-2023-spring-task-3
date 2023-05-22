import object.MainCharacter;
import org.junit.jupiter.api.Test;

import static UI.Screen.GRAVITY;
import static org.junit.jupiter.api.Assertions.*;

public class MainCharacterTest {

    @Test
    public void testMainCharacterUpdate() {
        MainCharacter mainCharacter = new MainCharacter();

        float initialSpeedY = mainCharacter.getSpeedY();
        float initialSpeedX = mainCharacter.getSpeedX();

        mainCharacter.update();

        float expectedSpeedY = initialSpeedY + GRAVITY;
        assertEquals(expectedSpeedY, mainCharacter.getSpeedY());

        float expectedSpeedX = initialSpeedX + 0.001f;
        assertEquals(expectedSpeedX, mainCharacter.getSpeedX());
    }

    @Test
    public void testMainCharacterDown() {
        MainCharacter mainCharacter = new MainCharacter();

        assertEquals(MainCharacter.RUNNING, mainCharacter.getState());

        mainCharacter.down(true);

        assertEquals(MainCharacter.DOWN, mainCharacter.getState());

        mainCharacter.down(false);

        assertEquals(MainCharacter.RUNNING, mainCharacter.getState());
    }

    @Test
    public void testMainCharacterSetAlive() {
        MainCharacter mainCharacter = new MainCharacter();

        assertTrue(mainCharacter.getAlive());
        assertEquals(MainCharacter.RUNNING, mainCharacter.getState());

        mainCharacter.setAlive(false);

        assertFalse(mainCharacter.getAlive());
        assertEquals(MainCharacter.DEATH, mainCharacter.getState());

        mainCharacter.setAlive(true);

        assertTrue(mainCharacter.getAlive());
        assertEquals(MainCharacter.RUNNING, mainCharacter.getState());
    }

}
