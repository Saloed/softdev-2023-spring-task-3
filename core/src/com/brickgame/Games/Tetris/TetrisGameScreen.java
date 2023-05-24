package com.brickgame.Games.Tetris;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.brickgame.Games.SidePanel;

import java.util.ArrayList;
import java.util.Collections;


public class TetrisGameScreen implements Screen {
    private final BrickGame game;
    private Board board;
    private SpriteBatch batch;
    private SidePanel sidePanel;
    private Stage stage;
    private Texture gameGrid;
    private Tetromino currentTetromino;
    private static boolean gameOver;
    private static final ArrayList<Integer> types = new ArrayList<>();
    public TetrisGameScreen(BrickGame gam) {
        game = gam;
    }

    public void createNewTetromino() {
        if (types.isEmpty()) {
            Collections.addAll(types, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(types);
        }
        int currenttype = types.get(0);
        types.remove(0);
        currentTetromino = new Tetromino(batch, new Piece(MathUtils.random(BrickGame.GRID_WIDTH/2 - 1, BrickGame.GRID_WIDTH/2 + 1), BrickGame.GRID_HEIGHT - 2), currenttype, board);
        for (Piece p : currentTetromino.tetromino) {
            if (board.board[(int) p.getX()][(int) p.getY()] != null) {
                gameOver = true;
                break;
            }
        }
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();
        board = new Board(batch);

        gameOver = false;
        sidePanel = new SidePanel(batch, game);
        Gdx.input.setInputProcessor(stage);
        gameGrid = new Texture(Gdx.files.internal("background.png"));
        stage.addActor(sidePanel.musicButton);
        createNewTetromino();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f / 255f, 66f / 255f, 231f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        currentTetromino.move();

        if (currentTetromino.isNewTetromino) {
            board.setTetromino(currentTetromino);
            board.clearRows();
            game.hit.play();
            createNewTetromino();
        }

        if (board.isNeedPlayAchives) {
            game.achives.play();
            board.isNeedPlayAchives = false;
        }

        if (board.isNeedIncreaseScore) {
            sidePanel.score.increaseScore();
            board.isNeedIncreaseScore = false;
        }
        // переход на следующий уровень
        if (sidePanel.isNextLevel() && Tetromino.TIME_MOVE_LIMIT > 0.05f) Tetromino.TIME_MOVE_LIMIT -= 0.05f;

        // отрисовка элементов игры
        batch.begin();
        batch.draw(gameGrid, 0, 0, BrickGame.GRID_WIDTH * Piece.SIZE, BrickGame.GRID_HEIGHT * Piece.SIZE);
        board.draw();
        sidePanel.draw();
        currentTetromino.draw();
        stage.draw();
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.changeScreen(0); // принудительный выход из игры

        if (gameOver) {
            game.changeScreen(6);
            game.endGameScreen.beforeGameScreen = 5;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        gameGrid.dispose();
    }
}




