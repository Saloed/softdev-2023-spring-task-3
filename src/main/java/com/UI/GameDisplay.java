package com.UI;

import com.go.*;

import javax.swing.*;
import java.awt.*;

import static com.UI.DisplayConfig.FON;

public class GameDisplay {


    public GameDisplay() {
        Board board = new Board(); // Создает игровое поле
        Game game = new Game(board); // Создает игру

        // Создает фрейм
        JFrame frame = new JFrame("StrateGO");
        JLabel background = new JLabel();
        background.setIcon(FON);
        background.setLayout(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(DisplayConfig.WINDOW_SIZE);
        frame.setMinimumSize(DisplayConfig.MIN_WINDOW_SIZE);


        // Создает панель с кнопками
        GamePanel gamePanel = new GamePanel(board, game);
        gamePanel.setOpaque(false);
        background.add(gamePanel);
        GameButtonsControlPanel buttons = new GameButtonsControlPanel(new GridLayout(6, 1, 1, 0));
        buttons.setOpaque(false);
        buttons.addNewGameListener(gamePanel);
        buttons.addRemoveStoneListener(new RemoveStoneListener(game, board, gamePanel));
        buttons.addSaveGameListener(new SaveGameListener(game));
        buttons.addPassMoveListener(new PassMoveListener(game, board));
        buttons.addLoveButtonListener(new LoveButtonListener());
        buttons.addOpenGameListener(new OpenGameListener(game, gamePanel));

        GameButtonsControlContainerPanel buttonsContainer = new GameButtonsControlContainerPanel(new FlowLayout(FlowLayout.CENTER), buttons);
        buttonsContainer.setOpaque(false);
        background.add(buttonsContainer, BorderLayout.WEST);

        frame.add(background);

        frame.pack(); // Устанавливает размеры фрейма в соответствии с размером содержимого
        frame.setVisible(true); // Делает фрейм видимым
        gamePanel.repaint(); // Рисует игровую панель
    }
}
