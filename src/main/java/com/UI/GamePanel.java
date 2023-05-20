package com.UI;

import com.go.Board;
import com.go.Game;
import com.go.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Queue;

import static com.go.Board.BOARD_SIZE;

class GamePanel extends JPanel implements GameButtonsControlPanel.NewGameListener {

    private final static int SCALE_FACTOR = 30;
    private final static double STONE_SCALE_FACTOR = 0.9;

    private final Board board;
    private final Game game;
    private final NewGame newGame;
    
    public GamePanel(Board board, Game game) {

        this.board = board;
        this.game = game;
        this.newGame = new NewGame(board);
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
        int boardSize = BOARD_SIZE - 1;
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
        int boardSize = newGame.getBoardSize();
        int widthSize = (getWidth() - SCALE_FACTOR * 2) / (boardSize - 1);
        int heightSize = (getHeight() - SCALE_FACTOR * 2) / (boardSize - 1);
        return Math.min(widthSize, heightSize);
    }

    private int getIndent(int measureSize) {
        int boardSize = newGame.getBoardSize();
        return SCALE_FACTOR + (measureSize - SCALE_FACTOR * 2 - getCellSize() * (boardSize - 1)) / 2;
    }

    private int getStoneSize() {
        return (int) (getCellSize() * STONE_SCALE_FACTOR);
    }

    private int getStoneCordByIndent(int indent, int cord) {
        return indent + cord * getCellSize() - getStoneSize() / 2;
    }

    // Отрисовка камне
    private void drawStones(Graphics g) {
        int xIndent = getIndent(getWidth());
        int yIndent = getIndent(getHeight());

        final Stone[][] boardContent = newGame.getBoardContent();
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

    private int mouseCordToStone(int cord, int indent) {
       return (cord - indent + getCellSize() / 2) / getCellSize();
    }

    // Считывает клики мыши и добавляет камень в массив
    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {

            int cellSize = getCellSize();

            int xIndent = getIndent(getWidth());
            int yIndent = getIndent(getHeight());


            if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                int i = mouseCordToStone(mouseEvent.getX(), xIndent);
                int j = mouseCordToStone(mouseEvent.getY(), yIndent);

                if (i >= 0 && i < newGame.getBoardSize() && j >= 0 && j < newGame.getBoardSize()) {


                    // Добавление камня в массив и его отрисовка на игровой доске
                    Stone stone = new Stone(game.getCurrentPlayer(), i, j);
                    if (board.positions[i][j] != null)
                        JOptionPane.showMessageDialog(null, "Занято, не угадал!",
                                "Опачки!", JOptionPane.ERROR_MESSAGE);
                    else {
                        board.addStone(stone, game);
                        game.previousPlayer = stone;
                        game.move();
                    }
                    this.repaint(xIndent + i * cellSize - cellSize / 2,
                            yIndent + j * cellSize - cellSize / 2, cellSize, cellSize);
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


class NewGame {
    private Color currentPlayer;
    private final Board board;
    private Queue<Stone> moves;

    public NewGame(Board board) {
        this.board = board;
    }

    public boolean addStoneByCords(int x, int y) {
        Stone stone = new Stone(currentPlayer, x, y);
        //board.addStone(stone);
        //
        return true;
    }

    public Stone[][] getBoardContent() {
        return this.board.getPositions();
    }
    
    public int getBoardSize() {
        return Board.BOARD_SIZE;
    }
}