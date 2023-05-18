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
        Constants.SIDE_LENGTH = 3;
        Constants.ARRAY_SIDE = 2 * Constants.SIDE_LENGTH - 1;
        MainLogic.initForTests();
        MainLogic.getGrid().setState(0,4, 2);
        MainLogic.getGrid().setState(4, 0, 2);
        MainLogic.shift(Direction.UP_RIGHT);
        Assertions.assertArrayEquals(MainLogic.getGrid().getArray(), new int[][]{
                {-1, -1, 0, 0, 4},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        MainLogic.shift(Direction.RIGHT);
        Assertions.assertArrayEquals(MainLogic.getGrid().getArray(), new int[][]{
                {-1, -1, 0, 0, 4},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        MainLogic.shift(Direction.LEFT);
        Assertions.assertArrayEquals(MainLogic.getGrid().getArray(), new int[][]{
                {-1, -1, 4, 0, 0},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        MainLogic.shift(Direction.DOWN_LEFT);
        Assertions.assertArrayEquals(MainLogic.getGrid().getArray(), new int[][]{
                {-1, -1, 0, 0, 0},
                {-1, 0, 0, 0, 0},
                {4, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
    }

    @Test
    public void winTest() {
        Constants.SIDE_LENGTH = 3;
        Constants.ARRAY_SIDE = 2 * Constants.SIDE_LENGTH - 1;
        MainLogic.initForTests();
        MainLogic.getGrid().setState(0,4, 8192);
        MainLogic.getGrid().setState(4, 0, 8192);
        MainLogic.shift(Direction.UP_RIGHT);
        Assertions.assertTrue(MainLogic.isThere16384);
    }

    @Test
    public void failTest() {
        Constants.SIDE_LENGTH = 3;
        Constants.ARRAY_SIDE = 2 * Constants.SIDE_LENGTH - 1;
        MainLogic.initForTests();
        MainLogic.getGrid().setState(0, 2, 2);
        MainLogic.getGrid().setState(0, 3, 4);
        MainLogic.getGrid().setState(0, 4, 8);
        MainLogic.getGrid().setState(1, 1, 16);
        MainLogic.getGrid().setState(1, 2, 32);
        MainLogic.getGrid().setState(1, 3, 64);
        MainLogic.getGrid().setState(1, 4, 128);
        MainLogic.getGrid().setState(2, 0, 256);
        MainLogic.getGrid().setState(2, 1, 512);
        MainLogic.getGrid().setState(2, 2, 1024);
        MainLogic.getGrid().setState(2, 3, 2048);
        MainLogic.getGrid().setState(2, 4, 4096);
        MainLogic.getGrid().setState(3, 0, 8192);
        MainLogic.getGrid().setState(3, 1, 16384);
        MainLogic.getGrid().setState(3, 2, 2);
        MainLogic.getGrid().setState(3, 3, 4);
        MainLogic.getGrid().setState(4, 0, 8);
        MainLogic.getGrid().setState(4, 1, 16);
        MainLogic.getGrid().setState(4, 2, 32);
        Assertions.assertTrue(MainLogic.isItEnd());
        MainLogic.getGrid().setState(4, 2, 16);
        Assertions.assertFalse(MainLogic.isItEnd());
    }

}
