package task3;

import java.awt.*;

class Geometry {
    public static int numOfRows = 10;
    public static int numOfCellsInRow = 16;
    public static int numOfBombs = numOfRows * numOfCellsInRow / 5;

    public static final int CELL_OUTER_RADIUS = 20;
    public static final int CELL_INNER_RADIUS = (int) Math.round(CELL_OUTER_RADIUS * Math.sqrt(3) / 2);

    public static int boardWidth() {
        return (3 * numOfCellsInRow + 1) * CELL_OUTER_RADIUS / 2;
    }
    public static int boardHeight() {
        return (2 * numOfRows + 1) * CELL_INNER_RADIUS;
    }

    public static Dimension boardDimension() {
        return new Dimension(boardWidth(), boardHeight());
    }

    public static Polygon hexagon(int x, int y) {
        var dx = CELL_OUTER_RADIUS / 2;
        var dy = CELL_INNER_RADIUS;

        var p = new Polygon();
        p.addPoint(x - 2 * dx, y);
        p.addPoint(x - dx, y - dy);
        p.addPoint(x + dx, y - dy);
        p.addPoint(x + 2 * dx, y);
        p.addPoint(x + dx, y + dy);
        p.addPoint(x - dx, y + dy);
        return p;
    }

    public static Polygon flagPolygon(Point center) {
        var d = CELL_INNER_RADIUS / 2;

        var p = new Polygon();
        p.addPoint(center.x - d, center.y + d);
        p.addPoint(center.x, center.y - d);
        p.addPoint(center.x + d, center.y + d);
        return p;
    }

    public static Polygon hexagon(Point center) {
        return hexagon(center.x, center.y);
    }

    public static Point cellCenter(int row, int col) {
        int h = (col % 2) == 0 ? 1 : 2;
        int x = (3 * col + 2) * CELL_OUTER_RADIUS / 2;
        int y = (2 * row + h) * CELL_INNER_RADIUS;
        return new Point(x, y);
    }
}
