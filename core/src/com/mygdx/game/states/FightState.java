package com.mygdx.game.states;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.IndieGame;
import com.mygdx.game.characters.Enemy;
import com.mygdx.game.characters.MainHero;
import com.mygdx.game.playStateActivities.Inventory;
import com.mygdx.game.playStateActivities.Shop;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class FightState extends State {
    // Создание шрифтов
    BitmapFont fontEnergy;
    BitmapFont fontHP;
    BitmapFont fontAttackDescription;
    String hpString;
    // Создание переменных для анимации
    private static final int FRAME_COLS_HERO = 3, FRAME_COLS_ENEMY = 6, FRAME_ROWS = 1, FRAME_COLS_FIRE = 18;
    private final Animation<TextureRegion> farmerAttackAnimation;
    private Animation<TextureRegion> swordAttackAnimation;
    private Animation<TextureRegion> swordAttackEffectsAnimation;
    private final Animation<TextureRegion> enemyAttackAnimation;
    float notificationTime;
    float startTimeOne;
    float startTimeTwo;
    float stateTime;
    // Считывание всех текстур
    private final Texture finalFight;
    private final Texture trainingTexture1;
    private final Texture trainingTexture2;
    private final Texture loseButtonTexture;
    private final Texture exitButtonTexture;
    private final Texture victoryTexture;
    private final Pixmap healthRed;
    private Pixmap partHealth;
    private final Texture emptyEnergyTexture;
    private final Texture fullEnergyTexture;
    private final Texture buttonTexture;
    private Texture heroHealthTexture;
    private Texture enemyHealthTexture;
    private final Texture healthBackground;
    private final Texture backgroundTexture;
    private final Texture heroTexture;
    private Texture heroCurrentTexture;
    private Texture enemyCurrentTexture;
    private final Texture emptyTexture;
    private final Texture enemyTexture;
    private final Texture attackIcon;
    private final Texture swordIcon;
    private final Texture defenceIcon;
    private final Rectangle getEnergyButton;
    private final Rectangle attackButton1;
    private Rectangle attackButton2;
    private final Rectangle attackButton3;
    private final Rectangle exitButton;
    private final Rectangle loseButton1;
    private final Rectangle loseButton2;

    private int fightRound;
    private int attackNumber;
    private int energyCountDraw;
    private int energyCount;
    // Создание переменной, определяющей статус битвы
    private int fightStatus;
    private int winStatus;
    private final boolean isStory;
    protected int storyLevel;
    // Герой и враг
    private final MainHero hero;
    private final Enemy enemy;
    private final Inventory inventory;
    private final Shop shop;

    public FightState(GameStateManager stateManager, MainHero hero, Enemy enemy,
                      boolean isStory, int storyLevel, Inventory inventory, Shop shop) {
        super(stateManager);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, IndieGame.WIDTH, IndieGame.HEIGHT);

        this.shop = shop;
        this.inventory = inventory;
        this.hero = hero;
        this.enemy = enemy;
        this.isStory = isStory;
        this.storyLevel = storyLevel;

        fontHP = new BitmapFont();
        fontHP.setColor(0, 0, 0, 1);
        fontEnergy = new BitmapFont();
        fontEnergy.setColor(0, 0, 0, 1);
        fontEnergy.getData().setScale(1.3f, 1.3f);
        fontAttackDescription = new BitmapFont();
        fontAttackDescription.setColor(0, 0, 0, 1);
        fontAttackDescription.getData().setScale(2, 2);
        hpString = "HP:";

        fightRound = 1;
        attackNumber = 0;
        energyCount = 0;
        energyCountDraw = 0;
        fightStatus = 0;
        winStatus = 0;
        loseButton1 = new Rectangle();
        loseButton1.x = IndieGame.WIDTH / 2 - 50;
        loseButton1.y = IndieGame.HEIGHT / 2 - 50;
        loseButton1.height = 64;
        loseButton1.width = 64;
        loseButton2 = new Rectangle();
        loseButton2.x = IndieGame.WIDTH / 2;
        loseButton2.y = IndieGame.HEIGHT / 2 - 50;
        loseButton2.height = 64;
        loseButton2.width = 64;
        exitButton = new Rectangle();
        exitButton.x = IndieGame.WIDTH / 2 - 20;
        exitButton.y = IndieGame.HEIGHT / 2 - 50;
        exitButton.height = 64;
        exitButton.width = 64;
        getEnergyButton = new Rectangle();
        getEnergyButton.x = IndieGame.WIDTH / 2 - 110;
        getEnergyButton.y = 200;
        getEnergyButton.width = 220;
        getEnergyButton.height = 72;
        attackButton1 = new Rectangle();
        attackButton1.x = 10;
        attackButton1.y = 10;
        attackButton1.width = 64;
        attackButton1.height = 64;
        if (!Objects.equals(inventory.getSwordName(), "startSword")) {
            attackButton2 = new Rectangle();
            attackButton2.x = 80;
            attackButton2.y = 10;
            attackButton2.width = 64;
            attackButton2.height = 64;
        }
        attackButton3 = new Rectangle();
        attackButton3.x = 150;
        attackButton3.y = 10;
        attackButton3.width = 64;
        attackButton3.height = 64;
        enemyTexture = enemy.getTexture();
        enemyCurrentTexture = enemyTexture;
        heroTexture = new Texture("characters/farmerFight.png");
        heroCurrentTexture = heroTexture;
        // Записывание текстур
        finalFight = new Texture("fight/lastFight.png");
        trainingTexture1 = new Texture("fight/trainingWindow1.png");
        trainingTexture2 = new Texture("fight/trainingWindow2.png");
        loseButtonTexture = new Texture("fight/loseButton.png");
        exitButtonTexture = new Texture("fight/exitButton.png");
        victoryTexture = new Texture("fight/victory.png");
        List<Texture> swordTextures = inventory.getSwordTextures();
        buttonTexture = new Texture("fight/buttonLarge.png");
        emptyEnergyTexture = new Texture("fight/emptyEnergy.png");
        fullEnergyTexture = new Texture("fight/fullEnergy.png");
        emptyTexture = new Texture("fight/empty.png");
        attackIcon = new Texture("fight/attackIcon1.png");
        defenceIcon = new Texture("fight/defenceIcon.png");
        Texture enemyAttackSheet = enemy.getAttackSheet();
        Texture farmerAttackSheet = new Texture(Gdx.files.internal("fight/farmerAttack.png"));
        swordIcon = swordTextures.get(0);
        Texture swordEffectAttackSheet = swordTextures.get(1);
        Texture swordAttackSheet = swordTextures.get(2);
        healthBackground = new Texture("fight/healthBackground.png");
        backgroundTexture = new Texture("fight/fightBackground.png");
        // Создание healthbar'а
        healthRed = (new Pixmap(Gdx.files.getFileHandle("fight/healthRed.png",
                Files.FileType.Internal)));
        partHealth = new Pixmap(120, 26, Pixmap.Format.RGB888);
        partHealth.drawPixmap(healthRed, 0, 0, 0, 0,
                hero.getHealth() * hero.getHealthMax() * 120, 26);
        heroHealthTexture = new Texture(partHealth, Pixmap.Format.RGB888, false);
        partHealth.drawPixmap(healthRed, 0, 0, 0, 0,
                enemy.getHealth() * enemy.getHealthMax() * 120, 26);
        enemyHealthTexture = new Texture(partHealth, Pixmap.Format.RGB888, false);
        TextureRegion[][] tmp = TextureRegion.split(farmerAttackSheet,
                farmerAttackSheet.getWidth() / FRAME_COLS_HERO,
                farmerAttackSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] farmerAttackFrames = new TextureRegion[FRAME_COLS_HERO * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS_HERO; j++) {
                farmerAttackFrames[index++] = tmp[i][j];
            }
        }
        farmerAttackAnimation = new Animation<>(0.25f, farmerAttackFrames);
        tmp = TextureRegion.split(enemyAttackSheet,
                enemyAttackSheet.getWidth() / FRAME_COLS_ENEMY,
                enemyAttackSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] enemyAttackFrames = new TextureRegion[FRAME_COLS_ENEMY * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS_ENEMY; j++) {
                enemyAttackFrames[index++] = tmp[i][j];
            }
        }
        enemyAttackAnimation = new Animation<>(0.25f, enemyAttackFrames);
        if (Objects.equals(inventory.getSwordName(), "fireSword")) {
            tmp = TextureRegion.split(swordAttackSheet,
                    swordAttackSheet.getWidth() / FRAME_COLS_HERO,
                    swordAttackSheet.getHeight() / FRAME_ROWS);
            TextureRegion[] swordAttackFrames = new TextureRegion[FRAME_COLS_HERO * FRAME_ROWS];
            index = 0;
            for (int i = 0; i < FRAME_ROWS; i++) {
                for (int j = 0; j < FRAME_COLS_HERO; j++) {
                    swordAttackFrames[index++] = tmp[i][j];
                }
            }
            swordAttackAnimation = new Animation<>(0.2f, swordAttackFrames);
        }
        if (Objects.equals(inventory.getSwordName(), "fireSword")) {
            tmp = TextureRegion.split(swordEffectAttackSheet,
                    swordEffectAttackSheet.getWidth() / FRAME_COLS_FIRE,
                    swordEffectAttackSheet.getHeight() / FRAME_ROWS);
            TextureRegion[] swordAttackEffectsFrames = new TextureRegion[FRAME_COLS_FIRE * FRAME_ROWS];
            index = 0;
            for (int i = 0; i < FRAME_ROWS; i++) {
                for (int j = 0; j < FRAME_COLS_FIRE; j++) {
                    swordAttackEffectsFrames[index++] = tmp[i][j];
                }
            }
            swordAttackEffectsAnimation = new Animation<>(0.04f, swordAttackEffectsFrames);
        }

        startTimeOne = 0f;
        startTimeTwo = 0f;
        stateTime = 0f;
        notificationTime = 0f;
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        if (winStatus == 0) {
            if (fightStatus == 0) {
                notificationTime = stateTime;
                energyCount += Math.random() * 3;
                if (energyCount > 3) energyCount = 3;
                if (PlayState.isTraining) {
                    if (fightRound == 1) energyCount = 1;
                    if (fightRound == 2) energyCount = 1;
                }
                fightStatus = 1;
            }
            if (fightStatus == 1) {
                if (energyCount > 0) {
                    if (attackButton1.contains(new Point(Gdx.input.getX(),
                            IndieGame.HEIGHT - Gdx.input.getY())) &&
                            Gdx.input.justTouched() && ((PlayState.isTraining && fightRound != 2)
                            || !PlayState.isTraining)) {
                        startTimeOne = stateTime;
                        fightStatus = 2;
                        attackNumber = 1;
                    }
                    if (Objects.equals(inventory.getSwordName(), "fireSword")) {
                        if (attackButton2.contains(new Point(Gdx.input.getX(),
                                IndieGame.HEIGHT - Gdx.input.getY())) &&
                                Gdx.input.justTouched() && energyCount == 3) {
                            startTimeOne = stateTime;
                            fightStatus = 2;
                            attackNumber = 2;
                        }
                    }
                    if (attackButton3.contains(new Point(Gdx.input.getX(),
                            IndieGame.HEIGHT - Gdx.input.getY())) &&
                            Gdx.input.justTouched() && ((PlayState.isTraining && fightRound != 1)
                            || !PlayState.isTraining)) {
                        fightStatus = 2;
                        attackNumber = 3;
                    }
                } else fightStatus = 2;
            }
            if (fightStatus == 2) {
                if (stateTime - 0.75 > startTimeOne) {
                    startTimeOne = stateTime + 1;
                    startTimeTwo = stateTime;
                    fightStatus = 3;
                    if (attackNumber == 1) {
                        enemy.setHealth(enemy.getHealth() - (hero.getAttack()
                                + inventory.getSwordAttack(inventory.getSwordName())));
                        energyCount -= 1;
                    }
                    if (attackNumber == 2) {
                        enemy.setHealth(enemy.getHealth() - 100);
                        energyCount -= 3;
                    }
                    if (attackNumber != 3) attackNumber = 0;
                    if (enemy.getHealth() < 0) enemy.setHealth(0);
                }
            }
            if (fightStatus == 3 && stateTime > startTimeTwo + 1.5) {
                if (stateTime - 1.5 > startTimeOne) {
                    fightStatus = 0;
                    if (attackNumber == 3) hero.setHealth(hero.getHealth() - enemy.getAttack() + 5);
                    else {
                        hero.setHealth(hero.getHealth() - enemy.getAttack());
                    }
                    if (hero.getHealth() < 0) hero.setHealth(0);
                    fightRound++;
                }
            }
            if (hero.getHealth() == 0) winStatus = 1;
            if (enemy.getHealth() == 0) winStatus = 2;
        }
        if (winStatus == 1) {
            if (loseButton1.contains(new Point(Gdx.input.getX(), IndieGame.HEIGHT - Gdx.input.getY()))
                    && Gdx.input.justTouched()) {
                hero.setHealth(hero.getHealthMax());
                enemy.setHealth(enemy.getHealthMax());
                winStatus = 0;
            }
            if (loseButton2.contains(new Point(Gdx.input.getX(), IndieGame.HEIGHT - Gdx.input.getY()))
                    && Gdx.input.justTouched()) {
                hero.setHealth(hero.getHealthMax());
                stateManager.set(new PlayState(stateManager, true, storyLevel, hero, shop,
                        false));
                winStatus = 0;
            }
        }
        if (winStatus == 2) {
            if (exitButton.contains(new Point(Gdx.input.getX(), IndieGame.HEIGHT - Gdx.input.getY()))
                    && Gdx.input.justTouched()) {
                if (storyLevel == 3) Gdx.app.exit();
                if (storyLevel < 3 || storyLevel == 5) {
                    if (isStory) storyLevel++;
                    hero.setHealth(hero.getHealthMax());
                    hero.enemyKill();
                    if (hero.getEnemyKills() > 20) storyLevel = 2;
                    hero.earnMoney(10);
                    if (storyLevel == 2) stateManager.set(new PlayState(stateManager,
                            PlayState.isTraining, storyLevel, hero, shop,false));
                    stateManager.set(new PlayState(stateManager, true, storyLevel, hero, shop,
                            false));
                }
            }
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentHeroFrame = farmerAttackAnimation.getKeyFrame(stateTime, true);
        TextureRegion currentEnemyFrame = enemyAttackAnimation.getKeyFrame(stateTime, true);
        sb.draw(backgroundTexture, 0, 0);
        // Отрисовка energy bar'а
        for (int i = 20; i < 141; i += 60) {
            if (energyCountDraw < energyCount) {
                sb.draw(fullEnergyTexture, i, 750);
                energyCountDraw++;
            } else sb.draw(emptyEnergyTexture, i, 750);
        }
        energyCountDraw = 0;
        sb.draw(heroCurrentTexture, 10, 100);
        sb.draw(enemyCurrentTexture, 1100, 100);
        sb.draw(attackIcon, attackButton1.x, attackButton1.y);
        if (Objects.equals(inventory.getSwordName(), "fireSword")) {
            sb.draw(swordIcon, attackButton2.x, attackButton2.y);
        }
        sb.draw(defenceIcon, attackButton3.x, attackButton3.y);
        // Отрисовка кнопки получения энергии
        if (fightStatus == 1 && (notificationTime + 2) > stateTime && winStatus == 0) {
            sb.draw(buttonTexture, getEnergyButton.x, getEnergyButton.y);
            fontEnergy.draw(sb, "You got " + energyCount + " energy",
                    getEnergyButton.x + 46, getEnergyButton.y + 55);
        }
        // Отрисовка атаки героя
        if (fightStatus == 2 && winStatus == 0) {
            if (attackNumber == 1 || attackNumber == 2) {
                heroCurrentTexture = emptyTexture;
                if (attackNumber == 1) sb.draw(currentHeroFrame, 1000, 100);
                if (Objects.equals(inventory.getSwordName(), "fireSword") && attackNumber == 2) {
                    TextureRegion currentSwordFrame = swordAttackAnimation.getKeyFrame(stateTime,
                            true);
                    TextureRegion currentEffectFrame = swordAttackEffectsAnimation.getKeyFrame(stateTime,
                            true);
                    sb.draw(currentEffectFrame, 1000, 100);
                    sb.draw(currentSwordFrame, 1090, 95);
                }
                if (stateTime - 0.75 > startTimeOne) {
                    heroCurrentTexture = heroTexture;
                }
            }
        }
        // Отрисовка атаки врага
        if (fightStatus == 3 && stateTime > startTimeTwo + 1.5 && winStatus == 0) {
            enemyCurrentTexture = emptyTexture;
            sb.draw(currentEnemyFrame, 100, 100);
            if (stateTime - 1.5 > startTimeOne) {
                enemyCurrentTexture = enemyTexture;
            }
        }
        // Отрисовка HP врага и текста
        sb.draw(healthBackground, 1200, 700);
        sb.draw(enemyHealthTexture, 1204, 703);
        partHealth = new Pixmap(120, 26, Pixmap.Format.RGB888);
        partHealth.drawPixmap(healthRed, 0, 0, 0, 0,
                (int) (enemy.getHealth() * 1.2f), 26);
        enemyHealthTexture = new Texture(partHealth, Pixmap.Format.RGB888, false);
        fontHP.draw(sb, hpString + enemy.getHealth() + "/" + enemy.getHealthMax(), 1200, 750);
        // Отрисовка HP героя и текста
        sb.draw(healthBackground, 40, 700);
        sb.draw(heroHealthTexture, 44, 703);
        partHealth = new Pixmap(120, 26, Pixmap.Format.RGB888);
        partHealth.drawPixmap(healthRed, 0, 0, 0, 0,
                (int) (hero.getHealth() * 1.2f), 26);
        heroHealthTexture = new Texture(partHealth, Pixmap.Format.RGB888, false);
        fontHP.draw(sb, hpString + hero.getHealth() + "/" + hero.getHealthMax(), 40, 750);
        // Отрисовка обучения
        if (PlayState.isTraining) {
            if (fightRound == 1) sb.draw(trainingTexture1, attackButton1.x, attackButton1.y + 64);
            if (fightRound == 2) sb.draw(trainingTexture2, attackButton3.x, attackButton3.y + 64);
        }
        // Отрисовка описания атак
        if (fightStatus == 1) {
            if (attackButton1.contains(new Point(Gdx.input.getX(),
                    IndieGame.HEIGHT - Gdx.input.getY()))) {
                fontAttackDescription.draw(sb, "Attack: " + (hero.getAttack() +
                                inventory.getSwordAttack(inventory.getSwordName())) + "\nEnergy cost: 1",
                        IndieGame.WIDTH / 2 - 140, 600);
            }
            if (Objects.equals(inventory.getSwordName(), "fireSword")) {
                if (attackButton2.contains(new Point(Gdx.input.getX(),
                        IndieGame.HEIGHT - Gdx.input.getY()))) {
                    fontAttackDescription.draw(sb, "Attack: 100\nEnergy cost: 3",
                            IndieGame.WIDTH / 2 - 140, 600);
                }
            }
            if (attackButton3.contains(new Point(Gdx.input.getX(),
                    IndieGame.HEIGHT - Gdx.input.getY()))) {
                fontAttackDescription.draw(sb, "Save energy and give 5 defence\nEnergy cost: 0",
                        IndieGame.WIDTH / 2 - 140, 600);
            }
        }
        if (winStatus != 0) {
            if (storyLevel < 3 || storyLevel == 5)
                sb.draw(victoryTexture, IndieGame.WIDTH / 2 - 90, IndieGame.HEIGHT / 2);
            if (storyLevel == 3) sb.draw(finalFight, IndieGame.WIDTH / 2 - 140,
                    IndieGame.HEIGHT / 2 - 20);
            if (winStatus == 1) {
                fontEnergy.draw(sb, "Lose", IndieGame.WIDTH / 2 - 15,
                        IndieGame.HEIGHT / 2 + 45);
                sb.draw(loseButtonTexture, loseButton1.x, loseButton1.y);
                sb.draw(exitButtonTexture, loseButton2.x, loseButton2.y);
                fontEnergy.draw(sb, "Exit?", IndieGame.WIDTH / 2 - 15, loseButton1.y - 25);
            }
            if (winStatus == 2) {
                if (storyLevel == 3)
                    fontEnergy.draw(sb, "You end the game",
                            IndieGame.WIDTH / 2 - 68, IndieGame.HEIGHT / 2 + 45);
                if (storyLevel < 3 || storyLevel == 5) {
                    fontEnergy.draw(sb, "Victory", IndieGame.WIDTH / 2 - 25,
                            IndieGame.HEIGHT / 2 + 45);
                    fontEnergy.draw(sb, "Earn money:10", IndieGame.WIDTH / 2 - 35, 45);
                }
                sb.draw(exitButtonTexture, exitButton.x, exitButton.y);
            }
        }
        sb.end();
    }

    @Override
    public void dispose() {
        healthRed.dispose();
        heroHealthTexture.dispose();
        enemyHealthTexture.dispose();
        partHealth.dispose();
        healthBackground.dispose();
        backgroundTexture.dispose();
        heroTexture.dispose();
        heroCurrentTexture.dispose();
        enemyCurrentTexture.dispose();
        emptyTexture.dispose();
        enemyTexture.dispose();
        attackIcon.dispose();
        defenceIcon.dispose();
        buttonTexture.dispose();
        fontHP.dispose();
        fontEnergy.dispose();
        fontAttackDescription.dispose();
    }
}