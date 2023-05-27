import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Race.CarEnemy;
import com.brickgame.Games.Race.CarPlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarPlayerTest {
    private CarPlayer carPlayer;
    @Mock
    private SpriteBatch batch;


    @Before
    public void setUp() {
        batch = mock(SpriteBatch.class);
        carPlayer = new CarPlayer(batch);
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(CarEnemy.timeUpdatePositionLimit);
    }

    @Test
    public void moveTest() {
        carPlayer.moveRight();
        assertEquals(6, carPlayer.car[0].getX(), 0.001);
        carPlayer.moveLeft();
        assertEquals(3, carPlayer.car[0].getX(), 0.001);
    }

    @Test
    public void checkCrashTest() {
        CarEnemy carEnemy1 = new CarEnemy(batch);
        if (carEnemy1.car[0].getX() == 3) {
            carPlayer.moveLeft();
        } else carPlayer.moveRight();
        while (carEnemy1.car[0].getY() > 3) carEnemy1.updatePosition();
        ArrayList<CarEnemy> array1 = new ArrayList<>();
        array1.add(carEnemy1);
        assertTrue(carPlayer.checkCrash(array1));
    }

    @Test
    public void deleteCarEnemyTest() {
        CarEnemy carEnemy1 = new CarEnemy(batch);
        if (carEnemy1.car[0].getX() == 3) {
            carPlayer.moveRight();
        } else carPlayer.moveLeft();
        while (carEnemy1.car[0].getY() > -1) carEnemy1.updatePosition();
        ArrayList<CarEnemy> array1 = new ArrayList<>();
        array1.add(carEnemy1);
        carPlayer.deleteCarEnemy(array1);
        assertEquals(0, array1.size());
    }

    @After
    public void dispose(){
        batch.dispose();
    }
}
