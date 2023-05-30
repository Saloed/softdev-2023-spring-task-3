package com.brickgame;

import com.brickgame.Games.Piece;
import com.badlogic.gdx.backends.lwjgl3.*;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Brick Game Simulator");
        config.setForegroundFPS(60);
        config.setResizable(false);
        config.setWindowedMode((int) (16 * Piece.SIZE), (int) (20 * Piece.SIZE));
        new Lwjgl3Application(new BrickGame(), config);
    }
}