package HelperClasses;

import java.awt.geom.Rectangle2D;

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
        int maxWidth = lvlData[0].length * TileSize;
        if (x < 0 || x >= maxWidth) return true;
        if (y < 0 || y >= GameHeight) return true;

        int xIndex = (int) (x / TileSize);
        int yIndex = (int) (y / TileSize);

        return IsTileSolid(xIndex, yIndex, lvlData);
    }

    public static boolean IsInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (GetTile(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48)
            return GetTile(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) == 48;
        return true;
    }

    private static int GetTile(float xPos, float yPos, int[][] lvlData) {
        int x = (int) (xPos / TileSize);
        int y = (int) (yPos / TileSize);
        return lvlData[y][x];
    }

    public static boolean IsTileSolid(int tileX, int tileY, int[][] lvlData) {
        int value = lvlData[tileY][tileX];
        switch (value) {
            case 11:
            case 48:
            case 49:
                return false;
            default:
                return true;
        }
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
            if (IsTileSolid(xStart + i, y, lvlData))
                if (IsTileSolid(xStart + i, y - 1, lvlData)) return false;
        }
        return true;
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTileClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++)
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
        return true;
    }

    public static boolean IsWayClear(int[][] lvlData, Rectangle2D.Float anotherHitbox, Rectangle2D.Float mcHitbox, int yTile) {
        int anotherTileX = (int) (anotherHitbox.x / TileSize);

        int mcTileX;

        if (Abroad(mcHitbox.x, mcHitbox.y + mcHitbox.height + 1, lvlData))
            mcTileX = (int) (mcHitbox.x / TileSize);
        else
            mcTileX = (int) ((mcHitbox.x + mcHitbox.width) / TileSize);

        if (anotherTileX > mcTileX)
            return IsAllTilesWalkable(mcTileX, anotherTileX, yTile, lvlData);
        else
            return IsAllTilesWalkable(anotherTileX, mcTileX, yTile, lvlData);
    }
}
