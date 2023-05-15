package com.UI;

import com.go.Board;
import com.go.Game;
import com.go.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

class GamePanel extends JPanel implements GameButtonsControlPanel.NewGameListener {

    private final static int BOARD_SIZE = 13;

    private final Board board;
    private final Game game;

    public GamePanel(Board board, Game game) {

        this.board = board;
        this.game = game;

        enableEvents(AWTEvent.MOUSE_EVENT_MASK); //Считывает мышь
    }

    // Отрисовка игрового поля и камней
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawStones(g);
    }

    // Отрисовка игрового поля
    private void drawBoard (Graphics g) {
        int boardSize = BOARD_SIZE - 1;
        int cellSize = Math.min((getWidth() - 30 * 2) / boardSize, (getHeight() - 30 * 2) / boardSize);
        int xIndent = 30 + (getWidth() - 30 * 2 - cellSize * boardSize) / 2;
        int yIndent = 30 + (getHeight() - 30 * 2 - cellSize * boardSize) / 2;
        g.setColor(DisplayConfig.GRID_COLOR);
        for (int i = 0; i <= boardSize; i++) {
            g.drawLine(xIndent + i * cellSize, yIndent, xIndent + i * cellSize, getHeight() - yIndent);
            g.drawLine(xIndent, yIndent + i * cellSize, getWidth() - xIndent, yIndent + i * cellSize);
        }
    }

    // Отрисовка камней
    private void drawStones (Graphics g) {
        int boardSize = BOARD_SIZE;
        int cellSize = Math.min((getWidth() - 30 * 2) / (boardSize - 1), (getHeight() - 30 * 2) / (boardSize - 1));
        int xIndent = 30 + (getWidth() - 30 * 2 - cellSize * (boardSize - 1)) / 2;
        int yIndent = 30 + (getHeight() - 30 * 2 - cellSize * (boardSize - 1)) / 2;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Stone stone = board.getPosition(i, j);
                if (stone != null) {
                    g.setColor(stone.color());
                    int stoneSize = (int) (cellSize * 0.9); // Размер камня
                    g.fillOval(xIndent + i * cellSize - stoneSize / 2,
                            yIndent + j * cellSize - stoneSize / 2, stoneSize, stoneSize);
                }
            }
        }
    }

    // Считывает клики мыши и добавляет камень в массив
    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {

            //определение координат клика
            int boardSize = BOARD_SIZE - 1;
            int cellSize = Math.min((getWidth() - 30 * 2) / boardSize, (getHeight() - 30 * 2) / boardSize);
            int xIndent = 30 + (getWidth() - 30 * 2 - cellSize * boardSize) / 2;
            int yIndent = 30 + (getHeight() - 30 * 2 - cellSize * boardSize) / 2;
            if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                int i = (int) ((float) (x - xIndent + cellSize / 2) / cellSize);
                int j = (int) ((float) (y - yIndent + cellSize / 2) / cellSize);
                if (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE) {

                    // Добавление камня в массив и его отрисовка на игровой доске
                    Stone stone = new Stone(game.getCurrentPlayer(), i, j);
                    if (board.getPosition(i, j) == null) game.move();
                    if (board.positions[i][j] != null)
                        JOptionPane.showMessageDialog(null, "Занято, не угадал!", "Опачки!", JOptionPane.ERROR_MESSAGE);
                    else {
                        board.addStone(stone);
                        game.previousPlayer = stone;
                    }
                    this.repaint(xIndent + i * cellSize - cellSize / 2, yIndent + j * cellSize - cellSize / 2, cellSize, cellSize);
                }
            }
        }
    }

    @Override
    public void onNewGameClick() {
        game.newGame();
        this.repaint();
    }
}
