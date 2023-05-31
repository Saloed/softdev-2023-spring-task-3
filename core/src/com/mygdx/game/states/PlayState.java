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
import com.mygdx.game.playStateActivities.ActionTime;
import com.mygdx.game.playStateActivities.Inventory;
import com.mygdx.game.playStateActivities.Menu;
import com.mygdx.game.playStateActivities.Shop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class PlayState extends State {
//    public static float actionTime;
//    public static float stateTime;
//    public static float trainingTime;
    // const
    private static final int textureSize = 16, inventoryTextureSize = 64;
    // Переменные диалогового окна
    public static boolean isDialog;
    protected int dialogNumber;
    public static boolean isLeftPressed;
    public static boolean isRightPressed;
    public static boolean isUpPressed;
    public static boolean isDownPressed;
    public static boolean isTraining;

    private int enemyPlace;
    private boolean isEnemyOnWindow;
    // Инвентарь
    protected int itemNumber;
    protected List<String> swordsList;
    // Шрифт
    private final BitmapFont font;
    private final BitmapFont moneyFont;

    // Спрайты
    private final Texture enemyTexture;
    private final Pixmap progressBarTexture;
    private Texture progressBar;
    private final Texture dialogBackground;
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
    private final Texture trainingInfo1;
    private final Texture trainingInfo2;

    // Хранение спрайтов
    private final List<Rectangle> wheatList;
    private final List<Rectangle> houseList;
    private final List<Rectangle> roadList;
    private final List<Rectangle> grassList;
    private final List<Rectangle> trees1List;
    private final List<Rectangle> trees2List;
    public static int trainingCountKeyPressed;
    //    private List<Rectangle> trees3List;
    private final Rectangle testRectangle;
    OrthographicCamera camera;
    private final Rectangle castle;
    private final Rectangle quest;
    private final Rectangle enemyRectangle;
    private final Rectangle dialogRectangle;
    private final MainHero hero;
    private final Swords swords;
    private final List<Vector2> questPositions;
    protected int storyLevel;
    private final Inventory inventory;
    private final Menu menu;
    private final Shop shop;
    private final ActionTime time;


    public PlayState(GameStateManager stateManager, boolean isTraining, int storyLevel,
                     MainHero hero, Shop shop, boolean isEnemyOnWindow) {
        super(stateManager);
        camera = new OrthographicCamera();
        questPositions = new ArrayList<>();
        questPositions.add(new Vector2(136, 153));
        questPositions.add(new Vector2(51, 34));
        questPositions.add(new Vector2(444, 444));
        questPositions.add(new Vector2(289, 289));
        questPositions.add(new Vector2(444, 444));
        questPositions.add(new Vector2(444, 444));
        this.storyLevel = storyLevel;
        isDialog = storyLevel == 0 || storyLevel == 1;
        PlayState.isTraining = isTraining;
        if (storyLevel == 5) PlayState.isTraining = false;
        menu = new Menu();
        this.isEnemyOnWindow = isEnemyOnWindow;
        enemyPlace = 0;
        font = new BitmapFont();
        font.setColor(0, 0, 0, 1);
        moneyFont = new BitmapFont();
        moneyFont.setColor(0, 0, 0, 1);
        moneyFont.getData().setScale(0.6f, 0.6f);

        this.shop = shop;
        time = new ActionTime();
        progressBarTexture = (new Pixmap(Gdx.files.getFileHandle("playWindow/progressBar.png",
                Files.FileType.Internal)));
        progressBar = new Texture(progressBarTexture, Pixmap.Format.RGB888, false);
        enemyTexture = new Texture("playWindow/slime.png");
        trainingInfo1 = new Texture("playWindow/trainingWindow1.png");
        trainingInfo2 = new Texture("playWindow/trainingWindow2.png");
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
        File firstLevel = new File("playWindow/firstLevel.txt");

        dialogRectangle = new Rectangle();
        roadList = new ArrayList<>();
        wheatList = new ArrayList<>();
        houseList = new ArrayList<>();
        grassList = new ArrayList<>();
        trees1List = new ArrayList<>();
        trees2List = new ArrayList<>();
        castle = new Rectangle();
        castle.width = textureSize * 2;
        castle.height = textureSize * 2;
        quest = new Rectangle();
        quest.x = questPositions.get(storyLevel).x;
        quest.y = questPositions.get(storyLevel).y;
        quest.height = textureSize;
        quest.width = textureSize;
        enemyRectangle = new Rectangle();
        enemyRectangle.width = textureSize;
        enemyRectangle.height = textureSize;
        testRectangle = new Rectangle();
        List<String> currentLevel = new ArrayList<>();
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
                    testRectangle.x = symbolNumber * textureSize + symbolNumber;
                    testRectangle.y = countString * textureSize + countString;
                    testRectangle.width = textureSize;
                    testRectangle.height = textureSize;
                    grassList.add(new Rectangle(testRectangle));
                }
                if (raw.charAt(symbolNumber) == '1') {
                    testRectangle.x = symbolNumber * textureSize + symbolNumber;
                    testRectangle.y = countString * textureSize + countString;
                    testRectangle.width = textureSize;
                    testRectangle.height = textureSize;
                    trees1List.add(new Rectangle(testRectangle));
                }
                if (raw.charAt((symbolNumber)) == '2') {
                    castle.x = symbolNumber * textureSize + symbolNumber;
                    castle.y = countString * textureSize + countString;
                }
                if (raw.charAt(symbolNumber) == '3') {
                    testRectangle.x = symbolNumber * textureSize + symbolNumber;
                    testRectangle.y = countString * textureSize + countString;
                    testRectangle.width = textureSize;
                    testRectangle.height = textureSize;
                    houseList.add(new Rectangle(testRectangle));
                }
                if (raw.charAt(symbolNumber) == '4') {
                    testRectangle.x = symbolNumber * textureSize + symbolNumber;
                    testRectangle.y = countString * textureSize + countString;
                    testRectangle.width = textureSize;
                    testRectangle.height = textureSize;
                    roadList.add(new Rectangle(testRectangle));
                }
                if (raw.charAt(symbolNumber) == '5') {
                    testRectangle.x = symbolNumber * textureSize + symbolNumber;
                    testRectangle.y = countString * textureSize + countString;
                    testRectangle.width = textureSize;
                    testRectangle.height = textureSize;
                    trees2List.add(new Rectangle(testRectangle));
                }
                if (raw.charAt(symbolNumber) == '6') {
                    testRectangle.x = symbolNumber * textureSize + symbolNumber;
                    testRectangle.y = countString * textureSize + countString;
                    testRectangle.width = textureSize;
                    testRectangle.height = textureSize;
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
        testRectangle.height = inventoryTextureSize;
        testRectangle.width = inventoryTextureSize;
        this.hero = hero;
        hero.setMenu(menu);
        hero.setTime(time);
        hero.setShop(shop);
        inventory = hero.getInventory();
        this.swords = new Swords(inventory.getSwordName());
        swordsList = inventory.getSwordsNames();
        camera.setToOrtho(false, IndieGame.WIDTH / 2,
                IndieGame.HEIGHT / 2);
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
            if (hero.getHitBox().overlaps(elem)) hero.setPosition((int) oldX, (int) oldY);
        }
        for (Rectangle elem : houseList) {
            if (hero.getHitBox().overlaps(elem)) hero.setPosition((int) oldX, (int) oldY);
        }
        for (Rectangle elem : trees2List) {
            if (hero.getHitBox().overlaps(elem)) hero.setPosition((int) oldX, (int) oldY);
        }
        if (hero.getHitBox().overlaps(castle)) hero.setPosition((int) oldX, (int) oldY);
        if (isTraining && storyLevel == 1) {
            // Boolean обучение
            if (time.getTrainingTime() + 10 < time.getStateTime()) {
                isTraining = false;
            }
        }
        // Диалоговое окно(квесты)
        if (!isDialog && hero.getEnemyKills() > 20 && storyLevel == 2) {
            quest.setPosition(questPositions.get(1));
            storyLevel = 2;
        }
        if (isDialog) {
            if (Gdx.input.justTouched() && dialogRectangle.contains(Gdx.input.getX() / 2,
                    (IndieGame.HEIGHT - Gdx.input.getY()) / 2)) {
                dialogNumber++;
                isDialog = false;
            }
        }
        if (hero.getHitBox().overlaps(quest)) {
            if (storyLevel == 0)
                stateManager.set(new FightState(stateManager, hero,
                        new Enemy("slime"), true, storyLevel, inventory, shop));
            if (storyLevel == 1) {
                isTraining = false;
                isDialog = true;
                if (!inventory.getSwordsNames().contains("freeSword")) inventory.findSword("freeSword");
                quest.setPosition(questPositions.get(2));
                storyLevel = 5;
            }
            if (storyLevel == 3) {
                isTraining = false;
                stateManager.set(new FightState(stateManager, hero,
                        new Enemy("demon"), false, storyLevel, inventory, shop));
            }
            if (storyLevel == 2) {
                isDialog = true;
                dialogNumber = 4;
                quest.setPosition(questPositions.get(3));
                storyLevel = 3;
            }
        }
        if (hero.getHitBox().overlaps(enemyRectangle)) {
            hero.setHealth(hero.getHealthMax());
            stateManager.set(new FightState(stateManager, hero,
                    new Enemy("slime"), false, storyLevel, inventory, shop));
        }
        // Магазин
        if (Gdx.input.isKeyPressed(Input.Keys.B) && time.getActionTime() + 0.8 < time.getStateTime()
                && !inventory.isOpen() && !isDialog && !menu.isOpen()) {
            time.setActionTime(time.getStateTime());
            shop.changeOpen();
        }
        int selectedNumber;
        if (shop.isOpen()) {
            if (Gdx.input.justTouched()) {
                if (shop.isSelect() &&
                        shop.getBuyButton().contains(Gdx.input.getX() / 2,
                                (IndieGame.HEIGHT - Gdx.input.getY()) / 2) &&
                        hero.getMoney() >= shop.getPrices().get(shop.selectedNumber()) &&
                        !shop.getSoldNames().contains(shop.getProductsNames().get(shop.selectedNumber()))) {
                    hero.spendMoney(shop.getPrices().get(shop.selectedNumber()));
                    inventory.findSword(shop.getProductsNames().get(shop.selectedNumber()));
                    shop.productSold(shop.getProductsNames().get(shop.selectedNumber()));
                }
                selectedNumber = 0;
                for (int symbol = 0; symbol < 3; symbol++) {
                    testRectangle.x = IndieGame.WIDTH / 4 - 100 + inventoryTextureSize * symbol;
                    testRectangle.y = 100 + 96;
                    if (testRectangle.contains(Gdx.input.getX() / 2,
                            (IndieGame.HEIGHT - Gdx.input.getY()) / 2)) {
                        shop.trueSelect();
                        shop.setSelectedNumber(selectedNumber);
                    }
                    selectedNumber++;
                }
            }
        }

        // Меню
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && time.getActionTime() + 0.8 < time.getStateTime()
                && !inventory.isOpen() && !isDialog && !shop.isOpen()) {
            time.setActionTime(time.getStateTime());
            menu.changeOpen();
        }
        if (menu.isOpen()) if (Gdx.input.justTouched() &&
                menu.getMenuButton().contains(Gdx.input.getX() / 2,
                        (IndieGame.HEIGHT - Gdx.input.getY()) / 2)) Gdx.app.exit();
        // Инвентарь
        if (Gdx.input.isKeyPressed(Input.Keys.I) && time.getActionTime() + 0.8 < time.getStateTime()
                && !menu.isOpen() && !isDialog && !shop.isOpen()) {
            time.setActionTime(time.getStateTime());
            inventory.changeOpen();
        }
        if (inventory.isOpen()) {
            selectedNumber = 0;
            for (int raw = 0; raw < 5; raw++) {
                for (int symbol = 0; symbol < 5; symbol++) {
                    testRectangle.x = IndieGame.WIDTH / 4 - 160 + inventoryTextureSize * symbol;
                    testRectangle.y = 10 + inventoryTextureSize * raw;
                    if (testRectangle.contains(Gdx.input.getX() / 2,
                            (IndieGame.HEIGHT - Gdx.input.getY()) / 2) && Gdx.input.justTouched() &&
                            selectedNumber < inventory.getSwordsNames().size()) {
                        inventory.setSwordName(inventory.getSwordsNames().get(selectedNumber));

                    }
                    selectedNumber++;
                }
            }
        }
        // Спавн врага
        if (!isEnemyOnWindow && !isTraining) {
            enemyPlace = (int) (Math.random() * roadList.size());
            enemyRectangle.x = roadList.get(enemyPlace).x;
            enemyRectangle.y = roadList.get(enemyPlace).y;
            if (quest.overlaps(enemyRectangle) || quest.overlaps(hero.getHitBox())) {
                while (quest.overlaps(enemyRectangle)) {
                    enemyPlace = (int) (Math.random() * roadList.size());
                    enemyRectangle.x = roadList.get(enemyPlace).x;
                    enemyRectangle.y = roadList.get(enemyPlace).y;
                }
            }
            isEnemyOnWindow = true;
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        time.plusStateTime(Gdx.graphics.getDeltaTime());
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
        // Отрисовка Врага
        if (isEnemyOnWindow) {
            sb.draw(enemyTexture, enemyRectangle.x, enemyRectangle.y);
        }
        // Отрисовка обучения
        if (isTraining && !isDialog) {
            if (storyLevel == 0) {
                if (trainingCountKeyPressed < 4) {
                    sb.draw(trainingInfo1, 1, IndieGame.HEIGHT / 2 - 70);
                    sb.draw(progressBar, 10, IndieGame.HEIGHT / 2 - 60);
                    Pixmap partProgressBar = new Pixmap(120, 26, Pixmap.Format.RGB888);
                    partProgressBar.drawPixmap(progressBarTexture, 0, 0, 0, 0,
                            120 / 4 * (4 - trainingCountKeyPressed), 26);
                    progressBar = new Texture(partProgressBar, Pixmap.Format.RGB888, false);
                }
            }
            if (storyLevel == 1) {
                sb.draw(trainingInfo2, 1, IndieGame.HEIGHT / 2 - 70);
            }
        }
        // Отрисовка денег
        if (!isTraining) {
            font.draw(sb, "money:" + hero.getMoney(), 1, IndieGame.HEIGHT / 2 - 10);
        }
        // Отрисовка места квеста
        sb.draw(questImage, quest.x, quest.y);
        // Отрисовка замка
        sb.draw(roadImage, castle.x, castle.y);
        sb.draw(roadImage, castle.x + 17, castle.y);
        sb.draw(roadImage, castle.x + 17, castle.y + 17);
        sb.draw(roadImage, castle.x, castle.y + 17);
        sb.draw(castleImage, castle.x, castle.y);
        // Отрисовка героя
        sb.draw(hero.getTexture(), hero.getPosition().x, hero.getPosition().y);
        // Отрисовка меню
        if (menu.isOpen()) {
            sb.draw(menu.getMenuBackground(), IndieGame.WIDTH / 4 - 76, 150);
            font.draw(sb, "MENU", IndieGame.WIDTH / 4 - 25, 240);
            sb.draw(menu.getMenuButtonTexture(), menu.getMenuButton().x, menu.getMenuButton().y);
            moneyFont.draw(sb, "Exit", menu.getMenuButton().x + 5, menu.getMenuButton().y + 20);
        }
        // Отрисовка магазина
        if (shop.isOpen()) {
            itemNumber = 0;
            sb.draw(shop.getBackgroundTexture(), IndieGame.WIDTH / 4 - 160, 10);
            font.draw(sb, "SHOP", 335, 310);
            sb.draw(shop.getBuyButtonTexture(), shop.getBuyButton().x, shop.getBuyButton().y);
            font.draw(sb, "buy", 347, 65);
//            for (int raw = 0; raw < 2; raw++) {
            for (int symbol = 0; symbol < 3; symbol++) {
                sb.draw(shop.getItemBackgroundTexture(), IndieGame.WIDTH / 4 - 100 + inventoryTextureSize * symbol,
                        100 + 96);
                if (itemNumber < shop.getPrices().size()) {
                    sb.draw(swords.getSwordTexture(shop.getProductsNames().get(itemNumber)),
                            IndieGame.WIDTH / 4 - 100 + inventoryTextureSize
                                    * symbol + 32 - swords.getSwordTexture(
                                    shop.getProductsNames().get(itemNumber)).getWidth() / 2,
                            100 + 96 + 32 - swords.getSwordTexture(
                                    shop.getProductsNames().get(itemNumber)).getHeight() / 2);
                    font.draw(sb, shop.getPrices().get(itemNumber).toString(), IndieGame.WIDTH / 4
                            - 80 + inventoryTextureSize * symbol, 90 + 96);
                }
                if (shop.isSelect() && shop.selectedNumber() == itemNumber) {
                    sb.draw(shop.getSelectedTexture(),
                            IndieGame.WIDTH / 4 - 100 + inventoryTextureSize * symbol, 100 + 96);
                    if (shop.getSoldNames().contains(shop.getProductsNames().get(shop.selectedNumber())))
                        sb.draw(shop.getSoldTexture(), IndieGame.WIDTH / 4 - 100 + inventoryTextureSize * symbol,
                                100 + 96);
                }
                itemNumber++;
            }
//            }
        }
        // Отрисовка инвентаря
        if (inventory.isOpen()) {
            itemNumber = 0;
            sb.draw(inventory.getMenuBackgroundTexture(), IndieGame.WIDTH / 4 - 224, 266);
            sb.draw(inventory.getBackpackTexture(), IndieGame.WIDTH / 4 - 224, 266);
            sb.draw(inventory.getBackgroundTexture(), IndieGame.WIDTH / 4 - 160, 10);
            for (int raw = 0; raw < 5; raw++) {
                for (int symbol = 0; symbol < 5; symbol++) {
                    sb.draw(inventory.getSlotTexture(), IndieGame.WIDTH / 4 - 160 + inventoryTextureSize * symbol,
                            10 + inventoryTextureSize * raw);
                    if (itemNumber < swordsList.size()) {
                        sb.draw(swords.getSwordTexture(inventory.getSwordsNames().get(itemNumber)),
                                IndieGame.WIDTH / 4 - 160 + inventoryTextureSize * symbol + 32 -
                                        swords.getSwordTexture(inventory.getSwordsNames()
                                                .get(itemNumber)).getWidth() / 2,
                                10 + inventoryTextureSize * raw + 32 -
                                        swords.getSwordTexture(inventory.getSwordsNames()
                                                .get(itemNumber)).getHeight() / 2);
                        if (Objects.equals(inventory.getSwordName(),
                                inventory.getSwordsNames().get(itemNumber))) sb.draw(
                                inventory.getEquippedText(), IndieGame.WIDTH / 4 - 160 +
                                        inventoryTextureSize * symbol,
                                10 + inventoryTextureSize * raw);
                        itemNumber++;
                    }
                }
            }
        }
        // Отрисовка диалогового окна
        if (isDialog) {
            if (dialogNumber == 1) {
                sb.draw(dialogBackground, dialogRectangle.x, dialogRectangle.y);
                font.draw(sb, "Today is a great day to go chop wood\nin the forest",
                        dialogRectangle.x + 14,
                        dialogRectangle.y + 90);
                font.draw(sb, "Click to start", dialogRectangle.x + 34, dialogRectangle.y + 20);
            }
            if (dialogNumber == 2) {
                sb.draw(dialogBackground, dialogRectangle.x, dialogRectangle.y);
                font.draw(sb, "Where did the slime come from in the\nforest. " +
                                "I need to tell the king about this",
                        dialogRectangle.x + 14, dialogRectangle.y + 90);
                font.draw(sb, "Click to continue", dialogRectangle.x + 34,
                        dialogRectangle.y + 20);
            }
            if (dialogNumber == 3) {
                sb.draw(dialogBackground, dialogRectangle.x, dialogRectangle.y);
                font.draw(sb, "King: I give you this sword\nCome back when you kill 20 enemies",
                        dialogRectangle.x + 14, dialogRectangle.y + 90);
                font.draw(sb, "Click to continue", dialogRectangle.x + 34,
                        dialogRectangle.y + 20);
            }
            if (dialogNumber == 4) {
                sb.draw(dialogBackground, dialogRectangle.x, dialogRectangle.y);
                font.draw(sb, "King: Now you are ready for the final\nbattle " +
                                "Slimes appear because of the\ndemon that is in the field. " +
                                "Kill him",
                        dialogRectangle.x + 14, dialogRectangle.y + 90);
                font.draw(sb, "Click to continue", dialogRectangle.x + 34,
                        dialogRectangle.y + 20);
            }
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