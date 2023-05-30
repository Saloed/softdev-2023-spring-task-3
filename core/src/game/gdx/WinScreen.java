package game.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class WinScreen implements Screen {

    Start game;
    Texture screensaver;

    OrthographicCamera camera;

    public WinScreen(Start gam) {
        this.game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        screensaver = new Texture(Gdx.files.internal("istockphoto-1227393046-612x612.jpg"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(screensaver, 1, 1);
        game.batch.end();
        if (Gdx.input.isTouched()) {
            game.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}