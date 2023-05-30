import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Piece;
import com.brickgame.Games.Tetris.Board;
import com.brickgame.Games.Tetris.Direction;
import com.brickgame.Games.Tetris.Tetromino;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TetrominoTest {

    private Tetromino tetromino;
    @Mock
    private SpriteBatch batch;

    @Before
    public void setUp() {
        batch = mock(SpriteBatch.class);
        Board board = new Board(batch);
        Piece centerTetromino = new Piece(5, 18);
        tetromino = new Tetromino(batch, centerTetromino, 5, board);
        Gdx.graphics = mock(Graphics.class);
        Gdx.input = mock(Input.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(0.6f);
    }

    @Test
    public void rotateTest() {
        tetromino.rotate();
        assertNull(tetromino.tetromino.get(0).directions);
        assertEquals(Direction.LEFT, tetromino.tetromino.get(1).directions.get(0));
        assertEquals(Direction.LEFT_UP, tetromino.tetromino.get(2).directions.get(0));
        assertEquals(Direction.DOWN, tetromino.tetromino.get(3).directions.get(0));
    }

    @Test
    public void moveRightTest() {
        tetromino.moveRight();
        assertEquals(6, tetromino.centerTetromino.getX(), 0.001);
    }

    @Test
    public void moveLeftTest() {
        tetromino.moveLeft();
        assertEquals(4, tetromino.centerTetromino.getX(), 0.001);
    }

    @Test
    public void moveDownTest() {
        tetromino.moveDown();
        assertEquals(17, tetromino.centerTetromino.getY(), 0.001);
    }

    @After
    public void dispose() {
        batch.dispose();
    }
}
