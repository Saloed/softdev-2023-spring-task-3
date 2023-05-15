package com.go;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

interface IGame {
    void newGame();
    void saveGame();
    void move();
    void returnGame();
}

interface IBoardController {
    void initBoard();
}

public class Game implements IGame {
    //private static final double KOMI = 6.5; // Фора, то бишь количество компенсации белым
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
        int numberOfGames = 0;
        saveArrayToFile(board.positions, numberOfGames);
    }

    // Метод, который возвращает сохраненную игру
    @Override
    public void returnGame() {
        board.positions = loadArrayFromFile();
    }

    @Override
    public void move() {
        currentPlayer = (currentPlayer == Color.BLACK) ? Color.WHITE : Color.BLACK;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public Stone getPreviousPlayer() {
        return previousPlayer;
    }

    // Метод, который сохраняет игру в файл
    private void saveArrayToFile(Stone[][] arr, int numberOfGames) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(arr.length).append('\n');
            sb.append(arr[0].length).append('\n');
            for (Stone[] stones : arr) {
                for (int j = 0; j < arr[0].length; j++) {
                    if (stones[j] != null) {
                        sb.append(stones[j].color().toString()).append('\n');
                    } else {
                        sb.append("null").append('\n');
                    }
                }
            }
            Files.writeString(Path.of(String.format("SaveGame %d", numberOfGames)),
                    sb.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Считывает сохраненную игру из файла
    private Stone[][] loadArrayFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return new Stone[13][13];
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
                        Color color = Color.decode(cell);
                        savePositions[i][j] = new Stone(color, i, j);
                    }
                }
            }
            return savePositions;
        } catch (IOException e) {
            e.printStackTrace();
            return new Stone[13][13];
        }
    }
}