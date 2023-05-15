package com.UI;

import com.go.Game;

class OpenGameListener implements GameButtonsControlPanel.OpenGameListener {

    private final Game game;
    private final GamePanel gamePanel;

    OpenGameListener(Game game, GamePanel gamePanel) {
        this.game = game;
        this.gamePanel = gamePanel;
    }

    @Override
    public void onOpenGameClick() {
        game.returnGame();
        gamePanel.repaint();
    }
}

