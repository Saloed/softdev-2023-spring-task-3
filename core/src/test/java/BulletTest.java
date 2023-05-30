

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Shoot.Bullet;
import com.brickgame.Games.Shoot.Gun;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BulletTest {

    @Mock
    private SpriteBatch batch;

    private Bullet bullet;
    private Gun gun;

    @Before
    public void setUp(){
        batch = mock(SpriteBatch.class);
        gun = new Gun(batch);
        bullet = new Bullet(batch,gun );
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(1/60f);
    }

    @Test
    public void updatePosition(){
        bullet.timeUpdatePosition = bullet.timeUpdatePositionLimit;
        bullet.updatePosition();
        assertEquals(5, bullet.bullet.getX(), 0.001);
        assertEquals(2, bullet.bullet.getY(), 0.001);
    }

    @After
    public void dispose(){
        batch.dispose();
    }
}
