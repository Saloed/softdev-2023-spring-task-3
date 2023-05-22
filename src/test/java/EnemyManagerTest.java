import object.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyManagerTest {

    @Test
    public void testUpdate() {
        MainCharacter mainCharacter = new MainCharacter();
        EnemiesManager enemiesManager = new EnemiesManager(mainCharacter);

        mainCharacter.setSpeedX(8);
        enemiesManager.distanceBetweenEnemies = 600;

        enemiesManager.update();

        assertEquals(599.9, enemiesManager.distanceBetweenEnemies);

        mainCharacter.setSpeedX(9);
        enemiesManager.distanceBetweenEnemies = 600;

        enemiesManager.update();

        assertEquals(400, enemiesManager.distanceBetweenEnemies);
    }
}
