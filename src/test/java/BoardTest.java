import com.go.*;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testAddStone() {
        Board board = new Board();

        Stone stone = new Stone(Game.PlayerColor.BLACK, 0, 0);

        assertTrue(board.addStone(stone));
        assertEquals(stone, board.getPosition(0, 0));

        assertFalse(board.addStone(stone));
        assertEquals(stone, board.getPosition(0, 0));

        board.addStone(new Stone(Game.PlayerColor.WHITE, 1, 0));
        board.addStone(new Stone(Game.PlayerColor.WHITE, 0, 1));

        assertNull(board.getPosition(0, 0));

        stone = new Stone(Game.PlayerColor.BLACK, 0, 0);

        assertFalse(board.addStone(stone));
    }
}