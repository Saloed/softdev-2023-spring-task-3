package game.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Level2 implements gdx2, Screen {
    Start game;
    Texture background;
    Texture fon;
    OrthographicCamera camera;
    KeyboardAdapter inputProcessor = new KeyboardAdapter();
    public Hero2 me;
    Enemy21 enemy;
    Enemy22 enemy2;
    Enemy23 enemy3;
    Enemy24 enemy4;
    Enemy25 enemy5;
    Enemy26 enemy6;
    Enemy27 enemy7;
    Enemy28 enemy8;
    Enemy29 enemy9;
    public Boolean flag;
    Timer timer;
    Vulnerability vulnerability;
    public Level2(Start gam) {
        this.game = gam;
        timer = new Timer(1000, actionListener);
        Gdx.input.setInputProcessor(inputProcessor);
        me = new Hero2(120, 350);
        vulnerability = new Vulnerability();
        flag = true;
        enemy = new Enemy21();
        enemy2 = new Enemy22();
        enemy3 = new Enemy23();
        enemy4 = new Enemy24();
        enemy5 = new Enemy25();
        enemy6 = new Enemy26();
        enemy7 = new Enemy27();
        enemy8 = new Enemy28();
        enemy9 = new Enemy29();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        fon = new Texture(Gdx.files.internal("1616711132_33-p-zadnii-fon-dlya-igri-38.jpg"));
        background = new Texture(Gdx.files.internal("map_rendered_size_7788_1.png"));
        enemy.enemies = new Array<Rectangle>();
        enemy.spawnEnemy21();
        enemy.enemies2 = new Array<Rectangle>();
        enemy.spawnEnemy22();
        enemy2.enemies = new Array<Rectangle>();
        enemy2.spawnEnemy23();
        enemy2.enemies2 = new Array<Rectangle>();
        enemy2.spawnEnemy24();
        enemy3.enemies = new Array<Rectangle>();
        enemy3.spawnEnemy25();
        enemy3.enemies2 = new Array<Rectangle>();
        enemy3.spawnEnemy26();
        enemy4.enemies = new Array<Rectangle>();
        enemy4.spawnEnemy27();
        enemy4.enemies2 = new Array<Rectangle>();
        enemy4.spawnEnemy28();
        enemy5.enemies = new Array<Rectangle>();
        enemy5.spawnEnemy29();
        enemy5.enemies2 = new Array<Rectangle>();
        enemy5.spawnEnemy30();
        enemy6.enemies = new Array<Rectangle>();
        enemy6.spawnEnemy31();
        enemy6.enemies2 = new Array<Rectangle>();
        enemy6.spawnEnemy32();
        enemy7.enemies = new Array<Rectangle>();
        enemy7.spawnEnemy33();
        enemy7.enemies2 = new Array<Rectangle>();
        enemy7.spawnEnemy34();
        enemy8.enemies = new Array<Rectangle>();
        enemy8.spawnEnemy35();
        enemy8.enemies2 = new Array<Rectangle>();
        enemy8.spawnEnemy36();
        enemy9.enemies = new Array<Rectangle>();
        enemy9.spawnEnemy37();
        enemy9.enemies2 = new Array<Rectangle>();
        enemy9.spawnEnemy38();
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(fon, 1, 1);
        game.batch.draw(background, 1, 1);
        vulnerability.render(game.batch);
        me.render(game.batch);
        enemy.render(game.batch);
        enemy2.render(game.batch);
        enemy3.render(game.batch);
        enemy4.render(game.batch);
        enemy5.render(game.batch);
        enemy6.render(game.batch);
        enemy7.render(game.batch);
        enemy8.render(game.batch);
        enemy9.render(game.batch);
        game.batch.end();
        me.moveTo(inputProcessor.getDirection());
        if((me.getPositionX() > 900 || me.getPositionX() + 80 > 900) && (me.getPositionX() < 920 || me.getPositionX() + 80 < 920) && (me.getPositionY() > 200 || me.getPositionY() + 80 > 200) && (me.getPositionY() < 220 || me.getPositionY() + 80 < 220)) {
            flag = false;
            vulnerability.swap();
            timer.start();
        }
        if(flag) {
            timer.stop();
            if ((me.getPositionX() > enemy.getPositionX() || me.getPositionX() + 80 > enemy.getPositionX()) && (me.getPositionX() < enemy.getPositionX() + 55 || me.getPositionX() + 80 < enemy.getPositionX() + 55) && (me.getPositionY() > enemy.getPositionY() || me.getPositionY() + 80 > enemy.getPositionY()) && (me.getPositionY() < enemy.getPositionY() + 55 || me.getPositionY() + 80 < enemy.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if ((me.getPositionX() > enemy2.getPositionX() || me.getPositionX() + 80 > enemy2.getPositionX()) && (me.getPositionX() < enemy2.getPositionX() + 55 || me.getPositionX() + 80 < enemy2.getPositionX() + 55) && (me.getPositionY() > enemy2.getPositionY() || me.getPositionY() + 80 > enemy2.getPositionY()) && (me.getPositionY() < enemy2.getPositionY() + 55 || me.getPositionY() + 80 < enemy2.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if ((me.getPositionX() > enemy3.getPositionX() || me.getPositionX() + 80 > enemy3.getPositionX()) && (me.getPositionX() < enemy3.getPositionX() + 55 || me.getPositionX() + 80 < enemy3.getPositionX() + 55) && (me.getPositionY() > enemy3.getPositionY() || me.getPositionY() + 80 > enemy3.getPositionY()) && (me.getPositionY() < enemy3.getPositionY() + 55 || me.getPositionY() + 80 < enemy3.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if ((me.getPositionX() > enemy4.getPositionX() || me.getPositionX() + 80 > enemy4.getPositionX()) && (me.getPositionX() < enemy4.getPositionX() + 55 || me.getPositionX() + 80 < enemy4.getPositionX() + 55) && (me.getPositionY() > enemy4.getPositionY() || me.getPositionY() + 80 > enemy4.getPositionY()) && (me.getPositionY() < enemy4.getPositionY() + 55 || me.getPositionY() + 80 < enemy4.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if ((me.getPositionX() > enemy5.getPositionX() || me.getPositionX() + 80 > enemy5.getPositionX()) && (me.getPositionX() < enemy5.getPositionX() + 55 || me.getPositionX() + 80 < enemy5.getPositionX() + 55) && (me.getPositionY() > enemy5.getPositionY() || me.getPositionY() + 80 > enemy5.getPositionY()) && (me.getPositionY() < enemy5.getPositionY() + 55 || me.getPositionY() + 80 < enemy5.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if ((me.getPositionX() > enemy6.getPositionX() || me.getPositionX() + 80 > enemy6.getPositionX()) && (me.getPositionX() < enemy6.getPositionX() + 55 || me.getPositionX() + 80 < enemy6.getPositionX() + 55) && (me.getPositionY() > enemy6.getPositionY() || me.getPositionY() + 80 > enemy6.getPositionY()) && (me.getPositionY() < enemy6.getPositionY() + 55 || me.getPositionY() + 80 < enemy6.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if ((me.getPositionX() > enemy7.getPositionX() || me.getPositionX() + 80 > enemy7.getPositionX()) && (me.getPositionX() < enemy7.getPositionX() + 55 || me.getPositionX() + 80 < enemy7.getPositionX() + 55) && (me.getPositionY() > enemy7.getPositionY() || me.getPositionY() + 80 > enemy7.getPositionY()) && (me.getPositionY() < enemy7.getPositionY() + 55 || me.getPositionY() + 80 < enemy7.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if ((me.getPositionX() > enemy8.getPositionX() || me.getPositionX() + 80 > enemy8.getPositionX()) && (me.getPositionX() < enemy8.getPositionX() + 55 || me.getPositionX() + 80 < enemy8.getPositionX() + 55) && (me.getPositionY() > enemy8.getPositionY() || me.getPositionY() + 80 > enemy8.getPositionY()) && (me.getPositionY() < enemy8.getPositionY() + 55 || me.getPositionY() + 80 < enemy8.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if ((me.getPositionX() > enemy9.getPositionX() || me.getPositionX() + 80 > enemy9.getPositionX()) && (me.getPositionX() < enemy9.getPositionX() + 55 || me.getPositionX() + 80 < enemy9.getPositionX() + 55) && (me.getPositionY() > enemy9.getPositionY() || me.getPositionY() + 80 > enemy9.getPositionY()) && (me.getPositionY() < enemy9.getPositionY() + 55 || me.getPositionY() + 80 < enemy9.getPositionY() + 55)) {
                me.setPositionX(120);
                me.setPositionY(350);
            }
            if (me.getPositionX() > 1070) {
                game.setScreen(new Level3(game));
                dispose();
            }
        }
    }
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            flag = true;
        }
    };
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
        enemy.dispose();
        enemy2.dispose();
        enemy3.dispose();
        enemy4.dispose();
        enemy5.dispose();
        enemy6.dispose();
        enemy7.dispose();
        enemy8.dispose();
        enemy9.dispose();
        me.dispose();
        vulnerability.dispose();
        fon.dispose();
        background.dispose();
        timer.stop();
    }
}
