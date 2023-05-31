import UI.Screen;
import object.Land;
import object.MainCharacter;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LandTest {

    @Test
    public void testLandInitialization() {
        Screen game = new Screen();
        MainCharacter mainCharacter = new MainCharacter();
        Land land = new Land(mainCharacter);

        assertFalse(land.listImage.isEmpty());

        int expectedNumberOfElements = 600 / land.imageLand1.getWidth() + 2;
        assertEquals(expectedNumberOfElements, land.listImage.size());

        for (int i = 0; i < land.listImage.size(); i++) {
            Land.ImageLand imageLand = land.listImage.get(i);
            int expectedPosX = i * land.imageLand1.getWidth();
            assertEquals(expectedPosX, imageLand.posX);
        }
    }

    @Test
    public void testLandUpdate() {
        Screen game = new Screen();
        MainCharacter mainCharacter = new MainCharacter();
        Land land = new Land(mainCharacter);

        int initialPosX = land.listImage.get(0).posX;
        int speedX = 5;

        land.mainCharacter.setSpeedX(speedX);
        land.update();

        for (Land.ImageLand imageLand : land.listImage) {
            int expectedPosX = initialPosX - speedX;
            assertEquals(expectedPosX, imageLand.posX);
            initialPosX += land.imageLand1.getWidth();
        }
    }
}
