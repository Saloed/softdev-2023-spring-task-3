package org.helpers;

import org.main.Game;
import org.main.LangStates;
import org.objects.PathPoint;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static org.main.LangStates.*;

public class LoadSave {

    public static String homePath = System.getProperty("user.home");
    public static String saveFolder = "TDSaveFiles";
    public static String levelFileName = "level.txt";
    public static String langFileName = "language.txt";
    public static String filePath = homePath + File.separator + saveFolder + File.separator;
    public static File lvlFile = new File(filePath + levelFileName);
    public static File langFile = new File(filePath + langFileName);


    public static void createFolder() {
        File folder = new File(homePath + File.separator + saveFolder);
        if (!folder.exists())
            folder.mkdir();
    }

    public static BufferedImage getSpriteAtlas() {
        // Достаёт из папки resources png со всеми спрайтами
        BufferedImage img = null;
        InputStream is = LoadSave.class.getClassLoader().getResourceAsStream("spriteatlas.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }


    public static void createLevel(int[] idArr) {
        //Создаёт новый уровень с названием. Если уже существует, то ничего не делает
        if (!lvlFile.exists()) {
            try {
                lvlFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeToFile(idArr, new PathPoint(0, 11), new PathPoint(19, 11));
        }
    }

    public static void resetLevel() {
        lvlFile.delete();
        Game.createDefaultLevel();
    }

    private static void writeToFile(int[] idArr, PathPoint start, PathPoint end) {
        //Записывает в файл ID каждой клетки, а также позицию появляения врагов и позицию базы
        // (4 значения в конце, для каждой координаты)
        try {
            PrintWriter pw = new PrintWriter(lvlFile);

            for (int i : idArr)
                pw.println(i);
            pw.println(start.getxCord());
            pw.println(start.getyCord());
            pw.println(end.getxCord());
            pw.println(end.getyCord());

            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveLevel(int[][] idArr, PathPoint start, PathPoint end) {
        //Сохраняет уровень при нажатии на кнопку "Сохранить"
        if (lvlFile.exists())
            writeToFile(Utils.twoDToIntArr(idArr), start, end);
        else
            System.out.println("Файла не существует.");
    }

    private static List<Integer> readFromFile() {
        //Считывает информацию из txt файла уровня
        List<Integer> list = new ArrayList<>();
        try {
            Scanner sc = new Scanner(lvlFile);

            while (sc.hasNextLine()) {
                list.add(Integer.parseInt(sc.nextLine()));
            }

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<PathPoint> getLevelPathPoints() {
        //Находит координаты появления врагов и базы
        if (lvlFile.exists()) {
            ArrayList<Integer> list = new ArrayList<>(readFromFile());
            List<PathPoint> points = new ArrayList<>();
            points.add(new PathPoint(list.get(400), list.get(401)));
            points.add(new PathPoint(list.get(402), list.get(403)));

            return points;
        } else {
            System.out.println("Файла не существует");
            return null;
        }
    }

    public static int[][] getLevelData() {
        //Считывает данные каждой клетки из txt файла

        if (lvlFile.exists()) {
            ArrayList<Integer> list = new ArrayList<>(readFromFile());
            return Utils.arrayListTo2Dint(list, 20, 20);
        } else {
            System.out.println("Файла не существует");
            return null;
        }
    }

    public static void createLanguageFile() {
        //Создаёт файл для записи языка
        if (!langFile.exists()) {
            try {
                PrintWriter pw = new PrintWriter(langFile);

                pw.println("RUSSIAN");

                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else getLanguageFromFile();
    }

    public static void setLanguage(LangStates lang) {
        //Записывает язык в файл
        try {
            new PrintWriter(langFile).close();
            PrintWriter pw = new PrintWriter(langFile);
            switch (lang) {
                case RUSSIAN -> pw.println("RUSSIAN");
                case ENGLISH -> pw.println("ENGLISH");
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getLanguageFromFile();
    }

    public static void getLanguageFromFile() {
        //Достаёт язык из файла и меняет значение langState
        try {
            Scanner sc = new Scanner(langFile);
            if (!sc.hasNext()) {
                setLanguage(RUSSIAN);
                sc.close();
                return;
            }
            String lang = sc.nextLine();
            if (Objects.equals(lang, "RUSSIAN"))
                SetLangState(RUSSIAN);
            else if (Objects.equals(lang, "ENGLISH"))
                SetLangState(ENGLISH);
            else
                setLanguage(RUSSIAN); //Если в файле языка что-то непонятное

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
