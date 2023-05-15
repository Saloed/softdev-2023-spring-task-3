package com.UI;

import com.go.Game;

class PassMoveListener implements GameButtonsControlPanel.PassMoveListener {

    private final Game game;

    PassMoveListener(Game game) {
        this.game = game;
    }

    @Override
    public void onPassMoveClick() {
        game.move();
    }
}
