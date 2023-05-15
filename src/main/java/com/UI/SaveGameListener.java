package com.UI;

import com.go.Game;

class SaveGameListener implements GameButtonsControlPanel.SaveGameListener {

    private final Game game;

    SaveGameListener(Game game) {
        this.game = game;
    }

    @Override
    public void onSaveGameClick() {
        game.saveGame();
    }
}