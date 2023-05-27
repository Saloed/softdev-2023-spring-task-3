import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Shoot.Gun;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GunTest {
    private Gun gun;
    @Mock
    private SpriteBatch batch;

    @Before
    public void setUp() {
        batch = mock(SpriteBatch.class);
        gun = new Gun(batch);
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(gun.timeUpdatePositionLimit);
    }

    @Test
    public void testShoot() {
        gun.shoot();
        assertEquals(1, gun.bullets.size());
    }

    @Test
    public void testMoveLeft() {
        gun.timeUpdatePositions = gun.timeUpdatePositionLimit;
        gun.moveLeft();
        assertEquals(3, gun.gun.get(0).getX(), 0.001);
        assertEquals(4, gun.gun.get(1).getX(), 0.001);
        assertEquals(5, gun.gun.get(2).getX(), 0.001);
        assertEquals(0, gun.timeUpdatePositions, 0.001);
    }

    @Test
    public void testMoveRight() {
        gun.timeUpdatePositions = gun.timeUpdatePositionLimit;
        gun.moveRight();
        assertEquals(5, gun.gun.get(0).getX(), 0.001);
        assertEquals(6, gun.gun.get(1).getX(), 0.001);
        assertEquals(7, gun.gun.get(2).getX(), 0.001);
        assertEquals(0, gun.timeUpdatePositions, 0.001);
    }

    @After
    public void dispose(){
        batch.dispose();
    }
}