package org.helpers;

import org.main.IncorrectLevelStructure;
import org.objects.PathPoint;

import java.util.ArrayList;

import static org.helpers.Constants.Direction.*;
import static org.helpers.Constants.Tiles.*;
import static org.main.LangTexts.lvlWarningStr;

public class Utils {

    public static int[][] GetRoadDirArr(int[][] lvlTypeArr, PathPoint start, PathPoint end) {
        //Нужен, чтобы выстроить путь врагам ещё до того, как они появились
        //Перед этим, каждый противник отдельно проверял направление их пути каждый вызов метода update(),
        //что являлось затратным
        int[][] roadDirArr = new int[lvlTypeArr.length][lvlTypeArr[0].length];

        PathPoint currTile = start;
        int lastDir = -1;

        while (!isCurrTileSameAsEnd(currTile, end)) {
            PathPoint prevTile = currTile;
            currTile = GetNextRoadTile(prevTile, lastDir, lvlTypeArr);
            lastDir = GetDirFromPrevToCurr(prevTile, currTile);

            roadDirArr[prevTile.getyCord()][prevTile.getxCord()] = lastDir;
        }
        roadDirArr[end.getyCord()][end.getxCord()] = lastDir;

        return roadDirArr;
    }

    private static int GetDirFromPrevToCurr(PathPoint prevTile, PathPoint currTile) {
        //Сравнивает два тайла с помощью их координат, и возвращает направление от первого ко второму
        //Вверх/Вниз
        if (prevTile.getxCord() == currTile.getxCord()) {
            if (prevTile.getyCord() > currTile.getyCord())
                return UP;
            else return DOWN;
        } else {
            //Вправо/Влево
            if (prevTile.getxCord() > currTile.getxCord())
                return LEFT;
            else return RIGHT;
        }
    }

    private static PathPoint GetNextRoadTile(PathPoint prevTile, int lastDir, int[][] lvltypeArr) {
        //Проходит по всем (кроме противоположного текущему направлению), пока не найдёт тайл с дорогой
        //А если не найдёт (начало не соединено с концом), то выкидывает ошибку, которая в итоге пересоздаст уровень
        int testDir = lastDir;
        int counterCheck = 0;
        PathPoint testTile = GetTileInDir(prevTile, testDir, lastDir);

        while (!IsTileRoad(testTile, lvltypeArr)) {
            testDir++;
            testDir %= 4;
            testTile = GetTileInDir(prevTile, testDir, lastDir);

            counterCheck++;

            if (counterCheck > 4) {
                //Если были проверены все направления и дорогу так и не удалось найти
                //То выкидывает ошибку и пересоздаёт уровень
                throw new IncorrectLevelStructure(lvlWarningStr.toString());
            }
        }

        return testTile;
    }

    private static boolean IsTileRoad(PathPoint testTile, int[][] lvltypeArr) {
        if (testTile != null)
            if (testTile.getyCord() >= 0 && testTile.getyCord() < lvltypeArr.length)
                if (testTile.getxCord() >= 0 && testTile.getxCord() < lvltypeArr[0].length)
                    return lvltypeArr[testTile.getyCord()][testTile.getxCord()] == ROAD_TILE;
        return false;
    }

    private static PathPoint GetTileInDir(PathPoint prevTile, int testDir, int lastDir) {
        //Проходит по всем (кроме противоположного текущему направлению), и возвращает следующий тайл по направлению
        switch (testDir) {
            case LEFT -> {
                if (lastDir != RIGHT) return new PathPoint(prevTile.getxCord() - 1, prevTile.getyCord());
            }
            case UP -> {
                if (lastDir != DOWN) return new PathPoint(prevTile.getxCord(), prevTile.getyCord() - 1);
            }
            case RIGHT -> {
                if (lastDir != LEFT) return new PathPoint(prevTile.getxCord() + 1, prevTile.getyCord());
            }
            case DOWN -> {
                if (lastDir != UP) return new PathPoint(prevTile.getxCord(), prevTile.getyCord() + 1);
            }
        }
        return null;
    }

    private static boolean isCurrTileSameAsEnd(PathPoint currTile, PathPoint end) {
        //Проверяет, является ли текущая клетка конечной
        if (currTile.getxCord() == end.getxCord())
            return currTile.getyCord() == end.getyCord();
        return false;
    }

    public static int[][] ArrayListTo2Dint(ArrayList<Integer> list, int ySize, int xSize) {
        //Превращает список int в матрицу 20x20 (Для создания уровня)
        int[][] newArr = new int[ySize][xSize];

        for (int j = 0; j < newArr.length; j++) {
            for (int i = 0; i < newArr[j].length; i++) {
                int index = j * ySize + i;
                newArr[j][i] = list.get(index);
            }
        }

        return newArr;
    }

    public static int[] TwoDToIntArr(int[][] twoArr) {
        //Тоже самое, что и прошлый метод, но наоборот
        int[] oneArr = new int[twoArr.length * twoArr[0].length];
        for (int j = 0; j < twoArr.length; j++) {
            for (int i = 0; i < twoArr[j].length; i++) {
                int index = j * twoArr.length + i;
                oneArr[index] = twoArr[j][i];
            }
        }
        return oneArr;
    }

    public static int GetHypoDistance(float x1, float y1, float x2, float y2) {
        //Находит расстояние между двумя точками
        float xDiff = Math.abs(x1 - x2);
        float yDiff = Math.abs(y1 - y2);

        return (int) Math.hypot(xDiff, yDiff);
    }
}
