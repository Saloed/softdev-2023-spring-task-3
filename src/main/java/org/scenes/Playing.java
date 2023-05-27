package org.scenes;

import org.enemies.Enemy;
import org.helpers.Constants;
import org.helpers.LoadSave;
import org.main.Game;
import org.managers.EnemyManager;
import org.managers.ProjectileManager;
import org.managers.TowerManager;
import org.managers.WaveManager;
import org.objects.PathPoint;
import org.objects.Tower;
import org.ui.ActionBar;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static org.helpers.Constants.Tiles.*;
import static org.main.GameStates.GAME_WIN;
import static org.main.GameStates.SetGameState;

public class Playing extends GameScene implements SceneMethods {

    private static final int GOLD_TICK_TIMER = 3; //Сколько секунд нужно, чтобы получить 1 доп. валюту
    private int[][] lvl;
    private ActionBar actionBar;
    private int mouseX, mouseY;
    private EnemyManager enemyManager;
    private TowerManager towerManager;
    private ProjectileManager projectileManager;
    private WaveManager waveManager;
    private PathPoint start, end;
    private Tower selectedTower;
    private int goldTick;
    private boolean gamePaused;


    public Playing(Game game) {
        super(game);

        loadDefaultLevel();

        gamePaused = false;

        actionBar = new ActionBar(0, 640, 640, 160, this);
        enemyManager = new EnemyManager(this, start, end);
        towerManager = new TowerManager(this);
        projectileManager = new ProjectileManager(this);
        waveManager = new WaveManager(this);
    }

    private void loadDefaultLevel() {
        lvl = LoadSave.GetLevelData();
        ArrayList<PathPoint> points = LoadSave.GetLevelPathPoints();
        start = points.get(0);
        end = points.get(1);
    }

    public void setLevel(int[][] lvl, PathPoint start, PathPoint end) {
        this.lvl = lvl;
        this.start = start;
        this.end = end;
    }

    public void update() {
        if (!gamePaused) {
            updateTick();
            waveManager.update();

            goldTick++;
            if (goldTick % (60 * GOLD_TICK_TIMER) == 0)
                actionBar.addGold(1);

            if (isAllEnemiesDead()) {
                if (isThereMoreWaves()) {
                    waveManager.startWaveTimer();
                    if (isWaveTimeOver()) {
                        waveManager.increaseWaveIndex();
                        enemyManager.getEnemies().clear();
                        waveManager.resetEnemyIndex();
                    }
                } else {
                    SetGameState(GAME_WIN);
                }
            }

            if (isTimeForNewEnemy())
                spawnEnemy();

            enemyManager.update();
            towerManager.update();
            projectileManager.update();
        }
    }

    private boolean isWaveTimeOver() {
        return waveManager.isWaveTimeOver();
    }

    private boolean isThereMoreWaves() {
        return waveManager.isThereMoveWaves();
    }

    private boolean isAllEnemiesDead() {
        if (waveManager.isThereMoreEnemiesInWave())
            return false;
        for (Enemy e : enemyManager.getEnemies()) {
            if (e.isAlive())
                return false;
        }
        return true;
    }

    private void spawnEnemy() {
        enemyManager.spawnEnemy(waveManager.getNextEnemy());
    }

    private boolean isTimeForNewEnemy() {
        if (getWaveManager().isTimeForNewEnemy()) {
            return getWaveManager().isThereMoreEnemiesInWave();
        }
        return false;
    }

    public void setSelectedTower(Tower selectedTower) {
        this.selectedTower = selectedTower;
    }

    @Override
    public void render(Graphics g) {
        drawLevel(g);
        actionBar.draw(g);
        enemyManager.draw(g);
        towerManager.draw(g);
        projectileManager.draw(g);

        drawSelectedTower(g);
        drawHighlight(g);
    }

    private void drawHighlight(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawRect(mouseX, mouseY, 32, 32);
    }

    private void drawSelectedTower(Graphics g) {
        if (selectedTower != null)
            g.drawImage(towerManager.getTowerImgs()[selectedTower.getTowerType()], mouseX, mouseY, null);
    }

    private void drawLevel(Graphics g) {
        for (int y = 0; y < lvl.length; y++)
            for (int x = 0; x < lvl[y].length; x++) {
                int id = lvl[y][x];
                if (isAnimation(id)) {
                    g.drawImage(getSprite(id, animationIndex), x * 32, y * 32, null);
                } else {
                    g.drawImage(getSprite(id), x * 32, y * 32, null);
                }
            }
    }

    private void removeGold(int towerType) {
        actionBar.payForTower(towerType);
    }

    public void removeTower(Tower displayedTower) {
        towerManager.removeTower(displayedTower);
    }

    public void upgradeTower(Tower displayedTower) {
        towerManager.upgradeTower(displayedTower);
    }

    private Tower getTowerAt(int x, int y) {
        return towerManager.getTowerAt(x, y);
    }

    private boolean isTileGrass(int x, int y) {
        int id = lvl[y / 32][x / 32];
        int tileType = getGame().getTileManager().getTile(id).getTileType();

        return tileType == GRASS_TILE;
    }

    public void shootEnemy(Tower t, Enemy e) {
        projectileManager.newProjectile(t, e);
    }

    public void rewardPlayer(int enemyType) {
        actionBar.addGold(Constants.Enemies.getAward(enemyType));
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            selectedTower = null;
            actionBar.setDisplayedTower(null);
        }
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (y >= 640)
            actionBar.mouseClicked(x, y);
        else {
            if (selectedTower != null) {
                if (isTileGrass(mouseX, mouseY)) {
                    if (getTowerAt(mouseX, mouseY) == null) {
                        towerManager.addTower(selectedTower, mouseX, mouseY);
                        removeGold(selectedTower.getTowerType());
                        selectedTower = null;
                    }
                }
            } else {
                Tower t = getTowerAt(mouseX, mouseY);
                actionBar.displayTower(t);
            }
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        if (y >= 640)
            actionBar.mouseMoved(x, y);
        else {
            mouseX = (x / 32) * 32;
            mouseY = (y / 32) * 32;
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (y >= 640) {
            actionBar.mousePressed(x, y);
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        actionBar.mouseReleased(x, y);
    }

    public TowerManager getTowerManager() {
        return towerManager;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }

    public void removeOneLife() {
        actionBar.removeOneLife();
    }

    public void resetAll() {
        actionBar.resetAll();

        enemyManager.reset();
        towerManager.reset();
        projectileManager.reset();
        waveManager.reset();

        mouseX = 0;
        mouseY = 0;

        selectedTower = null;
        goldTick = 0;
        gamePaused = false;
    }
}
