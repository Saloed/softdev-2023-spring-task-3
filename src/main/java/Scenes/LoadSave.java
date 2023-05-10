package Scenes;

import GameEngine.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {
    public static final String MainCharacter = "images/mainCharacter3.png";
    public static final String ForLevels = "images/forLevels.png";
    public static final String Level1 = "images/level_one_data.png";

    public static BufferedImage GetSprite(String fileName) {
        BufferedImage image = null;

        InputStream inputStream = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static int[][] GetLevelData() {
        int[][] levelData = new int[Game.TilesInHeight][Game.TilesInWidth];
        BufferedImage image = GetSprite(Level1);

        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48) value = 0;
                levelData[j][i] = value;
            }
        return levelData;
    }
}
