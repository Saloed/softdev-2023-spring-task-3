package task3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class GameBoard extends JPanel {

    final List<Cell> cells = new ArrayList<>();

    boolean gameover;
    boolean bombsArranged;
    final Geometry geometry;

    public GameBoard(Geometry geometry) {
        this.geometry = geometry;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setPreferredSize(geometry.boardDimension());

        for (int row = 0; row < geometry.numOfRows; row++)
            for (int col = 0; col < geometry.numOfCellsInRow; col++)
                cells.add(new Cell(row, col));

        assingNeighbours();

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cells.stream()
                    .filter(cell -> cell.polygon.contains(e.getX(), e.getY()))
                    .limit(1).forEach(cell -> onCellClicked(cell, e.getButton()));
            }
        });

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintCells((Graphics2D) g);
        g.setColor(Color.black);
    }

    private void paintCells(Graphics2D g) {
        g.setFont(new Font("Segoe UI", Font.BOLD, Geometry.CELL_INNER_RADIUS));
        g.setStroke(new BasicStroke(3));  // line width
        cells.forEach(cell -> cell.paint(g, gameover));
    }

    private void onCellClicked(Cell cell, int button) {
        if (gameover) return;
        switch (button) {
            case 1 -> {
                bombsArrangement(cell);
                if (cell.open(true)) {
                    if (cells.stream().anyMatch(c -> (!c.hasBomb && !c.isOpen))) break;
                    cells.forEach(c -> c.hasFlag = c.hasBomb);
                    gameover = true;
                    repaint();
                    JOptionPane.showMessageDialog(null, "You won!");
                    return;
                }
                gameover = true;
            }
            case 3 -> {
                if (!cell.isOpen) cell.hasFlag = !cell.hasFlag;
            }
        }
        repaint();
    }

    void bombsArrangement(Cell exclude) {
        if (bombsArranged) return;

        bombsArranged = true;
        var random = new Random();
        var i = 0;
        while (i < geometry.numOfBombs) {
            var cell = cells.get(random.nextInt(cells.size()));
            if (cell != exclude && !cell.hasBomb) {
                cell.hasBomb = true;
                i++;
            }
        }
        updateNeighbourBombs();
    }

    Cell getCell(int row, int col) {
        if (row < 0 || col < 0 || col >= geometry.numOfCellsInRow) return null;
        int idx = row * geometry.numOfCellsInRow + col;
        if (idx >= cells.size()) return null;
        return cells.get(idx);
    }

    void assingNeighbours() {
        cells.forEach(cell -> {
            assingNeighbour(cell, 0, -1);
            assingNeighbour(cell, 0, 1);
            assingNeighbour(cell, -1, 0);
            assingNeighbour(cell, 1, 0);

            int h = cell.col % 2 == 0 ? -1 : 1;
            assingNeighbour(cell, h, - 1);
            assingNeighbour(cell, h, 1);
        });
    }

    void assingNeighbour(Cell cell, int dr, int dc) {
        cell.addNeighbour(getCell(cell.row + dr, cell.col + dc));
    }

    void updateNeighbourBombs() { cells.forEach(Cell::updateNeighbourBombs); }

}
