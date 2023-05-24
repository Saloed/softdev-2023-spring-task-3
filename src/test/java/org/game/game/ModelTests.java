package org.game.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ModelTests {

    @Test
    public void test1() {
        Assertions.assertEquals(16, 4*4);
    }

    @Test
    public void shiftTest() {
        Constants.setSideLength(3);
        Constants.setArraySide( 2 * Constants.getSideLength() - 1);
        MainLogic logic = new MainLogic();
        logic.init();
        logic.getGrid().setState(0,4, 2);
        logic.getGrid().setState(4, 0, 2);
        logic.shift(Direction.UP_RIGHT);
        Assertions.assertArrayEquals(logic.getGrid().getArray(), new int[][]{
                {-1, -1, 0, 0, 4},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        logic.shift(Direction.RIGHT);
        Assertions.assertArrayEquals(logic.getGrid().getArray(), new int[][]{
                {-1, -1, 0, 0, 4},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        logic.shift(Direction.LEFT);
        Assertions.assertArrayEquals(logic.getGrid().getArray(), new int[][]{
                {-1, -1, 4, 0, 0},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        logic.shift(Direction.DOWN_LEFT);
        Assertions.assertArrayEquals(logic.getGrid().getArray(), new int[][]{
                {-1, -1, 0, 0, 0},
                {-1, 0, 0, 0, 0},
                {4, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
    }

    @Test
    public void winTest() {
        Constants.setSideLength(3);
        Constants.setArraySide(2 * Constants.getSideLength() - 1);
        MainLogic logic = new MainLogic();
        logic.init();
        logic.getGrid().setState(0,4, 8192);
        logic.getGrid().setState(4, 0, 8192);
        logic.shift(Direction.UP_RIGHT);
        Assertions.assertTrue(logic.isThere16384);
    }

    @Test
    public void failTest() {
        Constants.setSideLength(3);
        Constants.setArraySide(2 * Constants.getSideLength() - 1);
        MainLogic logic = new MainLogic();
        logic.init();
        logic.getGrid().setState(0, 2, 2);
        logic.getGrid().setState(0, 3, 4);
        logic.getGrid().setState(0, 4, 8);
        logic.getGrid().setState(1, 1, 16);
        logic.getGrid().setState(1, 2, 32);
        logic.getGrid().setState(1, 3, 64);
        logic.getGrid().setState(1, 4, 128);
        logic.getGrid().setState(2, 0, 256);
        logic.getGrid().setState(2, 1, 512);
        logic.getGrid().setState(2, 2, 1024);
        logic.getGrid().setState(2, 3, 2048);
        logic.getGrid().setState(2, 4, 4096);
        logic.getGrid().setState(3, 0, 8192);
        logic.getGrid().setState(3, 1, 16384);
        logic.getGrid().setState(3, 2, 2);
        logic.getGrid().setState(3, 3, 4);
        logic.getGrid().setState(4, 0, 8);
        logic.getGrid().setState(4, 1, 16);
        logic.getGrid().setState(4, 2, 32);
        Assertions.assertTrue(logic.isItEnd());
        logic.getGrid().setState(4, 2, 16);
        Assertions.assertFalse(logic.isItEnd());
    }

}
