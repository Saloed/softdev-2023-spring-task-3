package com.go;

import javax.swing.*;
import java.awt.*;
import java.io.*;


public class Game implements IGame {

    private final Board board; // Состояние доски
    private Color currentPlayer; // Текущий игрок
    public Stone lastFixedStone; // Предыдущий установленный камень

    public Game(Board board) {
        this.board = board;
        currentPlayer = Color.BLACK;
    }

    // Метод, который создает новую игру
    @Override
    public void newGame() {
        board.clearBoard();
        currentPlayer = Color.BLACK;
    }

    // Метод, который добавляет камень на доску
    @Override
    public boolean addStoneByCords(int x, int y) {
        Stone stone = new Stone(currentPlayer, x, y);
        if (board.addStone(stone)) {
            lastFixedStone = stone;
            turn();
            return true;
        }
        return false;
    }

    // Меняет цвет текущего игрока
    @Override
    public void turn() {
        currentPlayer = (currentPlayer == Color.BLACK) ? Color.WHITE : Color.BLACK;
    }

    // Возвращает состояние игровой доски
    @Override
    public Stone[][] getBoardContent(Board board) {
        return board.getPositions();
    }

    // Возвращает размер доски
    @Override
    public int getBoardSize() {
        return Board.BOARD_SIZE;
    }

    // Метод, который сохраняет игру
    @Override
    public void saveGame() {
        saveArrayToFile(board.getPositions());
    }

    // Метод, который возвращает сохраненную игру
    @Override
    public void returnGame() {
        Stone[][] loadedStones = loadArrayFromFile();
        board.setPositions(loadedStones);
    }

    // Метод, который возвращает текущего игрока
    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    // Метод, который сохраняет игру в файл
    private void saveArrayToFile(Stone[][] arr) {
        JFileChooser fileChooser = new JFileChooser("./Save games");
        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) {
            bw.write(String.valueOf(arr.length));
            bw.newLine();
            bw.write(String.valueOf(arr[0].length));
            bw.newLine();
            for (Stone[] stones : arr) {
                for (int j = 0; j < arr[0].length; j++) {
                    if (stones[j] != null) {
                        bw.write(stones[j].color().getRGB() + " ");
                    } else {
                        bw.write("null ");
                    }
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод, который возвращает игру из файла
    private Stone[][] loadArrayFromFile() {
        JFileChooser fileChooser = new JFileChooser("./Save games");
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return board.getPositions();
        }

        File selectedFile = fileChooser.getSelectedFile();
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            int rows = Integer.parseInt(br.readLine());
            int cols = Integer.parseInt(br.readLine());
            Stone[][] savePositions = new Stone[rows][cols];
            for (int i = 0; i < rows; i++) {
                String[] cells = br.readLine().split(" ");
                for (int j = 0; j < cols; j++) {
                    String cell = cells[j];
                    if (cell.equals("null")) {
                        savePositions[i][j] = null;
                    } else {
                        int rgb = Integer.parseInt(cell);
                        savePositions[i][j] = new Stone(new Color(rgb), i, j);
                    }
                }
            }
            return savePositions;
        } catch (IOException e) {
            e.printStackTrace();
            return new Stone[getBoardSize()][getBoardSize()];
        }
    }
}