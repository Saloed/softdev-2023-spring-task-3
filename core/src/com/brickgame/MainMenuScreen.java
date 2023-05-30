package com.brickgame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brickgame.Games.Piece;

public class MainMenuScreen implements Screen {
    private final BrickGame brickGame;
    private final Stage stage;
    int currentButton;

    public MainMenuScreen(BrickGame game) {
        brickGame = game;
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);

        brickGame.assetsManager.queueAddSkin();
        brickGame.assetsManager.manager.finishLoading();
        Skin skin = brickGame.assetsManager.manager.get("skin/uiskin.json");

        currentButton = 0;
        //создание кнопок
        TextButton shoot = new TextButton("Shoot", skin);
        shoot.setSize(5*Piece.SIZE, Piece.SIZE);
        TextButton race = new TextButton("Race", skin);
        race.setSize(5*Piece.SIZE, Piece.SIZE);
        TextButton snake = new TextButton("Snake", skin);
        snake.setSize(5*Piece.SIZE, Piece.SIZE);
        TextButton tetris = new TextButton("Tetris", skin);
        tetris.setSize(5*Piece.SIZE, Piece.SIZE);
        TextButton arcanoid = new TextButton("Arcanoid", skin);
        arcanoid.setSize(5*Piece.SIZE, Piece.SIZE);
        TextButton exit = new TextButton("Exit", skin);
        exit.setSize(5*Piece.SIZE, Piece.SIZE);
        final Button musicButton = new Button(null, skin, "music");

        //добавление кнопок в таблицу
        table.add(arcanoid).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(shoot).fillX().uniformX();
        table.row().pad(0, 0, 10, 0);
        table.add(race).fillX().uniformX();
        table.row().pad(0, 0, 10, 0);
        table.add(snake).fillX().uniformX();
        table.row().pad(0, 0, 10, 0);
        table.add(tetris).fillX().uniformX();
        table.row().pad(0, 0, 10, 0);
        table.add(exit).fillX().uniformX();
        table.row().pad(0, 0, 0, 0);
        table.add(musicButton).center();

        // создание листенеров для кнопок
        shoot.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                brickGame.changeScreen(2);
            }
        });
        race.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                brickGame.changeScreen(3);
            }
        });
        snake.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                brickGame.changeScreen(4);
            }
        });
        tetris.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                brickGame.changeScreen(5);
            }
        });
        arcanoid.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                brickGame.changeScreen(1);
            }
        });
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        musicButton.setChecked(!brickGame.music.isPlaying());
        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (brickGame.music.isPlaying()) {
                    brickGame.music.pause();
                } else brickGame.music.play();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f / 255f, 66f / 255f, 231f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}