package task3;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Cell {
    final int row;
    final int col;
    final Point center;
    final Polygon polygon;

    boolean hasBomb;
    boolean hasFlag;
    boolean isOpen;
    long neighbourBombs;
    static final Color FLAG_COLOR = new Color(255, 193, 7);
    static final Color ONE_COLOR = new Color(63, 81, 181);
    static final Color TWO_COLOR = new Color(76, 175, 80);
    static final Color THREE_COLOR = new Color(244, 67, 54);
    static final Color FOUR_COLOR = new Color(103, 58, 183);
    static final Color FIVE_COLOR = new Color(3, 169, 244);
    static final Color SIX_COLOR = new Color(171, 71, 188);

    void updateNeighbourBombs() {
        neighbourBombs = neighbours.stream().filter(cell -> cell.hasBomb).count();
    }

    final List<Cell> neighbours = new ArrayList<>();

    Cell(int row, int col) {
        this.row = row;
        this.col = col;
        center = Geometry.cellCenter(row, col);
        polygon = Geometry.hexagon(center);
    }

    void paint(Graphics2D g, boolean gameover) {
        var d = Geometry.CELL_INNER_RADIUS / 2;
        var cellColor = isOpen || (gameover && hasBomb && !hasFlag) ? Color.LIGHT_GRAY : Color.DARK_GRAY;

        g.setColor(cellColor);
        g.fillPolygon(polygon);

        g.setColor(cellColor.darker());
        g.drawPolygon(polygon);

        if (hasFlag) {
            var flagPolygon = Geometry.flagPolygon(center);
            g.setColor(FLAG_COLOR);
            g.fillPolygon(flagPolygon);

            g.setColor(FLAG_COLOR.darker());
            g.drawPolygon(flagPolygon);

            if (gameover && !hasBomb) {
                g.setColor(Color.BLACK);
                g.drawLine(center.x - d, center.y + d,
                        center.x + d, center.y - d);
            }
        } else if (gameover && hasBomb) {
            g.setColor(Color.RED);
            g.fillOval(center.x - d, center.y - d, 2 * d, 2 * d);

            g.setColor(Color.RED.darker());
            g.drawOval(center.x - d, center.y - d, 2 * d, 2 * d);
        }

        if (isOpen && !hasBomb && neighbourBombs != 0) {
            g.setColor(numColor());
            g.drawString(String.valueOf(neighbourBombs), center.x - d / 2, center.y + d / 2);

        }
    }

    private Color numColor() {
        return switch ((int) neighbourBombs) {
            case 1 -> ONE_COLOR;
            case 2 -> TWO_COLOR;
            case 3 -> THREE_COLOR;
            case 4 -> FOUR_COLOR;
            case 5 -> FIVE_COLOR;
            case 6 -> SIX_COLOR;
            default -> Color.BLACK;
        };
    }

    boolean open(boolean user) {
        if (user) {
            if (hasFlag) return true;
            if (hasBomb) return false;
        }

        if (!isOpen && !hasBomb) {
            isOpen = true;
            if (neighbourBombs == 0) neighbours.forEach(cell -> cell.open(false));
        }
        return true;
    }

    void addNeighbour(Cell cell){
        if (cell != null) neighbours.add(cell);
    }
}
