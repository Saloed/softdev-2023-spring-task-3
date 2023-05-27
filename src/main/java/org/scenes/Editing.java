package org.scenes;

import org.helpers.LoadSave;
import org.main.Game;
import org.objects.PathPoint;
import org.objects.Tile;
import org.ui.ToolBar;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static org.helpers.Constants.Tiles.*;

public class Editing extends GameScene implements SceneMethods {

    private int[][] lvl;
    private Tile selectedTile;
    private int mouseX, mouseY, lastTileX, lastTileY, lastTileId;
    private boolean drawSelect;
    private PathPoint start, end;

    private ToolBar toolBar;

    public Editing(Game game) {
        super(game);

        drawSelect = false;

        loadDefaultLevel();
        toolBar = new ToolBar(0, 640, 640, 160, this);
    }

    public void loadDefaultLevel() {
        lvl = LoadSave.GetLevelData();
        ArrayList<PathPoint> points = LoadSave.GetLevelPathPoints();
        start = points.get(0);
        end = points.get(1);
        getGame().getPlaying().getEnemyManager().setStartAndEndPoints(start, end);
        getGame().getPlaying().setLevel(lvl, start, end);
    }

    public void update() {
        updateTick();
    }

    @Override
    public void render(Graphics g) {
        drawLevel(g);
        toolBar.draw(g);
        drawSelectedTile(g);
        drawPathPoints(g);
    }

    private void drawPathPoints(Graphics g) {
        //Рисует клетку появления врагов и базу
        if (start != null) {
            g.drawImage(toolBar.getPathStartImg(), start.getxCord() * 32, start.getyCord() * 32, null);
        }
        if (end != null) {
            g.drawImage(toolBar.getPathEndImg(), end.getxCord() * 32, end.getyCord() * 32, null);
        }
    }

    private void drawLevel(Graphics g) {
        //Рисует уровень в зависимости от id клетки в сохранённом файле
        for (int y = 0; y < lvl.length; y++) {
            for (int x = 0; x < lvl[y].length; x++) {
                int id = lvl[y][x];
                if (isAnimation(id)) {
                    g.drawImage(getSprite(id, animationIndex), x * 32, y * 32, null);
                } else {
                    g.drawImage(getSprite(id), x * 32, y * 32, null);
                }
            }
        }
    }

    private void drawSelectedTile(Graphics g) {
        //Рисует выбранную клетку на самом поле, но убирает её при перемещении курсора
        if (selectedTile != null && drawSelect) {
            g.drawImage(selectedTile.getSprite(), mouseX, mouseY, 32, 32, null);
        }
    }

    public void saveLevel() {
        //При нажатии кнопки сохранения уровень сохраняется
        LoadSave.SaveLevel(lvl, start, end);
        getGame().getPlaying().setLevel(lvl, start, end);
        getGame().getPlaying().resetAll();
        getGame().getPlaying().getEnemyManager().loadRoadDirArr();
        loadDefaultLevel();
    }

    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
        drawSelect = true;
    }

    public void changeTile(int x, int y) {
        //Ставит выбранную клетку на поле уровня
        if (selectedTile != null) {
            int tileX = x / 32;
            int tileY = y / 32;
            if (selectedTile.getId() >= 0) {

                if (lastTileX == tileX && lastTileY == tileY
                        && lastTileId == selectedTile.getId()) return;

                lastTileX = tileX;
                lastTileY = tileY;
                lastTileId = selectedTile.getId();

                lvl[tileY][tileX] = selectedTile.getId();
            } else {
                int id = lvl[tileY][tileX];
                if (getGame().getTileManager().getTile(id).getTileType() == ROAD_TILE) {
                    if (selectedTile.getId() == -1) {
                        start = new PathPoint(tileX, tileY);
                    } else
                        end = new PathPoint(tileX, tileY);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (y >= 640) {
            toolBar.mouseClicked(x, y);
        } else {
            changeTile(mouseX, mouseY);
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        if (y >= 640) {
            toolBar.mouseMoved(x, y);
            drawSelect = false;
        } else {
            drawSelect = true;
            mouseX = (x / 32) * 32;
            mouseY = (y / 32) * 32;
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (y >= 640)
            toolBar.mousePressed(x, y);
    }

    @Override
    public void mouseReleased(int x, int y) {
        toolBar.mouseReleased(x, y);
    }

    public void mouseDragged(int x, int y) {
        if (y > 0 && y < 640 && x > 0 && x < 640) {
            changeTile(x, y);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            toolBar.rotateSprite();
        }
    }

    public ToolBar getToolBar() {
        return toolBar;
    }
}
