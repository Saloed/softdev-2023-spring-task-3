import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.gdx.Start;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Test1 {

    @Test
    public void testCreateInstance() {
        Start start = new Start();
        assertNotNull(start);
    }
}