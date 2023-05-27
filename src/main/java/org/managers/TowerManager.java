package org.managers;

import org.enemies.Enemy;
import org.helpers.LoadSave;
import org.objects.Tower;
import org.scenes.Playing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TowerManager {
    private Playing playing;
    private BufferedImage[] towerImgs;
    private ArrayList<Tower> towers = new ArrayList<>();
    private int towerAmount = 0;


    public TowerManager(Playing playing) {
        this.playing = playing;
        loadTowerImgs();
    }

    private void loadTowerImgs() {
        //Загружает картинки башен из png спрайтов
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        towerImgs = new BufferedImage[3];
        for (int i = 0; i < 3; i++)
            towerImgs[i] = atlas.getSubimage((4 + i) * 32, 32, 32, 32);
    }

    public void addTower(Tower selectedTower, int xPos, int yPos) {
        towers.add(new Tower(xPos, yPos, towerAmount++, selectedTower.getTowerType()));
    }

    public void removeTower(Tower displayedTower) {
        towers.removeIf(tower -> tower.getId() == displayedTower.getId());
    }

    public void upgradeTower(Tower displayedTower) {
        for (Tower t : towers) {
            if (t.getId() == displayedTower.getId())
                t.upgradeTower();
        }
    }

    public void update() {
        for (Tower t : towers) {
            t.update();
            attackEnemyIfClose(t);
        }
    }

    private void attackEnemyIfClose(Tower t) {
        //Проверяет, есть ли враги в радиуса, и равен ли cooldown нулю, тогда стреляет
        for (Enemy e : playing.getEnemyManager().getEnemies()) {
            if (e.isAlive() && isEnemyInRange(t, e) && t.isCooldownOver()) {
                playing.shootEnemy(t, e);
                t.getResetCooldown();
            }
        }
    }


    private boolean isEnemyInRange(Tower t, Enemy e) {
        //Применяет метод из Utils, проверка врага в радиусе башни
        int range = org.helpers.Utils.GetHypoDistance(t.getX(), t.getY(), e.getX(), e.getY());
        return range < t.getRange();
    }

    public void draw(Graphics g) {
        for (Tower t : towers) {
            if (t.getSide()) {
                //Для отзеркаливания спрайта по вертикали
                g.drawImage(towerImgs[t.getTowerType()], t.getX() + 32, t.getY(), -32, 32, null);
            } else g.drawImage(towerImgs[t.getTowerType()], t.getX(), t.getY(), null);
        }
    }

    public Tower getTowerAt(int x, int y) {
        //Это для selectedTower в Playing
        for (Tower t : towers) {
            if (t.getX() == x && t.getY() == y)
                return t;
        }
        return null;
    }

    public BufferedImage[] getTowerImgs() {
        return towerImgs;
    }

    public void reset() {
        //Очищает список башен
        towers.clear();
        towerAmount = 0;
    }
}
