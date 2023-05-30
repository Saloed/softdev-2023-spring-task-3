import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Arcanoid.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlatformTest {

    private Platform platform;

    @Mock
    private SpriteBatch batch;

    @Before
    public void setUp(){
        batch = mock(SpriteBatch.class);
        platform = new Platform(batch, BrickGame.GRID_WIDTH / 2, 0, 3);
        Gdx.graphics = mock(Graphics.class);
        Gdx.input = mock(Input.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(0.1f);
    }

    @Test
    public void moveRightTest(){
        platform.moveRight();
        assertEquals(6, platform.platform[0].getX(), 0.001);
    }

    @Test
    public void moveLeftTest(){
        platform.moveLeft();
        assertEquals(4, platform.platform[0].getX(), 0.001);
    }

    @After
    public void dispose(){
        batch.dispose();
    }
}
