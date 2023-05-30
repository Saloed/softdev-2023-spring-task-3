package com.brickgame.Games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brickgame.BrickGame;


public class Score {
    public int score;
    private final Label label;
    private final SpriteBatch batch;

    public Score(SpriteBatch batch, BrickGame game) {
        score = 0;
        this.batch = batch;
        label = new Label("Score: " + score, game.skin);
        label.setWrap(true);
        label.setPosition((BrickGame.GRID_WIDTH + 1) * Piece.SIZE, (BrickGame.GRID_HEIGHT - 2) * Piece.SIZE);
    }

    public void increaseScore() {
        score += 100;
        label.setText("Score: " + score);
    }

    public void draw() {
        label.draw(batch, 1);
    }
}



