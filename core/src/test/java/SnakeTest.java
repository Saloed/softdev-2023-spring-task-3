import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Snake.Apple;
import com.brickgame.Games.Snake.Snake;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SnakeTest {
    private Snake snake;
    private Apple apple;

    @Mock
    private SpriteBatch batch;

    @Before
    public void setUp() {
        batch = mock(SpriteBatch.class);
        snake = new Snake(batch);
        Gdx.graphics = mock(Graphics.class);
        Gdx.input = mock(Input.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(snake.timeUpdatePositionLimit);
        apple = new Apple(batch, snake);
        while(snake.snake.get(0).getX()!= apple.apple.getX()) snake.updatePosition(apple);
        snake.direction = 0;
        while(snake.snake.get(0).getY()!= apple.apple.getY()) snake.updatePosition(apple);
    }


    @Test
    public void updatePositionTest(){
        assertEquals(4, snake.snake.size());
    }

    @Test
    public void checkSelfCollisionTest(){
        snake.direction = 1;
        while(snake.snake.get(0).getX()!= apple.apple.getX()) snake.updatePosition(apple);
        snake.direction = 0;
        while(snake.snake.get(0).getY()!= apple.apple.getY()) snake.updatePosition(apple);
        snake.direction = 3;
        snake.updatePosition(apple);
        snake.direction = 2;
        snake.updatePosition(apple);
        assertFalse(snake.checkSelfCollision());
        snake.direction = 1;
        snake.updatePosition(apple);
        assertTrue(snake.checkSelfCollision());
    }

    @After
    public void dispose(){
        batch.dispose();
    }
}
