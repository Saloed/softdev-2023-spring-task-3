package com.mygdx.game.states;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.IndieGame;
import com.mygdx.game.characters.Enemy;
import com.mygdx.game.characters.MainHero;
import com.mygdx.game.items.Swords;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayState extends State {
    public static float actionTime;
    public static float stateTime;
    // Переменные меню
    private final Texture menuBackground;
    private final Texture menuButtonTexture;
    private final Rectangle menuButton;
    public static boolean isMenuOpen;
    // Переменные диалогового окна
    public static boolean isDialog;
    protected int dialogNumber;
    // Boolean обучение
    public static boolean isLeftPressed;
    public static boolean isRightPressed;
    public static boolean isUpPressed;
    public static boolean isDownPressed;
    public static boolean isTraining;

    private int enemyPlace;
    private boolean isEnemyOnWindow;
    // Инвентарь
    public static boolean isInventoryOpen;
    protected int itemNumber;
    protected List<String> swordsList;
    // Шрифт
    private final BitmapFont font;
    private final BitmapFont moneyFont;

    // Спрайты
    private final Texture enemyTexture;
    private final Texture inventoryMenuBackground;
    private final Texture inventoryBackgroundTexture;
    private final Texture inventoryTexture;
    private final Texture inventorySlotTexture;
    private final Pixmap progressBarTexture;
    private Texture progressBar;
    private Pixmap progressBarPart;
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
    private final Texture trainingInfo;
    private Pixmap partProgressBar;

    // Хранение спрайтов
    private List<Rectangle> wheatList;
    private List<Rectangle> houseList;
    private List<Rectangle> roadList;
    private List<Rectangle> grassList;
    private List<Rectangle> trees1List;
    private List<Rectangle> trees2List;
    public static int trainingCountKeyPressed;
    //    private List<Rectangle> trees3List;
    private Rectangle testRectangle;
    OrthographicCamera camera;
    private List<String> currentLevel;
    private Rectangle castle;
    private Rectangle quest;
    private Rectangle enemyRectangle;
    private Rectangle dialogRectangle;
    private MainHero hero;
    private Swords swords;
    private File firstLevel;
    private List<Vector2> questPositions;
    protected int storyLevel;

    public PlayState(GameStateManager stateManager, boolean isTraining, int storyLevel, MainHero hero) {
        super(stateManager);
        camera = new OrthographicCamera();

        questPositions = new ArrayList<>();
        questPositions.add(new Vector2(136, 153));
        questPositions.add(new Vector2(51, 34));
        isInventoryOpen = false;
        this.storyLevel = storyLevel;
        isDialog = true;
        this.isTraining = isTraining;
        isMenuOpen = false;
        isEnemyOnWindow = false;
        enemyPlace = 0;
        font = new BitmapFont();
        font.setColor(0, 0, 0, 1);
        moneyFont = new BitmapFont();
        moneyFont.setColor(0, 0, 0, 1);
        moneyFont.getData().setScale(0.6f, 0.6f);

        actionTime = 0f;
        stateTime = 0f;
        enemyTexture = new Texture("PlayWindow/slime.png");
        menuBackground = new Texture("playWindow/menuBackground.png");
        menuButtonTexture = new Texture("playWindow/menuButton.png");
        inventoryMenuBackground = new Texture("playWindow/inventoryMenuBackground.png");
        inventoryBackgroundTexture = new Texture("playWindow/inventoryBackground.jpeg");
        inventoryTexture = new Texture("playWindow/inventory.png");
        inventorySlotTexture = new Texture("playWindow/inventorySlot.png");
        progressBarTexture = (new Pixmap(Gdx.files.getFileHandle("playWindow/progressBar.png",
                Files.FileType.Internal)));
        progressBarPart = progressBarTexture;
        progressBar = new Texture(progressBarPart, Pixmap.Format.RGB888, false);
        trainingInfo = new Texture("playWindow/trainingWindow.png");
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
        firstLevel = new File("playWindow/firstLevel.txt");

        dialogRectangle = new Rectangle();
        roadList = new ArrayList<>();
        wheatList = new ArrayList<>();
        houseList = new ArrayList<>();
        grassList = new ArrayList<>();
        trees1List = new ArrayList<>();
        trees2List = new ArrayList<>();
        castle = new Rectangle();
        quest = new Rectangle();
        quest.x = questPositions.get(storyLevel).x;
        quest.y = questPositions.get(storyLevel).y;
        quest.height = 16;
        quest.width = 16;
        enemyRectangle = new Rectangle();
        enemyRectangle.width = 16;
        enemyRectangle.height = 16;
        menuButton = new Rectangle();
        menuButton.x = IndieGame.WIDTH / 4 - 20;
        menuButton.y = IndieGame.HEIGHT / 4 - 50;
        menuButton.height = 32;
        menuButton.width = 32;
        testRectangle = new Rectangle();
        currentLevel = new ArrayList<>();
        trainingCountKeyPressed = 0;
        isLeftPressed = false;
        isRightPressed = false;
        isUpPressed = false;
        isDownPressed = false;


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
        int countString = 0;
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
            }
            countString++;
        }
        dialogRectangle.x = IndieGame.WIDTH / 4 - 144;
        dialogRectangle.y = 20;
        dialogRectangle.width = 288;
        dialogRectangle.height = 96;

        if (storyLevel == 0) dialogNumber = 1;
        if (storyLevel == 1) dialogNumber = 2;

        this.hero = hero;
        this.swords = new Swords(hero.getSwordName());
        swordsList = hero.getSwordsNames();
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
                hero.setPosition((int) oldX, (int) oldY);
            }
        }
        for (Rectangle elem : houseList) {
            if (hero.getHitBox().overlaps(elem)) {
                hero.setPosition((int) oldX, (int) oldY);
            }
        }
        for (Rectangle elem : trees2List) {
            if (hero.getHitBox().overlaps(elem)) {
                hero.setPosition((int) oldX, (int) oldY);
            }
        }
        if (isDialog) {
            if (Gdx.input.justTouched() && dialogRectangle.contains(Gdx.input.getX() / 2,
                    (IndieGame.HEIGHT - Gdx.input.getY()) / 2)) {
                isDialog = false;
            }
        }
        if (hero.getHitBox().overlaps(quest)) {
            if (storyLevel == 0)
                stateManager.set(new FightState(stateManager, hero,
                        new Enemy("slime"), true, storyLevel));
            if (storyLevel == 1) dialogNumber++;
        }
        if (hero.getHitBox().overlaps(enemyRectangle)){
            hero.setHealth(hero.getHealthMax());
            stateManager.set(new FightState(stateManager,hero,
                    new Enemy("slime"),false,storyLevel));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && actionTime + 1.5 < stateTime
                && !isInventoryOpen && !isDialog) {
            actionTime = stateTime;
            isMenuOpen = !isMenuOpen;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I) && actionTime + 1.5 < stateTime && !isMenuOpen && !isDialog) {
            actionTime = stateTime;
            isInventoryOpen = !isInventoryOpen;
        }
        if (isMenuOpen) if (Gdx.input.justTouched() &&
                menuButton.contains(Gdx.input.getX() / 2,
                        (IndieGame.HEIGHT - Gdx.input.getY()) / 2)) Gdx.app.exit();
        if (!isEnemyOnWindow && !isTraining){
            enemyPlace = (int) (Math.random() * roadList.size());
            enemyRectangle.x = roadList.get(enemyPlace).x;
            enemyRectangle.y = roadList.get(enemyPlace).y;
            isEnemyOnWindow = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        stateTime += Gdx.graphics.getDeltaTime();
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
            if (dialogNumber == 1) {
                sb.draw(dialogBackground, dialogRectangle.x, dialogRectangle.y);
                font.draw(sb, "Today is a great day to go chop wood\nin the forest", dialogRectangle.x + 14,
                        dialogRectangle.y + 90);
                font.draw(sb, "Click to start", dialogRectangle.x + 34, dialogRectangle.y + 20);
            }
            if (dialogNumber == 2) {
                sb.draw(dialogBackground, dialogRectangle.x, dialogRectangle.y);
                font.draw(sb, "Where did the slime come from in the\n forest. I need to tell the king about this",
                        dialogRectangle.x + 14, dialogRectangle.y + 90);
                font.draw(sb, "Click to continue", dialogRectangle.x + 34, dialogRectangle.y + 20);
            }
        }
        // Отрисовка обучения
        if (isTraining && !isDialog) {
            if (trainingCountKeyPressed < 4) {
                sb.draw(trainingInfo, 1, IndieGame.HEIGHT / 2 - 70);
                sb.draw(progressBar, 10, IndieGame.HEIGHT / 2 - 60);
                partProgressBar = new Pixmap(120, 26, Pixmap.Format.RGB888);
                partProgressBar.drawPixmap(progressBarTexture, 0, 0, 0, 0,
                        120 / 4 * (4 - trainingCountKeyPressed), 26);
                progressBar = new Texture(partProgressBar, Pixmap.Format.RGB888, false);
            }
        }
        // Отрисовка денег
        if (!isTraining) {
            font.draw(sb, "money:" + hero.getMoney(), 1, IndieGame.HEIGHT / 2 - 10);
        }
        // Отрисовка места квеста
        sb.draw(roadImage, quest.x, quest.y);
        sb.draw(questImage, quest.x, quest.y);
        // Отрисовка замка
        sb.draw(castleImage, castle.x, castle.y);
        // Отрисовка героя
        sb.draw(hero.getTexture(), hero.getPosition().x, hero.getPosition().y);
        // Отрисовка меню
        if (isMenuOpen) {
            sb.draw(menuBackground, IndieGame.WIDTH / 4 - 76, 150);
            font.draw(sb, "MENU", IndieGame.WIDTH / 4 - 25, 240);
            sb.draw(menuButtonTexture, menuButton.x, menuButton.y);
            moneyFont.draw(sb, "Exit", menuButton.x + 5, menuButton.y + 20);
        }
        // Отрисовка инвентаря
        if (isInventoryOpen) {
            itemNumber = 0;
            sb.draw(inventoryMenuBackground, IndieGame.WIDTH / 4 - 224, 266);
            sb.draw(inventoryTexture, IndieGame.WIDTH / 4 - 224, 266);
            sb.draw(inventoryBackgroundTexture, IndieGame.WIDTH / 4 - 160, 10);
            for (int raw = 0; raw < 5; raw++) {
                for (int symbol = 0; symbol < 5; symbol++) {
                    sb.draw(inventorySlotTexture, IndieGame.WIDTH / 4 - 160 + 64 * symbol, 10 + 64 * raw);
                    if (itemNumber < swordsList.size()) {
                        sb.draw(swords.getSwordTexture(hero.getSwordName()), IndieGame.WIDTH / 4 - 160 + 64
                                        * symbol + 32 - swords.getSwordTexture(hero.getSwordName()).getWidth() / 2,
                                10 + 64 * raw + 32 - swords.getSwordTexture(hero.getSwordName()).getHeight() / 2);
                        itemNumber++;
                    }
                }
            }
        }
        // Отрисовка Врага
        if (isEnemyOnWindow){
            sb.draw(enemyTexture,enemyRectangle.x,enemyRectangle.y);
        }
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