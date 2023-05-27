package com.brickgame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private final BrickGame brickGame;
    private final Stage stage;

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

        //создание кнопок
        TextButton shoot = new TextButton("Shoot", skin);
        TextButton race = new TextButton("Race", skin);
        TextButton snake = new TextButton("Snake", skin);
        TextButton tetris = new TextButton("Tetris", skin);
        TextButton arcanoid = new TextButton("Arcanoid", skin);
        TextButton exit = new TextButton("Exit", skin);
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