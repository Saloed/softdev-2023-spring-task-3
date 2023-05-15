package com.UI;

import com.go.Board;
import com.go.Game;

import javax.swing.*;
import java.awt.*;

public class GameDisplay {

    private final static int BOARD_SIZE = 13;

    public static void main(String[] args) {
        Board board = new Board(BOARD_SIZE); // Создает игровое поле
        Game game = new Game(board); // Создает игру

        // Создает фрейм
        JFrame frame = new JFrame("StrateGO");
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(new ImageIcon("fon.jpg").getImage()));
        background.setLayout(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(DisplayConfig.WINDOW_SIZE);
        frame.setMinimumSize(DisplayConfig.MIN_WINDOW_SIZE);



        // Создает панель с кнопками
        GamePanel gamePanel = new GamePanel(board, game);
        gamePanel.setOpaque(false);
        background.add(gamePanel);
        GameButtonsControlPanel buttons = new GameButtonsControlPanel(new GridLayout(2, 3, 3, 0));
        buttons.setOpaque(false);
        buttons.addNewGameListener(gamePanel);
        buttons.addRemoveStoneListener(new RemoveStoneListener(game, board, gamePanel));
        buttons.addSaveGameListener(new SaveGameListener(game));
        buttons.addPassMoveListener(new PassMoveListener(game));
        buttons.addLoveButtonListener(new LoveButtonListener());
        buttons.addOpenGameListener(new OpenGameListener(game, gamePanel));

        GameButtonsControlContainerPanel buttonsContainer = new GameButtonsControlContainerPanel(new FlowLayout(FlowLayout.CENTER), buttons);
        buttonsContainer.setOpaque(false);
        background.add(buttonsContainer, BorderLayout.NORTH);

        frame.add(background);

        frame.pack(); // Устанавливает размеры фрейма в соответствии с размером содержимого
        frame.setVisible(true); // Делает фрейм видимым
        gamePanel.repaint(); // Рисует игровую панель
    }
}
