package org.game.game;

import com.sun.tools.javac.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ModelTests {

    @Test
    public void test1() {
        Assertions.assertEquals(16, 4*4);
    }

    @Test
    public void shiftTest() {
        MainLogic.initForTests();
        MainLogic.grid.setState(0,4, 2);
        MainLogic.grid.setState(4, 0, 2);
        MainLogic.shift(Direction.UP_RIGHT);
        Assertions.assertArrayEquals(MainLogic.grid.getArray(), new int[][]{
                {-1, -1, 0, 0, 4},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        MainLogic.shift(Direction.RIGHT);
        Assertions.assertArrayEquals(MainLogic.grid.getArray(), new int[][]{
                {-1, -1, 0, 0, 4},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        MainLogic.shift(Direction.LEFT);
        Assertions.assertArrayEquals(MainLogic.grid.getArray(), new int[][]{
                {-1, -1, 4, 0, 0},
                {-1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
        MainLogic.shift(Direction.DOWN_LEFT);
        Assertions.assertArrayEquals(MainLogic.grid.getArray(), new int[][]{
                {-1, -1, 0, 0, 0},
                {-1, 0, 0, 0, 0},
                {4, 0, 0, 0, 0},
                {0, 0, 0, 0, -1},
                {0, 0, 0, -1, -1}
        });
    }

    @Test
    public void winTest() {
        MainLogic.initForTests();
        MainLogic.grid.setState(0,4, 8192);
        MainLogic.grid.setState(4, 0, 8192);
        MainLogic.shift(Direction.UP_RIGHT);
        Assertions.assertTrue(MainLogic.isThere16384);
    }

    @Test
    public void failTest() {
        MainLogic.initForTests();
        MainLogic.grid.setState(0, 2, 2);
        MainLogic.grid.setState(0, 3, 4);
        MainLogic.grid.setState(0, 4, 8);
        MainLogic.grid.setState(1, 1, 16);
        MainLogic.grid.setState(1, 2, 32);
        MainLogic.grid.setState(1, 3, 64);
        MainLogic.grid.setState(1, 4, 128);
        MainLogic.grid.setState(2, 0, 256);
        MainLogic.grid.setState(2, 1, 512);
        MainLogic.grid.setState(2, 2, 1024);
        MainLogic.grid.setState(2, 3, 2048);
        MainLogic.grid.setState(2, 4, 4096);
        MainLogic.grid.setState(3, 0, 8192);
        MainLogic.grid.setState(3, 1, 16384);
        MainLogic.grid.setState(3, 2, 2);
        MainLogic.grid.setState(3, 3, 4);
        MainLogic.grid.setState(4, 0, 8);
        MainLogic.grid.setState(4, 1, 16);
        MainLogic.grid.setState(4, 2, 32);
        Assertions.assertTrue(MainLogic.isItEnd());
        MainLogic.grid.setState(4, 2, 16);
        Assertions.assertFalse(MainLogic.isItEnd());
    }

}
