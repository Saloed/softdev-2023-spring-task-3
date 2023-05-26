package Scenes;

import Sprites.Constants;
import Sprites.Octopus;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static GameEngine.Game.*;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float w, float h, int[][] lvlData) {
        if (!Abroad(x, y, lvlData))
            if (!Abroad(x + w, y + h, lvlData))
                if (!Abroad(x + w, y, lvlData))
                    return !Abroad(x, y + h, lvlData);
        return false;
    }

    private static boolean Abroad(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * GameWidth;
        if (x < 0 || x >= maxWidth) return true;
        if (y < 0 || y >= GameHeight) return true;

        int xIndex = (int) (x / TileSize);
        int yIndex = (int) (y / TileSize);

        return IsTileSolid(xIndex, yIndex, lvlData);
    }

    public static boolean IsTileSolid(int tileX, int tileY, int[][] lvlData) {
        int value = lvlData[tileY][tileX];
        return value != 11;
    }

    public static float GetXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / TileSize);
        if (xSpeed > 0) {
            int tilePosX = currentTile * TileSize;
            int offsetX = (int) (TileSize - hitbox.width);
            return tilePosX + offsetX - 1;
        } else return currentTile * TileSize;
    }

    public static float GetYPos(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / TileSize);
        if (airSpeed > 0) {
            int tilePosY = currentTile * TileSize;
            int offsetY = (int) (TileSize - hitbox.height);
            return tilePosY + offsetY - 1;
        } else return currentTile * TileSize;
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!Abroad(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            return !Abroad(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        return false;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) return Abroad(hitbox.x + xSpeed + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        else return Abroad(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsAllTileClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(i + xStart, y, lvlData)) return false;
            if (!IsTileSolid(i + xStart, y + 1, lvlData)) return false;
        }
        return true;
    }

    public static boolean IsWayClear(int[][] lvlData, Rectangle2D.Float anotherHitbox, Rectangle2D.Float mcHitbox, int tileY) {
        int mcTileX = (int) (mcHitbox.x / TileSize);
        int anotherTileX = (int) (anotherHitbox.x / TileSize);
        if (anotherTileX > mcTileX)
            return IsAllTileClear(anotherTileX, mcTileX, tileY, lvlData);
        else
            return IsAllTileClear(mcTileX, anotherTileX, tileY, lvlData);
    }

    public static int[][] GetLevelData(BufferedImage image) {
        int[][] levelData = new int[image.getHeight()][image.getWidth()];
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48) value = 0;
                levelData[j][i] = value;
            }
        return levelData;
    }

    public static ArrayList<Octopus> GetOctopuses(BufferedImage image) {
        ArrayList<Octopus> list = new ArrayList<>();
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getGreen();
                if (value == Constants.Enemy.Octopus) list.add(new Octopus(i * TileSize, j * TileSize));
            }
        return list;
    }

    public static Point GetSpawnPoint(BufferedImage image) {
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100) return new Point(i * TileSize, j * TileSize);
            }
        return new Point(TileSize, TileSize);
    }
}
