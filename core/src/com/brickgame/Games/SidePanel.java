package com.brickgame.Games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brickgame.BrickGame;

public class SidePanel {
    public Score score;

    public int level;
    SpriteBatch batch;
    public Label levelLabel;
    public Button musicButton;
    BrickGame game;


    public SidePanel(SpriteBatch spriteBatch, final BrickGame game) {
        this.batch = spriteBatch;
        this.game = game;
        score = new Score(batch);
        level = 1;
        levelLabel = new Label("Level: " + level, game.skin);
        levelLabel.setPosition(11 * 32, 32 * 15);
        musicButton = new Button(null, game.skin, "music");
        musicButton.setPosition(11 * Piece.SIZE, 13 * Piece.SIZE);
        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.music.isPlaying()) {
                    game.music.pause();
                } else game.music.play();
            }
        });
    }

    public boolean isNextLevel() {
        if (score.score >= level * 1000) {
            level++;
            game.achives.play();
            levelLabel.setText("Level: " + level);
            return true;
        }
        return false;
    }

    public void draw() {
        levelLabel.draw(batch, 1);
        musicButton.draw(batch, 1);
        score.draw();
    }
}
