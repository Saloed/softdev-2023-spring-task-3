package com.brickgame.Games.Tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.brickgame.Games.SidePanel;


public class TetrisGameScreen implements Screen {
    static BrickGame game;
    static Board board;
    static SpriteBatch batch;

    static SidePanel sidePanel;
    Stage stage;
    Texture gameGrid;
    static Tetromino currentTetromino;
    public static boolean gameOver;

    public TetrisGameScreen(BrickGame gam) {
        game = gam;
    }

    public static void createNewTetromino() {
        currentTetromino = new Tetromino(batch, new Piece(MathUtils.random(4, 6), 18), MathUtils.random(6));
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
        // переход на следующий уровень
        if (sidePanel.isNextLevel() && Tetromino.TIME_MOVE_LIMIT > 0.05f) Tetromino.TIME_MOVE_LIMIT -= 0.05f;

        // отрисовка элементов игры
        batch.begin();
        batch.draw(gameGrid, 0, 0, 10 * Piece.SIZE, 20 * Piece.SIZE);
        board.draw();
        sidePanel.draw();
        currentTetromino.draw();
        stage.draw();
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.changeScreen(0); // принудительный выход из игры

        if (gameOver) {
            game.changeScreen(6);
            game.endGameSceen.beforeGameScreen = 5;
        }
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




