package org.main;

import java.awt.*;

public class Render {
    public Game game;

    public Render(Game game) {
        this.game = game;
    }

    public void render(Graphics g) {
        switch (GameStates.gameState) {
            case MENU -> game.getMenu().render(g);
            case PLAYING -> game.getPlaying().render(g);
            case EDIT -> game.getEditor().render(g);
            case GAME_OVER -> game.getGameOver().render(g);
            case GAME_WIN -> game.getGameWin().render(g);
        }
    }

}
