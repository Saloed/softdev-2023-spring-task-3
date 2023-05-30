package com.go;

import com.UI.GameDisplay;

import java.io.*;


public class Game implements IGame {

    private final Board board; // Состояние доски
    private PlayerColor currentPlayer; // Текущий игрок

    public enum PlayerColor {
        BLACK,
        WHITE
    }

    private Stone lastFixedStone; // Предыдущий установленный камень

    public Game(Board board) {
        this.board = board;
        currentPlayer = PlayerColor.BLACK;
    }

    // Метод, который создает новую игру
    @Override
    public void newGame() {
        board.clearBoard();
        currentPlayer = PlayerColor.BLACK;
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
        currentPlayer = (currentPlayer == PlayerColor.BLACK) ? PlayerColor.WHITE : PlayerColor.BLACK;
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

    // Возвращает последний зафиксированный камень
    @Override
    public Stone getLastFixedStone() {
        return lastFixedStone;
    }

    // Метод, который сохраняет игру
    @Override
    public void saveGame(File file) {
        saveArrayToFile(file, board.getPositions());
    }

    // Метод, который возвращает сохраненную игру из файла
    @Override
    public void returnGame(File file) {
        board.setPositions(loadArrayFromFile(file));
    }

    // Метод, который возвращает текущего игрока
    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    // Метод, который записывает игру в файл
    private void saveArrayToFile(File file, Stone[][] arr) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(String.valueOf(arr.length));
            bw.newLine();
            bw.write(String.valueOf(arr[0].length));
            bw.newLine();
            for (Stone[] stones : arr) {
                for (int j = 0; j < arr[0].length; j++) {
                    if (stones[j] != null) {
                        bw.write(stones[j].playerColor() + " ");
                    } else {
                        bw.write("null ");
                    }
                }
                bw.newLine();
            }
        } catch (IOException e) {
            GameDisplay.showErrorMessageDialog("Опачки!", "Ошибка записи в файл, попробуйте еще раз.");
        }
    }

    // Метод, который читает игру из файла
    private Stone[][] loadArrayFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
                        if (cell.equals("WHITE")) {
                            savePositions[i][j] = new Stone(PlayerColor.WHITE, i, j);
                        } else {
                            savePositions[i][j] = new Stone(PlayerColor.BLACK, i, j);
                        }
                    }
                }
            }
            return savePositions;
        } catch (IOException e) {
            GameDisplay.showErrorMessageDialog("Опачки!", "Ошибка чтения из файла, попробуйте еще раз.");
            return new Stone[getBoardSize()][getBoardSize()];
        }
    }

}