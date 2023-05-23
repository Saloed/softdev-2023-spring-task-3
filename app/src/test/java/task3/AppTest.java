package task3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    public void testGameBoard() {
        var gameBoard = new GameBoard();
        assertEquals(Geometry.numOfRows * Geometry.numOfCellsInRow, gameBoard.cells.size());

        var cell = gameBoard.cells.get(0);
        gameBoard.bombsArrangement(cell);
        assertTrue(cell.open(true));
        assertTrue(cell.isOpen);
        assertFalse(cell.hasBomb);
        assertFalse(cell.hasFlag);
        assertFalse(gameBoard.gameover);

        var bombs = gameBoard.cells.stream().filter(c -> c.hasBomb).count();
        assertEquals(Geometry.numOfBombs, bombs);
        var cellBomb = gameBoard.cells.stream().filter(c -> c.hasBomb).limit(1).findFirst();
        assertFalse(cellBomb.isEmpty() || cellBomb.get().open(true));
    }

}
