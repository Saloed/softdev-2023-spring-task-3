import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Race.CarEnemy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarEnemyTest {
    private CarEnemy carEnemy;
    @Mock
    private SpriteBatch batch;

    @Before
    public void setUp() {
        batch = mock(SpriteBatch.class);
        carEnemy = new CarEnemy(batch);
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(CarEnemy.timeUpdatePositionLimit);
    }

    @Test
    public void updatePositionTest() {
        carEnemy.updatePosition();
        carEnemy.updatePosition();
        carEnemy.updatePosition();
        carEnemy.updatePosition();
        assertEquals(BrickGame.GRID_HEIGHT - 1, carEnemy.car[0].getY(),0.001);
    }

    @After
    public void dispose(){
        batch.dispose();
    }
}
