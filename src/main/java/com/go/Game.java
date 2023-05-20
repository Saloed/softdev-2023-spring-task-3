package com.go;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import static com.go.Board.BOARD_SIZE;

public class Game implements IGame {
    private final Board board; // Состояние доски
    private Color currentPlayer;
    public Stone previousPlayer;

    // Конструктор
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

    // Метод, который сохраняет игру
    @Override
    public void saveGame() {
        saveArrayToFile(board.positions);
    }

    // Метод, который возвращает сохраненную игру
    @Override
    public void returnGame() {
        board.positions = loadArrayFromFile();
    }

    // Метод, который меняет текущего игрока после хода
    @Override
    public void move() {
        currentPlayer = (currentPlayer == Color.BLACK) ? Color.WHITE : Color.BLACK;
    }

    // Метод, который возвращает текущего игрока
    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    // Метод, который возвращает предыдущего игрока
    public Stone getPreviousPlayer() {
        return previousPlayer;
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

    private Stone[][] loadArrayFromFile() {
        JFileChooser fileChooser = new JFileChooser("./Save games");
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return board.positions;
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
            return new Stone[BOARD_SIZE][BOARD_SIZE];
        }
    }
}