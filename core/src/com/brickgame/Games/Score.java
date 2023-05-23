package com.brickgame.Games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;


public class Score {
    public int score;

    public final Label label;
    SpriteBatch batch;

    public Score(SpriteBatch batch) {
        score = 0;
        this.batch = batch;
        Skin skin = new Skin(Gdx.files.internal("D:\\BrickSim\\assets\\skin\\uiskin.json"));
        label = new Label("Score: " + score, skin);
        label.setWrap(true);
        label.setPosition(11 * Piece.SIZE, 18 * Piece.SIZE);
    }

    public void increaseScore() {
        score += 100;
        label.setText("Score: " + score);
    }

    public void draw() {
        label.draw(batch, 1);
    }
}



