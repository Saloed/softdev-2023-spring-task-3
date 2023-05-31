package game.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.awt.*;
import java.util.Iterator;
import java.util.TimerTask;

public class Enemy32 {
    private final Texture texture;
    public Array<Rectangle> enemies;
    private boolean p;
    private float posX;
    private float posX2;
    private float posY;
    private float posY2;
    public Array<Rectangle> enemies2;
    private final Vector2 position = new Vector2();

    public Enemy32() {
        this.texture = new Texture(Gdx.files.internal("1644923227_1-fikiwiki-com-p-kartinki-krasnii-krug-1.png"));
        p = true;
    }
    double i = - Math.PI/2;
    double s = Math.PI/2;
    public void spawnEnemy37() {
        Rectangle enemy = new Rectangle();
        enemies.add(enemy);
        i += Math.PI/100;
    }
    public void spawnEnemy38() {
        Rectangle enem = new Rectangle();
        enemies2.add(enem);
        s += Math.PI/100;
    }
    public void render(Batch batch) {
        for(Rectangle enemy: enemies) {
            batch.draw(texture, enemy.x, enemy.y);
        }
        for(Rectangle enem: enemies2) {
            batch.draw(texture, enem.x, enem.y);
        }
        spawnEnemy38();
        spawnEnemy37();
        for (Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext(); ) {
            Rectangle enemy = iter.next();
            enemy.x = 620;
            enemy.x += (int) (Math.sin(i) * 350);
            enemy.y = 330;
            enemy.y += (int) (Math.cos(i) * 35);
            posX = enemy.x;
            posY = enemy.y;
        }
        for (Iterator<Rectangle> iter = enemies2.iterator(); iter.hasNext();) {
            Rectangle enem = iter.next();
            enem.x = 620;
            enem.x += (int) (Math.sin(s) * 350);
            enem.y = 320;
            enem.y += (int) (Math.cos(s) * 90);
            posX2 = enem.x;
            posY2 = enem.y;
        }
    }
    public float getPositionX(){
        return posX;
    }
    public float getPositionY(){
        return posY;
    }
    public float getPositionX2(){
        return posX2;
    }
    public float getPositionY2(){
        return posY2;
    }
    public void dispose() {
        texture.dispose();
    }
}
