package org.enemies;

import org.helpers.Constants;
import org.managers.EnemyManager;

import java.awt.*;

import static org.helpers.Constants.Direction.*;

public abstract class Enemy {
    protected EnemyManager enemyManager;

    protected float x, y;
    protected Rectangle bounds;
    protected int health;
    protected int maxHealth;
    protected int ID;
    protected int enemyType;
    protected int lastDir;
    protected boolean alive;
    protected int slowTickLimit;
    protected int slowTick;

    public Enemy(float x, float y, int ID, int enemyType, EnemyManager enemyManager) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.enemyType = enemyType;
        this.enemyManager = enemyManager;
        bounds = new Rectangle((int) x, (int) y, 32, 32);
        lastDir = -1;
        alive = true;
        this.slowTickLimit = 120; //Замедляет на 2 секунды (60 - одна секунда)
        slowTick = slowTickLimit;
        setStartHealth();
    }

    private void setStartHealth() {
        //Выставляет начальное значение здоровья из Constants
        health = Constants.Enemies.getStartHealth(enemyType);
        maxHealth = health;
    }

    public void hurt(int damage) {
        //Наносит урон. Если здоровье меньше 0, то умирает и даёт награду игроку
        this.health -= damage;
        if (health <= 0) {
            alive = false;
            enemyManager.rewardPlayer(enemyType);
        }
    }

    public void kill() {
        //Когда враг доходит до базы
        alive = false;
        health = 0;
    }

    public void slow() {
        //Замедляет. Когда slowTick вернётся в значение slowTickLimit, то замедление уйдёт
        slowTick = 0;
    }

    public void move(float speed, int dir) {
        //Двигается в выбранном направлении
        lastDir = dir;

        if (slowTick < slowTickLimit) {
            slowTick++;
            speed *= 0.5f;
        }
        switch (dir) {
            case LEFT -> this.x -= speed;
            case UP -> this.y -= speed;
            case RIGHT -> this.x += speed;
            case DOWN -> this.y += speed;
        }
        updateHitbox();
    }

    private void updateHitbox() {
        //Для проверки коллизии снарядов
        bounds.x = (int) x;
        bounds.y = (int) y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float getHealthBarFloat() {
        return health / (float) maxHealth;
    }
    //Нужен для визуализации здоровья

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getLastDir() {
        return lastDir;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isSlowed() {
        return slowTick < slowTickLimit;
    }

    public void setLastDir(int newDir) {
        this.lastDir = newDir;
    }
}
