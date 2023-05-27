import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Shoot.Enemy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnemyTest {

    @Mock
    private SpriteBatch batch;
    private int level;
    private Enemy enemy;

    @Before
    public void setUp() {
        batch = mock(SpriteBatch.class);
        level = 1;
        enemy = new Enemy(batch, level);
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(1/60f);
    }


    @Test
    public void testUpdatePosition() {
        enemy.timeUpdatePosition = enemy.timeUpdatePositionLimit;
        enemy.updatePosition();
        assertEquals(BrickGame.GRID_HEIGHT + 1, enemy.enemy[0].getY(), 0.001);
        enemy.updatePosition();
        assertEquals(BrickGame.GRID_HEIGHT + 1, enemy.enemy[0].getY(), 0.001);
    }

    @After
    public void dispose(){
        batch.dispose();
    }
}
