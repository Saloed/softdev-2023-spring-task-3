package Levels;

import GameEngine.Game;
import GameStates.Playing;
import HelperClasses.DataProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static GameEngine.Game.TileSize;

public class LevelManager {
    private final Playing playing;
    private BufferedImage[] levelImage;
    private BufferedImage[] water;
    public ArrayList<Level> levels;
    public int lvlIndex = 0, animationIndex, animationTick;

    public LevelManager(Playing playing) {
        this.playing = playing;
        importImageForLevels();
        createWater();
        levels = new ArrayList<>();
        createAllLevels();
    }


    private void createAllLevels() {
        BufferedImage[] allLevels = DataProcessing.GetAllLevels();
        for (BufferedImage i : allLevels)
            levels.add(new Level(i));
    }

    public void loadNextLvl() {
        if (lvlIndex == 1) playing.backgroundImage = DataProcessing.GetSprite(DataProcessing.PlayingBackgroundNight);
        else playing.backgroundImage = DataProcessing.GetSprite(DataProcessing.PlayingBackgroundDay);
        Level nextLevel = levels.get(lvlIndex);
        playing.getMainCharacter().lvlData(nextLevel.getLevelData());
        playing.getMainCharacter().setMaxMoneyWidth(nextLevel);
        playing.setMaxTilesOffsetX(nextLevel.getLvlOffset());
        playing.getEnemyManager().loadEnemies(nextLevel);
        playing.getObjectManager().loadObjects(nextLevel);
    }

    private void importImageForLevels() {
        BufferedImage image = DataProcessing.GetSprite(DataProcessing.ForLevels);
        levelImage = new BufferedImage[48];
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelImage[index] = image.getSubimage(i * 32, j * 32, 32, 32);
            }
    }

    private void createWater() {
        BufferedImage image = DataProcessing.GetSprite(DataProcessing.WaterAnimation);
        water = new BufferedImage[5];
        for (int i = 0; i < 4; i++)
            water[i] = image.getSubimage(i * 32, 0, 32, 32);
        water[4] = DataProcessing.GetSprite(DataProcessing.Water);
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (int j = 0; j < Game.TilesInHeight; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getIndex(i, j);
                int x = TileSize * i - xLvlOffset;
                int y = TileSize * j;
                if (index == 48)
                    g.drawImage(water[animationIndex], x, y, TileSize, TileSize, null);
                else if (index == 49)
                    g.drawImage(water[4], x, y, TileSize, TileSize, null);
                else
                    g.drawImage(levelImage[index], x, y, TileSize, TileSize, null);
            }
        }
    }

    public Level getLevel() {
        return levels.get(lvlIndex);
    }

    public int getLvlIndex() {
        return lvlIndex;
    }

    public void setLvlIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }

    public void update() {
        updateWater();
    }

    private void updateWater() {
        animationTick++;
        if (animationTick >= 50) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 4)
                animationIndex = 0;
        }
    }

    public int getAmountOfLevels() {
        return levels.size();
    }
}
