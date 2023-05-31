package Levels;

import GameObjects.Money;
import GameObjects.Spike;

import static HelperClasses.Constants.Enemy.*;
import static HelperClasses.Constants.Objects.*;

import Sprites.Crab;
import Sprites.Octopus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static GameEngine.Game.*;

public class Level {
    private final BufferedImage image;
    private final int[][] lvlData;
    private final ArrayList<Crab> crabs = new ArrayList<>();
    private final ArrayList<Octopus> octopuses = new ArrayList<>();
    private final ArrayList<Money> moneys = new ArrayList<>();
    private final ArrayList<Spike> spikes = new ArrayList<>();
    private int maxTilesOffsetX;
    private Point spawnPoint;

    public Level(BufferedImage image) {
        this.image = image;
        lvlData = new int[image.getHeight()][image.getWidth()];
        loadLevel();
        lvlOffsets();

    }

    private void loadLevel() {
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                createLvlData(red, i, j);
                createEnemies(green, i, j);
                createObjects(blue, i, j);
            }
    }

    private void createLvlData(int red, int x, int y) {
        if (red >= 50) lvlData[y][x] = 0;
        else lvlData[y][x] = red;
    }

    private void createEnemies(int green, int x, int y) {
        switch (green) {
            case Crab:
                crabs.add(new Crab(x * TileSize, y * TileSize));
                break;
            case Octopus:
                octopuses.add(new Octopus(x * TileSize, y * TileSize));
                break;
            case 100:
                spawnPoint = new Point(x * TileSize, y * TileSize);
                break;
        }
    }

    private void createObjects(int blue, int x, int y) {
        switch (blue) {
            case Money:
                moneys.add(new Money(x * TileSize, y * TileSize, Money));
                break;
            case Spike:
                spikes.add(new Spike(x * TileSize, y * TileSize, Spike));
                break;
        }
    }

    private void lvlOffsets() {
        int lvlTiles = image.getWidth();
        int maxTilesOffset = lvlTiles - TilesInWidth; //total amount of tiles - visible amount
        maxTilesOffsetX = TileSize * maxTilesOffset;
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

    public ArrayList<Crab> getCrabs() {
        return crabs;
    }

    public ArrayList<Octopus> getOctopuses() {
        return octopuses;
    }

    public ArrayList<Money> getMoneys() {
        return moneys;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
}
