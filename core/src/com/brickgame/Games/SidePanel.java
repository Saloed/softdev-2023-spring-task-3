package com.brickgame.Games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brickgame.BrickGame;

public class SidePanel {
    private final BrickGame game;
    private final SpriteBatch batch;
    public Score score;
    public Label levelLabel;
    public Button musicButton;
    public int level;


    public SidePanel(SpriteBatch spriteBatch, BrickGame game) {
        this.batch = spriteBatch;
        this.game = game;
        score = new Score(batch,game);
        level = 1;
        levelLabel = new Label("Level: " + level, game.skin);
        musicButton = new Button(null, game.skin, "music");
        levelLabel.setPosition((BrickGame.GRID_WIDTH + 1) * Piece.SIZE, (BrickGame.GRID_HEIGHT - 5) * Piece.SIZE);
        musicButton.setPosition((BrickGame.GRID_WIDTH + 1) * Piece.SIZE, (BrickGame.GRID_HEIGHT - 7) * Piece.SIZE);
        musicButton.setChecked(!game.music.isPlaying());
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
