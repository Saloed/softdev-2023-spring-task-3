package game.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import java.awt.*;

public class Level3 implements gdx2, Screen {
    Start game;
    Texture background;
    Texture fon;
    OrthographicCamera camera;
    KeyboardAdapter inputProcessor = new KeyboardAdapter();
    Hero3 me;
    Enemy31 enemy31;
    Enemy32 enemy32;
    public Level3(Start gam) {
        this.game = gam;
        Gdx.input.setInputProcessor(inputProcessor);
        me = new Hero3(70, 570);
        enemy31 = new Enemy31();
        enemy32 = new Enemy32();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        fon = new Texture(Gdx.files.internal("1616711132_33-p-zadnii-fon-dlya-igri-38.jpg"));
        background = new Texture(Gdx.files.internal("map_rendered_size_7872_1.png"));
        enemy31.enemies = new Array<Rectangle>();
        enemy31.spawnEnemy35();
        enemy31.enemies2 = new Array<Rectangle>();
        enemy31.spawnEnemy36();
        enemy32.enemies = new Array<Rectangle>();
        enemy32.spawnEnemy37();
        enemy32.enemies2 = new Array<Rectangle>();
        enemy32.spawnEnemy38();
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(fon, 1, 1);
        game.batch.draw(background, 1, 1);
        me.render(game.batch);
        enemy31.render(game.batch);
        enemy32.render(game.batch);
        game.batch.end();
        me.moveTo(inputProcessor.getDirection());
        if ((me.getPositionX() > enemy31.getPositionX() || me.getPositionX() + 80 > enemy31.getPositionX()) && (me.getPositionX() < enemy31.getPositionX() + 55 || me.getPositionX() + 80 < enemy31.getPositionX() + 55) && (me.getPositionY() > enemy31.getPositionY() || me.getPositionY() + 80 > enemy31.getPositionY()) && (me.getPositionY() < enemy31.getPositionY() + 55 || me.getPositionY() + 80 < enemy31.getPositionY() + 55)) {
            me.setPositionX(70);
            me.setPositionY(570);
        }
        if ((me.getPositionX() > enemy31.getPositionX2() || me.getPositionX() + 80 > enemy31.getPositionX2()) && (me.getPositionX() < enemy31.getPositionX2() + 55 || me.getPositionX() + 80 < enemy31.getPositionX2() + 55) && (me.getPositionY() > enemy31.getPositionY2() || me.getPositionY() + 80 > enemy31.getPositionY2()) && (me.getPositionY() < enemy31.getPositionY2() + 55 || me.getPositionY() + 80 < enemy31.getPositionY2() + 55)) {
            me.setPositionX(70);
            me.setPositionY(570);
        }
        if ((me.getPositionX() > enemy32.getPositionX() || me.getPositionX() + 80 > enemy32.getPositionX()) && (me.getPositionX() < enemy32.getPositionX() + 55 || me.getPositionX() + 80 < enemy32.getPositionX() + 55) && (me.getPositionY() > enemy32.getPositionY() || me.getPositionY() + 80 > enemy32.getPositionY()) && (me.getPositionY() < enemy32.getPositionY() + 55 || me.getPositionY() + 80 < enemy32.getPositionY() + 55)) {
            me.setPositionX(70);
            me.setPositionY(570);
        }
        if ((me.getPositionX() > enemy32.getPositionX2() || me.getPositionX() + 80 > enemy32.getPositionX2()) && (me.getPositionX() < enemy32.getPositionX2() + 55 || me.getPositionX() + 80 < enemy32.getPositionX2() + 55) && (me.getPositionY() > enemy32.getPositionY2() || me.getPositionY() + 80 > enemy32.getPositionY2()) && (me.getPositionY() < enemy32.getPositionY2() + 55 || me.getPositionY() + 80 < enemy32.getPositionY2() + 55)) {
            me.setPositionX(70);
            me.setPositionY(570);
        }
        if(me.getPositionY() < 240 && me.getPositionY() > 190 && me.getPositionX() > 100 && me.getPositionX() < 530){
            game.setScreen(new WinScreen(game));
            dispose();
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
        enemy31.dispose();
        me.dispose();
        fon.dispose();
        background.dispose();
    }
}
