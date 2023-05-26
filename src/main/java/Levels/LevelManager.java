package Levels;

import GameEngine.Game;
import GameStates.GameState;
import GameStates.Playing;
import Scenes.DataProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static GameEngine.Game.TileSize;

public class LevelManager {
    private final Game game;
    private BufferedImage[] levelImage;
    private final ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importImageForLevels();
        levels = new ArrayList<>();
        createAllLevels();
    }

    private void createAllLevels() {
        BufferedImage[] allLevels = DataProcessing.GetAllLevels();
        for (BufferedImage i : allLevels)
            levels.add(new Level(i));
    }

    public void loadNextLvl() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0; //game completed and all lvls replay
            GameState.gameState = GameState.MENU;
        }
        if (lvlIndex == 1) game.getPlaying().backgroundImage = DataProcessing.GetSprite(DataProcessing.PlayingBackgroundNight);
        Level nextLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(nextLevel);
        game.getPlaying().getMainCharacter().lvlData(nextLevel.getLevelData());
        game.getPlaying().setMaxTilesOffsetX(nextLevel.getLvlOffset());
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

    public void draw(Graphics g, int xLvlOffset) {
        for (int j = 0; j < Game.TilesInHeight; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getIndex(i, j);
                g.drawImage(levelImage[index], i * TileSize - xLvlOffset, j * TileSize, TileSize, TileSize, null);
            }
        }
    }

    public Level getLevel() {
        return levels.get(lvlIndex);
    }

    public void update() {
    }

    /*public int getAmountOfLevels() {
        return levels.size();
    }*/
}
