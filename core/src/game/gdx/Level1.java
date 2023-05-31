package game.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Level1 implements gdx, Screen {
    Start game;
    Sound sound;
    Sound soundSpeed;
    static Music music;
    Texture background;
    Texture fon;
    OrthographicCamera camera;


    Hero1 me;
    Enemy11 enemy;
    Enemy12 enemy2;
    Enemy13 enemy3;
    Enemy14 enemy4;
    Enemy15 enemy5;
    Speed speed;
    Timer timer;

    KeyboardAdapter inputProcessor = new KeyboardAdapter();

    public Level1(Start gam) {
        this.game = gam;
        timer = new Timer(1000, actionListener);
        Gdx.input.setInputProcessor(inputProcessor);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        me = new Hero1(45, 250);
        speed = new Speed();
        enemy = new Enemy11();
        enemy2 = new Enemy12();
        enemy3 = new Enemy13();
        enemy4 = new Enemy14();
        enemy5 = new Enemy15();

        music = Gdx.audio.newMusic(Gdx.files.internal("@txutside - So be it__QiziBeatz [Getbeat.ru].mp3"));
        music.setLooping(true);
        sound = Gdx.audio.newSound(Gdx.files.internal("fa4811c6c7e5a78.mp3"));
        soundSpeed = Gdx.audio.newSound(Gdx.files.internal("35b6b80c7de1e22.mp3"));
        fon = new Texture(Gdx.files.internal("1616711132_33-p-zadnii-fon-dlya-igri-38.jpg"));
        background = new Texture(Gdx.files.internal("map_rendered_size_7761_1 (2).png"));
        enemy.enemies = new Array<Rectangle>();
        enemy.spawnEnemy();
        enemy.enemies2 = new Array<Rectangle>();
        enemy.spawnEnemy2();
        enemy2.enemies = new Array<Rectangle>();
        enemy2.spawnEnemy3();
        enemy2.enemies2 = new Array<Rectangle>();
        enemy2.spawnEnemy4();
        enemy3.enemies = new Array<Rectangle>();
        enemy3.spawnEnemy5();
        enemy3.enemies2 = new Array<Rectangle>();
        enemy3.spawnEnemy6();
        enemy4.enemies = new Array<Rectangle>();
        enemy4.spawnEnemy7();
        enemy4.enemies2 = new Array<Rectangle>();
        enemy4.spawnEnemy8();
        enemy5.enemies = new Array<Rectangle>();
        enemy5.spawnEnemy9();
        enemy5.enemies2 = new Array<Rectangle>();
        enemy5.spawnEnemy10();
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(fon, 1, 1);
        game.batch.draw(background, 1, 1);
        enemy.render(game.batch);
        enemy2.render(game.batch);
        enemy3.render(game.batch);
        enemy4.render(game.batch);
        enemy5.render(game.batch);
        speed.render(game.batch);
        me.render(game.batch);
        game.batch.end();
        me.moveTo(inputProcessor.getDirection());
        if(((me.getPositionX() > 540 || me.getPositionX() + 78> 540) && (me.getPositionX() < 560 || me.getPositionX() + 78< 560) && (me.getPositionY() > 478|| me.getPositionY() + 78> 480) && (me.getPositionY() < 478|| me.getPositionY() + 78< 480)) && (speed.p)) {
            speed.swap();
            soundSpeed.play();
            inputProcessor.swap1();
            timer.start();
        }
        if((me.getPositionX() > enemy.getPositionX() || me.getPositionX() + 78> enemy.getPositionX()) && (me.getPositionX() < enemy.getPositionX() + 55 || me.getPositionX() + 78< enemy.getPositionX() + 55) && (me.getPositionY() > enemy.getPositionY() || me.getPositionY() + 78> enemy.getPositionY()) && (me.getPositionY() < enemy.getPositionY()+55 || me.getPositionY() + 78< enemy.getPositionY()+55)) {
            sound.play();
            me.setPositionX(45);
            me.setPositionY(250);
        }
        if((me.getPositionX() > enemy2.getPositionX() || me.getPositionX() + 78> enemy2.getPositionX()) && (me.getPositionX() < enemy2.getPositionX() + 55 || me.getPositionX() + 78< enemy2.getPositionX() + 55) && (me.getPositionY() > enemy2.getPositionY() || me.getPositionY() + 78> enemy2.getPositionY()) && (me.getPositionY() < enemy2.getPositionY()+55 || me.getPositionY() + 78< enemy2.getPositionY()+55)) {
            me.setPositionX(45);
            me.setPositionY(250);
            sound.play();
        }
        if((me.getPositionX() > enemy3.getPositionX() || me.getPositionX() + 78> enemy3.getPositionX()) && (me.getPositionX() < enemy3.getPositionX() + 55 || me.getPositionX() + 78< enemy3.getPositionX() + 55) && (me.getPositionY() > enemy3.getPositionY() || me.getPositionY() + 78> enemy3.getPositionY()) && (me.getPositionY() < enemy3.getPositionY()+55 || me.getPositionY() + 78< enemy3.getPositionY()+55)) {
            me.setPositionX(45);
            me.setPositionY(250);
            sound.play();
        }
        if((me.getPositionX() > enemy4.getPositionX() || me.getPositionX() + 78> enemy4.getPositionX()) && (me.getPositionX() < enemy4.getPositionX() + 55 || me.getPositionX() + 78< enemy4.getPositionX() + 55) && (me.getPositionY() > enemy4.getPositionY() || me.getPositionY() + 78> enemy4.getPositionY()) && (me.getPositionY() < enemy4.getPositionY()+55 || me.getPositionY() + 78< enemy4.getPositionY()+55)) {
            me.setPositionX(45);
            me.setPositionY(250);
            sound.play();
        }
        if((me.getPositionX() > enemy5.getPositionX() || me.getPositionX() + 78> enemy5.getPositionX()) && (me.getPositionX() < enemy5.getPositionX() + 55 || me.getPositionX() + 78< enemy5.getPositionX() + 55) && (me.getPositionY() > enemy5.getPositionY() || me.getPositionY() + 78> enemy5.getPositionY()) && (me.getPositionY() < enemy5.getPositionY()+55 || me.getPositionY() + 78< enemy5.getPositionY()+55)) {
            me.setPositionX(45);
            me.setPositionY(250);
            sound.play();
        }
        if (me.getPositionX() > 1085 && me.getPositionY() < 500) {
            game.setScreen(new Level2(game));
            dispose();
            timer.stop();
        }
    }
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            inputProcessor.swap2();
        }
    };
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        music.play();
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
        timer.stop();
        me.dispose();
        enemy.dispose();
        enemy2.dispose();
        enemy3.dispose();
        enemy4.dispose();
        enemy5.dispose();
        speed.dispose();
        fon.dispose();
        background.dispose();
        soundSpeed.dispose();
        sound.dispose();
    }
}
