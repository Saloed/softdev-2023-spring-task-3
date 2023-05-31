package game.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;
import java.util.Iterator;

public class Enemy14 {
    private final Texture texture;
    public Array<Rectangle> enemies;
    private boolean p;
    private float posX;
    private float posY;
    public Array<Rectangle> enemies2;
    public long lastDropTime;
    public long lastDropTime2;
    private final Vector2 position = new Vector2();

    public Enemy14() {
        this.texture = new Texture(Gdx.files.internal("1644923227_1-fikiwiki-com-p-kartinki-krasnii-krug-1.png"));
        p = true;
    }
    public void spawnEnemy7() {
        Rectangle enemy = new Rectangle();
        enemy.x = 943;
        enemy.y = 449;
        enemies.add(enemy);
        lastDropTime = TimeUtils.nanoTime();
    }
    public void spawnEnemy8() {
        Rectangle enem = new Rectangle();
        enem.x = 218;
        enem.y = 449;
        if (p) {
            p = false;
            lastDropTime2 = TimeUtils.nanoTime();
        }
        else {
            enemies2.add(enem);
            p = true;
            lastDropTime2 = TimeUtils.nanoTime();
        }
    }
    public void render(Batch batch) {
        for(Rectangle enemy: enemies) {
            batch.draw(texture, enemy.x, enemy.y);
        }
        for(Rectangle enem: enemies2) {
            batch.draw(texture, enem.x, enem.y);
        }
        if (TimeUtils.nanoTime() - lastDropTime2 > 1710241823L) spawnEnemy8();
        if (TimeUtils.nanoTime() - lastDropTime > 3420483646L) spawnEnemy7();
        for (Iterator<Rectangle> iter = enemies2.iterator(); iter.hasNext(); ) {
            Rectangle enemy = iter.next();
            enemy.x += 450 * Gdx.graphics.getDeltaTime();
            if (enemy.x > 943) iter.remove();
            posX = enemy.x;
            posY = enemy.y;
        }
        for (Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext();) {
            Rectangle enem = iter.next();
            enem.x -= 380 * Gdx.graphics.getDeltaTime();
            if (enem.x < 218) iter.remove();
            posX = enem.x;
            posY = enem.y;
        }
    }
    public float getPositionX(){
        return posX;
    }
    public float getPositionY(){
        return posY;
    }
    public void dispose() {
        texture.dispose();
    }
}
