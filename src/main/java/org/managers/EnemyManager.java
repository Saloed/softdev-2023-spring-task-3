package org.managers;

import org.enemies.*;
import org.helpers.LoadSave;
import org.helpers.Utils;
import org.main.IncorrectLevelStructure;
import org.objects.PathPoint;
import org.scenes.Playing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.helpers.Constants.Direction.*;
import static org.helpers.Constants.Enemies.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[] enemyImgs;
    private ArrayList<Enemy> enemies;
    private PathPoint start, end;
    private int hpBarWidth;
    private BufferedImage slowEffect;
    private int[][] roadDirArr;

    public EnemyManager(Playing playing, PathPoint start, PathPoint end) {
        this.playing = playing;
        this.start = start;
        this.end = end;
        enemyImgs = new BufferedImage[4];

        enemies = new ArrayList<>();

        hpBarWidth = 20;

        loadEffectImg();
        loadEnemyImgs();
        loadRoadDirArr();
    }

    public void loadRoadDirArr() { //Загружает карту уровня, но из неё берутся только тайлы с дорогами и их направление
        try {
            roadDirArr = Utils.getRoadDirArr(playing.getGame().getTileManager().getTypeArr(), start, end);
        } catch (IncorrectLevelStructure e) {
            e.printStackTrace();
            LoadSave.resetLevel();
        }
    }


    private void loadEffectImg() {// Эффект заморозки
        slowEffect = LoadSave.getSpriteAtlas().getSubimage(0, 3 * 32, 32, 32);
    }

    private void loadEnemyImgs() {
        //Загружает картинки врагов из png со спрайтами
        BufferedImage atlas = LoadSave.getSpriteAtlas();

        for (int i = 0; i < 4; i++) {
            enemyImgs[i] = atlas.getSubimage(i * 32, 32, 32, 32);
        }

    }

    public void update() {

        for (Enemy e : enemies)
            if (e.isAlive())
                updateEnemyMove(e);
    }

    private void updateEnemyMove(Enemy e) {
        //Двигает врага в соответствии с направлением на карте направлений
        PathPoint currTile = getEnemyTile(e);

        int dir = roadDirArr[currTile.getyCord()][currTile.getxCord()];

        e.move(getSpeed(e.getEnemyType()), dir);

        PathPoint newTile = getEnemyTile(e);

        if (isTilesTheSame(newTile, end)) { //Наносит урон базе
            e.kill();
            playing.removeOneLife();
        }

        if (!isTilesTheSame(currTile, newTile)) {
            //При перемещении в новую клетку, сравнивает направления,
            // и если они не совпадают - выравнивает спрайт и меняет направление
            int newDir = roadDirArr[newTile.getyCord()][newTile.getxCord()];
            if (newDir != dir) {
                e.setPos(newTile.getxCord() * 32, newTile.getyCord() * 32);
                e.setLastDir(newDir);
            }
        }
    }

    private PathPoint getEnemyTile(Enemy e) {
        //Смотрит, на какой клетке находился враг в данный момент
        //LEFT и UP требуют добавления 31, так как точка спрайта врага находится в _левом верхнем_ углу самого спрайта
        return switch (e.getLastDir()) {
            case LEFT -> new PathPoint((int) ((e.getX() + 31) / 32), (int) (e.getY() / 32));
            case UP -> new PathPoint((int) (e.getX() / 32), (int) ((e.getY() + 31) / 32));
            default -> new PathPoint((int) (e.getX() / 32), (int) (e.getY() / 32));
        };
    }

    private boolean isTilesTheSame(PathPoint currTile, PathPoint newTile) {
        if (currTile.getxCord() == newTile.getxCord())
            return currTile.getyCord() == newTile.getyCord();
        return false;
    }

    public void spawnEnemy(int nextEnemy) {
        addEnemy(nextEnemy);
    }

    public void addEnemy(int enemyType) {
        //Создаёт новых врагов на координате начального спавна

        int x = start.getxCord() * 32;
        int y = start.getyCord() * 32;

        switch (enemyType) {
            case GOLEM -> enemies.add(new Golem(x, y, 0, this));
            case SLIME -> enemies.add(new Slime(x, y, 0, this));
            case BOAR -> enemies.add(new Boar(x, y, 0, this));
            case GOBLIN -> enemies.add(new Goblin(x, y, 0, this));
        }
    }

    public void draw(Graphics g) {
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                drawEnemy(e, g);
                drawHealthBar(e, g);
                drawEffects(e, g);
            }
        }
    }

    private void drawEffects(Enemy e, Graphics g) {
        if (e.isSlowed()) {
            g.drawImage(slowEffect, (int) e.getX(), (int) e.getY(), null);
        }
    }

    private void drawHealthBar(Enemy e, Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) e.getX() + 16 - (getNewBarWidth(e) / 2), (int) e.getY() - 10, getNewBarWidth(e), 3);
    }

    private int getNewBarWidth(Enemy e) {
        return (int) (hpBarWidth * e.getHealthBarFloat());
    }

    private void drawEnemy(Enemy e, Graphics g) {
        g.drawImage(enemyImgs[e.getEnemyType()], (int) e.getX(), (int) e.getY(), null);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public int getAmountOfAllLiveEnemies() {
        int size = 0;
        for (Enemy e : enemies)
            if (e.isAlive())
                size++;

        return size;
    }

    public void rewardPlayer(int enemyType) {
        playing.rewardPlayer(enemyType);
    }

    public void reset() {
        enemies.clear();
    }

    public void setStartAndEndPoints(PathPoint start, PathPoint end) {
        this.start = start;
        this.end = end;
    }
}
