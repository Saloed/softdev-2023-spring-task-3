package com.brickgame.Games.Arcanoid;

import com.badlogic.gdx.graphics.Texture;
import com.brickgame.Games.Piece;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brickgame.BrickGame;
import com.brickgame.Games.SidePanel;

public class ArcanoidGameScreen implements Screen {
    private final BrickGame game;
    private SpriteBatch batch;
    private SidePanel sidePanel;
    private Stage stage;
    private Ball ball;
    private Platform platform;
    private Blocks blocks;
    private Texture gameGrid;

    private float  timeStepUpdPosPlatfrm;
    private final float timeStepUpdatePosPlatLimit = 0.1f;

    public ArcanoidGameScreen(BrickGame gam) {
        game = gam;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        gameGrid = new Texture(Gdx.files.internal("background.png"));
        blocks = new Blocks(batch);
        platform = new Platform(batch, BrickGame.GRID_WIDTH / 2, 0, 3);
        ball = new Ball(batch, platform.platform[platform.platform.length / 2].getX(), 1, 1, 1);
        sidePanel = new SidePanel(batch, game);

        stage.addActor(sidePanel.musicButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f / 255f, 66f / 255f, 231f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // обновление элементов игры
        timeStepUpdPosPlatfrm += Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && timeStepUpdPosPlatfrm >= timeStepUpdatePosPlatLimit) {
            platform.moveRight();
            timeStepUpdPosPlatfrm = 0;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)&& timeStepUpdPosPlatfrm >= timeStepUpdatePosPlatLimit) {
            platform.moveLeft();
            timeStepUpdPosPlatfrm = 0;
        }
        ball.updatePosition(platform);
        ball.collidesWithBlocks(blocks);

        // Если нужно, то проигрываются соответсвующее звуки
        if (ball.isNeedIncreaseScore) sidePanel.score.increaseScore();
        if (ball.isNeedPlayHit) game.hit.play();
        if (ball.isNeedPlayBroke) game.broke.play();

        // отрисовка элементов игры
        batch.begin();
        batch.draw(gameGrid, 0, 0, BrickGame.GRID_WIDTH * Piece.SIZE, BrickGame.GRID_HEIGHT * Piece.SIZE);
        blocks.draw();
        platform.draw();
        ball.draw();
        sidePanel.draw();
        stage.draw();
        batch.end();

        // принудительный выход из игры, нажатием клавиши Escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.changeScreen(0);

        // переход на следующий уровень
        if (blocks.blocks.size() == 0) {
            platform = new Platform(batch, BrickGame.GRID_WIDTH / 2, 0, 3);
            ball = new Ball(batch, platform.platform[platform.platform.length / 2].getX(), 1, 1, 1);
            blocks.changeLevel();
            sidePanel.levelLabel.setText(("Level: " + blocks.level));
        }

        // проигрыш
        if (ball.ball.getY() < 0) {
            game.changeScreen(6);
            game.endGameScreen.beforeGameScreen = 1;
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
