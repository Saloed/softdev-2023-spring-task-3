package Levels;

import Sprites.Octopus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static GameEngine.Game.*;
import static Scenes.HelpMethods.*;

public class Level {
    private final BufferedImage image;
    private int[][] lvlData;
    private ArrayList<Octopus> octopuses;
    private int maxTilesOffsetX;
    private Point spawnPoint;

    public Level(BufferedImage image) {
        this.image = image;
        createLvlData();
        createEnemies();
        lvlOffsets();
        spawnPoint();
    }

    private void spawnPoint() {
        spawnPoint = GetSpawnPoint(image);
    }

    private void lvlOffsets() {
        int lvlTiles = image.getWidth();
        int maxTilesOffset = lvlTiles - TilesInWidth; //total amount of tiles - visible amount
        maxTilesOffsetX = TileSize * maxTilesOffset;
    }

    private void createEnemies() {
        octopuses = GetOctopuses(image);
    }

    private void createLvlData() {
        lvlData = GetLevelData(image);
    }

    public int getIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxTilesOffsetX;
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }

    public ArrayList<Octopus> getOctopuses() {
        return octopuses;
    }
}
