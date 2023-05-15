package com.UI;

import javax.swing.*;
import java.awt.*;

class GameButtonsControlPanel extends JPanel {

    interface NewGameListener {
        void onNewGameClick();
    }

    interface SaveGameListener {
        void onSaveGameClick();
    }

    interface RemoveStoneListener {
        void onRemoveStoneClick();
    }
    interface PassMoveListener {
        void onPassMoveClick();
    }

    interface LoveButtonListener {
        void onLoveButtonClick();
    }

    interface OpenGameListener {
        void onOpenGameClick();
    }

    private NewGameListener newGameListener;
    private SaveGameListener saveGameListener;
    private RemoveStoneListener removeStoneListener;
    private PassMoveListener passMoveListener;
    private LoveButtonListener loveButtonListener;
    private OpenGameListener openGameListener;

    public GameButtonsControlPanel(LayoutManager layoutManager) {
        super(layoutManager);
        JButton newGame = new JButton("Новая игра");
        JButton saveGame = new JButton("Сохранить игру");
        JButton openGame = new JButton("Открыть игру");
        JButton passButton = new JButton("Пасс");
        JButton cancelButton = new JButton("Отменить ход");
        JButton loveButton = new JButton("Разработчик топ");


        newGame.addActionListener(e -> {
            if (newGameListener != null)
                newGameListener.onNewGameClick();
        });

        saveGame.addActionListener(e -> {
            if (saveGameListener != null)
                saveGameListener.onSaveGameClick();
        });

        cancelButton.addActionListener(e -> {
            if (removeStoneListener != null)
                removeStoneListener.onRemoveStoneClick();
        });

        passButton.addActionListener(e -> {
            if (passMoveListener != null)
                passMoveListener.onPassMoveClick();
        });

        loveButton.addActionListener(e -> {
            if (loveButtonListener != null)
                loveButtonListener.onLoveButtonClick();
        });

        openGame.addActionListener(e -> {
            if (openGameListener != null)
                openGameListener.onOpenGameClick();
        });

        add(newGame);
        add(saveGame);
        add(openGame);
        add(passButton);
        add(cancelButton);
        add(loveButton);
    }

    public void addNewGameListener(NewGameListener newGameListener) {
        this.newGameListener = newGameListener;
    }

    public void addSaveGameListener(SaveGameListener saveGameListener) {
        this.saveGameListener = saveGameListener;
    }

    public void addRemoveStoneListener(RemoveStoneListener removeStoneListener) {
        this.removeStoneListener = removeStoneListener;
    }

    public void addPassMoveListener(PassMoveListener passMoveListener) {
        this.passMoveListener = passMoveListener;
    }

    public void addLoveButtonListener(LoveButtonListener loveButtonListener) {
        this.loveButtonListener = loveButtonListener;
    }

    public void addOpenGameListener(OpenGameListener openGameListener) {
        this.openGameListener = openGameListener;
    }
}