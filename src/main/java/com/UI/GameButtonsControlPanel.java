package com.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        JButton newGame = new JButton("Новая игра (N)");
        JButton saveGame = new JButton("Сохранить игру (S)");
        JButton openGame = new JButton("Открыть игру (O)");
        JButton passButton = new JButton("Пасс (P)");
        JButton cancelButton = new JButton("Отменить ход (R)");
        JButton loveButton = new JButton("Разработчик топ (L)");


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

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_N:
                        if (newGameListener != null)
                            newGameListener.onNewGameClick();
                        break;
                    case KeyEvent.VK_S:
                        if (saveGameListener != null)
                            saveGameListener.onSaveGameClick();
                        break;
                    case KeyEvent.VK_O:
                        if (openGameListener != null)
                            openGameListener.onOpenGameClick();
                        break;
                    case KeyEvent.VK_P:
                        if (passMoveListener != null)
                            passMoveListener.onPassMoveClick();
                        break;
                    case KeyEvent.VK_R:
                        if (removeStoneListener != null)
                            removeStoneListener.onRemoveStoneClick();
                        break;
                    case KeyEvent.VK_L:
                        if (loveButtonListener != null)
                            loveButtonListener.onLoveButtonClick();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        this.setFocusable(true);
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