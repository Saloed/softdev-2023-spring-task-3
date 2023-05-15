package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.IndieGame;
import com.mygdx.game.characters.Enemy;
import com.mygdx.game.characters.MainHero;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayState extends State {
    // Boolean переменная диалогового окна
    boolean isDialog;
    // Шрифт
    private final BitmapFont font;
    // Спрайты
    private Texture dialogBackground;
    private final Texture roadImage;
    private final Texture grass1Image;
    private final Texture grass2Image;
    private final Texture wheatImage;
    private final Texture tree1Image;
    private final Texture tree2Image;
    private final Texture tree3Image;
    private final Texture castleImage;
    private final Texture houseImage;
    private final Texture questImage;
    // Хранение спрайтов
    private List<Rectangle> wheatList;
    private List<Rectangle> houseList;
    private List<Rectangle> roadList;
    private List<Rectangle> grassList;
    private List<Rectangle> trees1List;
    private List<Rectangle> trees2List;
    //    private List<Rectangle> trees3List;
    private Rectangle testRectangle;
    OrthographicCamera camera;
    private List<String> currentLevel;
    private Rectangle castle;
    private Rectangle quest;
    private Rectangle dialogRectangle;
    private MainHero hero;

    public PlayState(GameStateManager stateManager) {
        super(stateManager);
        camera = new OrthographicCamera();

        isDialog = true;
        font = new BitmapFont();
        font.setColor(0, 0, 0, 1);

        dialogBackground = new Texture("playWindow/dialogBackground.png");
        roadImage = new Texture("playWindow/road.png");
        grass1Image = new Texture("playWindow/grass1.png");
        grass2Image = new Texture("playWindow/grass2.png");
        tree1Image = new Texture("playWindow/tree1.png");
        tree2Image = new Texture("playWindow/tree2.png");
        tree3Image = new Texture("playWindow/tree3.png");
        castleImage = new Texture("playWindow/castle.png");
        houseImage = new Texture("playWindow/house.png");
        wheatImage = new Texture("playWindow/wheatField.png");
        questImage = new Texture("playWindow/quest.png");
        File firstLevel = new File("assets/playWindow/firstLevel.txt");

        dialogRectangle = new Rectangle();
        roadList = new ArrayList<>();
        wheatList = new ArrayList<>();
        houseList = new ArrayList<>();
        grassList = new ArrayList<>();
        trees1List = new ArrayList<>();
        trees2List = new ArrayList<>();
//        trees3List = new ArrayList<>();
        castle = new Rectangle();
        quest = new Rectangle();
        testRectangle = new Rectangle();
        currentLevel = new ArrayList<>();

        // Считываю уровень
        try {
            Scanner scanner = new Scanner(firstLevel);
            while (scanner.hasNextLine()) {
                currentLevel.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Записываю координаты текстур
        Integer countString = 0;
        for (String raw : currentLevel) {
            for (int symbolNumber = 0; symbolNumber < raw.length(); symbolNumber++) {
                if (raw.charAt(symbolNumber) == '0') {
                    testRectangle.x = symbolNumber * 16 + symbolNumber;
                    testRectangle.y = countString * 16 + countString;
                    testRectangle.width = 16;
                    testRectangle.height = 16;
                    grassList.add(new Rectangle(testRectangle));
                }
                if (raw.charAt(symbolNumber) == '1') {
                    testRectangle.x = symbolNumber * 16 + symbolNumber;
                    testRectangle.y = countString * 16 + countString;
                    testRectangle.width = 16;
                    testRectangle.height = 16;
                    trees1List.add(new Rectangle(testRectangle));
                }
                if (raw.charAt((symbolNumber)) == '2') {
                    castle.x = symbolNumber * 16 + symbolNumber;
                    castle.y = countString * 16 + countString;
                }
                if (raw.charAt(symbolNumber) == '3') {
                    testRectangle.x = symbolNumber * 16 + symbolNumber;
                    testRectangle.y = countString * 16 + countString;
                    testRectangle.width = 16;
                    testRectangle.height = 16;
                    houseList.add(new Rectangle(testRectangle));
                }
                if (raw.charAt(symbolNumber) == '4') {
                    testRectangle.x = symbolNumber * 16 + symbolNumber;
                    testRectangle.y = countString * 16 + countString;
                    testRectangle.width = 16;
                    testRectangle.height = 16;
                    roadList.add(new Rectangle(testRectangle));
                }
                if (raw.charAt(symbolNumber) == '5') {
                    testRectangle.x = symbolNumber * 16 + symbolNumber;
                    testRectangle.y = countString * 16 + countString;
                    testRectangle.width = 16;
                    testRectangle.height = 16;
                    trees2List.add(new Rectangle(testRectangle));
                }
                if (raw.charAt(symbolNumber) == '6') {
                    testRectangle.x = symbolNumber * 16 + symbolNumber;
                    testRectangle.y = countString * 16 + countString;
                    testRectangle.width = 16;
                    testRectangle.height = 16;
                    wheatList.add(new Rectangle(testRectangle));
                }
                if (raw.charAt((symbolNumber)) == '9') {
                    quest.x = symbolNumber * 16 + symbolNumber;
                    quest.y = countString * 16 + countString;
                }
            }
            countString++;
        }
        dialogRectangle.x = IndieGame.WIDTH / 4 - 144;
        dialogRectangle.y = 20;
        dialogRectangle.width = 288;
        dialogRectangle.height = 96;
        hero = new MainHero(50, 50, 100, 10);
        camera.setToOrtho(false, IndieGame.WIDTH / 2, IndieGame.HEIGHT / 2);
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        handleInput();
        float oldX = hero.getPosition().x;
        float oldY = hero.getPosition().y;
        hero.update();
        //проверка на коллизии
        for (Rectangle elem : trees1List) {
            if (hero.getHitBox().overlaps(elem)) {
                hero = new MainHero((int) oldX, (int) oldY, hero.getHealth(), hero.getAttack());
            }
        }
        for (Rectangle elem : houseList) {
            if (hero.getHitBox().overlaps(elem)) {
                hero = new MainHero((int) oldX, (int) oldY, hero.getHealth(), hero.getAttack());
            }
        }
        for (Rectangle elem : trees2List) {
            if (hero.getHitBox().overlaps(elem)) {
                hero = new MainHero((int) oldX, (int) oldY, hero.getHealth(), hero.getAttack());
            }
        }
        if (Gdx.input.justTouched() && dialogRectangle.contains(Gdx.input.getX() / 2,
                (IndieGame.HEIGHT - Gdx.input.getY()) / 2)) isDialog = false;
        if (hero.getHitBox().overlaps(quest)) {
            stateManager.set(new FightState(stateManager, hero, new Enemy("slime")));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        // Отрисовка травы
        for (Rectangle elem : grassList) {
            sb.draw(grass1Image, elem.x, elem.y);
        }
        for (Rectangle elem : wheatList) {
            sb.draw(grass1Image, elem.x, elem.y);
            sb.draw(wheatImage, elem.x, elem.y);
        }
        // Отрисовка деревьев
        for (Rectangle elem : trees1List) {
            sb.draw(grass2Image, elem.x, elem.y);
            sb.draw(tree1Image, elem.x, elem.y);
        }
        for (Rectangle elem : trees2List) {
            sb.draw(grass2Image, elem.x, elem.y);
            sb.draw(tree2Image, elem.x, elem.y);
        }
        // Отрисовка домов
        for (Rectangle elem : houseList) {
            sb.draw(roadImage, elem.x, elem.y);
            sb.draw(houseImage, elem.x, elem.y);
        }
        // Отрисовка дороги
        for (Rectangle elem : roadList) {
            sb.draw(roadImage, elem.x, elem.y);
        }
        // Отрисовка замка
        for (int raw = 0; raw < 2; raw++) {
            for (int number = 0; number < 2; number++) {
                sb.draw(grass1Image, castle.x + number, castle.y + raw);
            }
        }
        // Отрисовка диалогового окна
        if (isDialog) {
            sb.draw(dialogBackground, dialogRectangle.x, dialogRectangle.y);
            font.draw(sb, "Today is a great day to go chop wood\nin the forest", dialogRectangle.x + 14, dialogRectangle.y + 70);
        }
        // Отрисовка места квеста
        sb.draw(roadImage, quest.x, quest.y);
        sb.draw(questImage, quest.x, quest.y);
        // Отрисовка замка
        sb.draw(castleImage, castle.x, castle.y);
        // Отрисовка героя
        sb.draw(hero.getTexture(), hero.getPosition().x, hero.getPosition().y);
        sb.end();
    }

    @Override
    public void dispose() {
        grass1Image.dispose();
        grass2Image.dispose();
        wheatImage.dispose();
        tree1Image.dispose();
        tree2Image.dispose();
        tree3Image.dispose();
        castleImage.dispose();
        houseImage.dispose();
        questImage.dispose();
    }
}