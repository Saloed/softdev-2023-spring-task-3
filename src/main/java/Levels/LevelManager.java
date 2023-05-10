package Levels;

import GameEngine.Game;
import Scenes.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static GameEngine.Game.TilesSizes;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelImage;
    private Level level1;

    public LevelManager(Game game) {
        this.game = game;
        importImageForLevels();
        level1 = new Level(LoadSave.GetLevelData());
    }

    private void importImageForLevels() {
        BufferedImage image = LoadSave.GetSprite(LoadSave.ForLevels);
        levelImage = new BufferedImage[48];
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelImage[index] = image.getSubimage(i * 32, j * 32, 32, 32);
            }
    }

    public void draw(Graphics g) {
        for (int j = 0; j < Game.TilesInHeight; j++) {
            for (int i = 0; i < Game.TilesInWidth; i++) {
                int index = level1.getIndex(i, j);
                g.drawImage(levelImage[index], i * TilesSizes, j * TilesSizes, TilesSizes, TilesSizes, null);
            }
        }
    }

    public Level getLevel() {
        return level1;
    }

    public void update() {
    }
}
