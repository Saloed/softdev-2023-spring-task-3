package Scenes;

import GameEngine.Game;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float w, float h, int[][] lvlData) {
        if (!Abroad(x, y, lvlData))
            if (!Abroad(x + w, y + h, lvlData))
                if (!Abroad(x + w, y, lvlData))
                    return !Abroad(x, y + h, lvlData);
        return false;
    }

    private static boolean Abroad(float x, float y, int[][] lvlData) {
        if (x < 0 || x >= Game.GameWidth) return true;
        if (y < 0 || y >= Game.GameHeight) return true;

        int xIndex = (int) (x / Game.TilesSizes);
        int yIndex = (int) (y / Game.TilesSizes);
        int value = lvlData[yIndex][xIndex];

        return value != 11;
    }
}
