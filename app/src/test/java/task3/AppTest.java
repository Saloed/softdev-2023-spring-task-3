package task3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    public void testGameBoard() {
        var geometry = new Geometry(10, 16, 32);
        var gameBoard = new GameBoard(geometry);
        assertEquals(geometry.numOfRows * geometry.numOfCellsInRow, gameBoard.cells.size());

        var cell = gameBoard.cells.get(0);
        gameBoard.bombsArrangement(cell);
        assertTrue(cell.open(true));
        assertTrue(cell.isOpen);
        assertFalse(cell.hasBomb);
        assertFalse(cell.hasFlag);
        assertFalse(gameBoard.gameover);

        var bombs = gameBoard.cells.stream().filter(c -> c.hasBomb).count();
        assertEquals(geometry.numOfBombs, bombs);
        var cellBomb = gameBoard.cells.stream().filter(c -> c.hasBomb).limit(1).findFirst();
        assertFalse(cellBomb.isEmpty() || cellBomb.get().open(true));
    }

}
