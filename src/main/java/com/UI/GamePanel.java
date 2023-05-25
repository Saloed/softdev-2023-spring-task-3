package com.UI;

import com.go.Board;
import com.go.Game;
import com.go.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

class GamePanel extends JPanel implements GameButtonsControlPanel.NewGameListener {

    private final static int SCALE_FACTOR = 30;
    private final static double STONE_SCALE_FACTOR = 0.9;

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
    private void drawBoard(Graphics g) {
        int boardSize = game.getBoardSize() - 1;
        int cellSize = Math.min((getWidth() - SCALE_FACTOR * 2) / boardSize, (getHeight() - SCALE_FACTOR * 2) / boardSize);
        int xIndent = SCALE_FACTOR + (getWidth() - SCALE_FACTOR * 2 - cellSize * boardSize) / 2;
        int yIndent = SCALE_FACTOR + (getHeight() - SCALE_FACTOR * 2 - cellSize * boardSize) / 2;
        g.setColor(DisplayConfig.GRID_COLOR);
        for (int i = 0; i <= boardSize; i++) {
            g.drawLine(xIndent + i * cellSize, yIndent, xIndent + i * cellSize, getHeight() - yIndent);
            g.drawLine(xIndent, yIndent + i * cellSize, getWidth() - xIndent, yIndent + i * cellSize);
        }
    }

    private int getCellSize() {
        int boardSize = game.getBoardSize();
        int widthSize = (getWidth() - SCALE_FACTOR * 2) / (boardSize - 1);
        int heightSize = (getHeight() - SCALE_FACTOR * 2) / (boardSize - 1);
        return Math.min(widthSize, heightSize);
    }

    private int getIndent(int measureSize) {
        int boardSize = game.getBoardSize();
        return SCALE_FACTOR + (measureSize - SCALE_FACTOR * 2 - getCellSize() * (boardSize - 1)) / 2;
    }

    private int getStoneSize() {
        return (int) (getCellSize() * STONE_SCALE_FACTOR);
    }

    private int getStoneCordByIndent(int indent, int cord) {
        return indent + cord * getCellSize() - getStoneSize() / 2;
    }

    // Отрисовка камней
    private void drawStones(Graphics g) {
        int xIndent = getIndent(getWidth());
        int yIndent = getIndent(getHeight());

        final Stone[][] boardContent = game.getBoardContent(board);
        for (Stone[] stones : boardContent) {
            for (Stone stone : stones) {
                if (stone != null) {
                    g.setColor(stone.color());
                    g.fillOval(getStoneCordByIndent(xIndent, stone.x()),
                            getStoneCordByIndent(yIndent, stone.y()), getStoneSize(), getStoneSize());
                }
            }
        }
    }

    private int mouseCordToStoneCord(int cord, int indent) {
        return (cord - indent + getCellSize() / 2) / getCellSize();
    }

    private void showErrorMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    // Считывает клики мыши и добавляет камень в массив
    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            int x = mouseCordToStoneCord(mouseEvent.getX(), getIndent(getWidth()));
            int y = mouseCordToStoneCord(mouseEvent.getY(), getIndent(getHeight()));

            if (x >= 0 && x < game.getBoardSize() && y >= 0 && y < game.getBoardSize()) {

                boolean isStoneAdd = game.addStoneByCords(x, y);

                if (!isStoneAdd) {
                    showErrorMessageDialog("Опачки!", "Не угадал!");
                }

                repaint();
            }
        }
    }

    @Override
    public void onNewGameClick() {
        game.newGame();
        this.repaint();
    }
}