import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Arcanoid.Ball;
import com.brickgame.Games.Arcanoid.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BallTest {
    private Ball ball;
    private Platform platform;

    @Mock
    private SpriteBatch batch;

    @Before
    public void setUp(){
        batch = mock(SpriteBatch.class);
        platform = new Platform(batch, BrickGame.GRID_WIDTH / 2, 0, 3);
        ball = new Ball(batch, platform.platform[platform.platform.length / 2].getX(), 1, 1, 1);
        Gdx.graphics = mock(Graphics.class);
        Gdx.input = mock(Input.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(ball.timeUpdatePositionLimit);
    }

    @Test
    public void updatePositionTest(){
        ball.updatePosition(platform);
        assertEquals(2, ball.ball.getY(), 0.001);
        assertEquals(platform.platform[platform.platform.length / 2].getX() +1, ball.ball.getX(), 0.001);
        ball.dy = -ball.dy;
        ball.dx = -ball.dx;
        ball.updatePosition(platform);
        ball.updatePosition(platform);
        assertEquals(1, ball.dy, 0.001);
        assertEquals(-1, ball.dx, 0.001);
    }

    @After
    public void dispose(){
        batch.dispose();
    }
}
