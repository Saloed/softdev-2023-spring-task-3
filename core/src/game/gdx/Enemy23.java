package game.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;
import java.util.Iterator;

public class Enemy23 {
    private final Texture texture;
    public Array<Rectangle> enemies;
    private boolean p;
    private float posX;
    private float posY;
    public Array<Rectangle> enemies2;
    public long lastDropTime;
    public long lastDropTime2;
    private final Vector2 position = new Vector2();

    public Enemy23() {
        this.texture = new Texture(Gdx.files.internal("1644923227_1-fikiwiki-com-p-kartinki-krasnii-krug-1.png"));
        p = true;
    }
    public void spawnEnemy25() {
        Rectangle enemy = new Rectangle();
        enemy.x = 440;
        enemy.y = 59;
        enemies.add(enemy);
        lastDropTime = TimeUtils.nanoTime();
    }
    public void spawnEnemy26() {
        Rectangle enem = new Rectangle();
        enem.x = 440;
        enem.y = 563;
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
        if (TimeUtils.nanoTime() - lastDropTime2 > 844772000L) spawnEnemy26();
        if (TimeUtils.nanoTime() - lastDropTime > 1689544000L) spawnEnemy25();
        for (Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext(); ) {
            Rectangle enemy = iter.next();
            enemy.y += 620 * Gdx.graphics.getDeltaTime();
            if (enemy.y > 563) iter.remove();
            posX = enemy.x;
            posY = enemy.y;
        }
        for (Iterator<Rectangle> iter = enemies2.iterator(); iter.hasNext();) {
            Rectangle enem = iter.next();
            enem.y -= 540 * Gdx.graphics.getDeltaTime();
            if (enem.y < 59) iter.remove();
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
