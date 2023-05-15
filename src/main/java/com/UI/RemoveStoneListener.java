package com.UI;

import com.go.Board;
import com.go.Game;

class RemoveStoneListener implements GameButtonsControlPanel.RemoveStoneListener {

    private final Board board;
    private final Game game;
    private final GamePanel gamePanel;

    RemoveStoneListener(Game game, Board board, GamePanel gamePanel) {
        this.board = board;
        this.game = game;
        this.gamePanel = gamePanel;
    }

    @Override
    public void onRemoveStoneClick() {
        board.removeStone(game.getPreviousPlayer());
        gamePanel.repaint();
        game.move();
    }
}
