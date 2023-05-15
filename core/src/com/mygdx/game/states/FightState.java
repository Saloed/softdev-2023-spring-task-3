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

import java.awt.*;

public class FightState extends State {
    // Создание шрифта
    BitmapFont font;
    String hpString;
    // Создание переменных для анимации
    private static final int FRAME_COLS_HERO = 3, FRAME_COLS_ENEMY = 6, FRAME_ROWS = 1;
    private Animation<TextureRegion> farmerAttackAnimation;
    private Animation<TextureRegion> enemyAttackAnimation;
    float startTimeOne;
    float startTimeTwo;
    float stateTime;
    // Считывание всех текстур
    private final Pixmap healthRed;
    private Pixmap partHealth;
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
    private final Rectangle attackButton;
    // Создание переменной, определяющей статус битвы
    private int fightStatus;
    // Герой и враг
    private MainHero hero;
    private Enemy enemy;

    public FightState(GameStateManager stateManager, MainHero hero, Enemy enemy) {
        super(stateManager);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, IndieGame.WIDTH, IndieGame.HEIGHT);

        this.hero = hero;
        this.enemy = enemy;

        font = new BitmapFont();
        font.setColor(0, 0, 0, 1);
        hpString = "HP:";

        fightStatus = 0;
        attackButton = new Rectangle();
        attackButton.x = 10;
        attackButton.y = 10;
        attackButton.width = 64;
        attackButton.height = 64;
        enemyTexture = enemy.getTexture();
        enemyCurrentTexture = enemyTexture;
        heroTexture = hero.getFightTexture();
        heroCurrentTexture = heroTexture;
        // Записывание текстур
        emptyTexture = new Texture("fight/empty.png");
        attackIcon = new Texture("fight/attackIcon.png");
        Texture enemyAttackSheet = new Texture(Gdx.files.internal("characters/slimeAttack.png"));
        Texture farmerAttackSheet = new Texture(Gdx.files.internal("fight/farmerAttack.png"));
        healthBackground = new Texture("fight/healthBackground.png");
        backgroundTexture = new Texture("fight/fightBackground.png");
        // Создание healthbar'а
        healthRed = (new Pixmap(Gdx.files.getFileHandle("fight/healthRed.png", Files.FileType.Internal)));
        partHealth = new Pixmap(120, 26, Pixmap.Format.RGB888);
        partHealth.drawPixmap(healthRed, 0, 0, 0, 0, hero.getHealth() * hero.getHealthMax() * 120, 26);
        heroHealthTexture = new Texture(partHealth, Pixmap.Format.RGB888, false);
        partHealth.drawPixmap(healthRed, 0, 0, 0, 0, enemy.getHealth() * enemy.getHealthMax() * 120, 26);
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
        startTimeOne = 0f;
        startTimeTwo = 0f;
        stateTime = 0f;
        farmerAttackAnimation = new Animation<>(0.25f, farmerAttackFrames);
        enemyAttackAnimation = new Animation<>(0.25f, enemyAttackFrames);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        if (attackButton.contains(new Point(Gdx.input.getX(), IndieGame.HEIGHT - Gdx.input.getY())) &&
                Gdx.input.justTouched()) {
            startTimeOne = stateTime;
            fightStatus = 1;
        }
        if (fightStatus == 1) {
            if (stateTime - 0.75 > startTimeOne) {
                startTimeOne = stateTime + 1;
                startTimeTwo = stateTime;
                fightStatus = 2;
                enemy.setHealth(enemy.getHealth() - hero.getAttack());
            }
        }
        if (fightStatus == 2 && stateTime > startTimeTwo + 1.5) {
            if (stateTime - 1.5 > startTimeOne) {
                fightStatus = 0;
                hero.setHealth(hero.getHealth() - enemy.getAttack());
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
        sb.draw(healthBackground, 40, 700);
        sb.draw(heroHealthTexture, 44, 703);
        sb.draw(healthBackground, 1200, 700);
        sb.draw(enemyHealthTexture, 1204, 703);
        sb.draw(heroCurrentTexture, 10, 100);
        sb.draw(enemyCurrentTexture, 1100, 100);
        sb.draw(attackIcon, attackButton.x, attackButton.y);
        if (fightStatus == 1) {
            heroCurrentTexture = emptyTexture;
            sb.draw(currentHeroFrame, 1000, 100);
            if (stateTime - 0.75 > startTimeOne) {
                heroCurrentTexture = heroTexture;
            }
        }
        if (fightStatus == 2 && stateTime > startTimeTwo + 1.5) {
            enemyCurrentTexture = emptyTexture;
            sb.draw(currentEnemyFrame, 100, 100);
            if (stateTime - 1.5 > startTimeOne) {
                enemyCurrentTexture = enemyTexture;
            }
        }
        // Отрисовка HP врага и текста
        partHealth = new Pixmap(120, 26, Pixmap.Format.RGB888);
        partHealth.drawPixmap(healthRed, 0, 0, enemy.getHealthMax() - enemy.getHealth(), 0, 120, 26);
        enemyHealthTexture = new Texture(partHealth, Pixmap.Format.RGB888, false);
        font.draw(sb, hpString + enemy.getHealth() + "/" + enemy.getHealthMax(), 1200, 750);
        font.draw(sb,"Attack:"+enemy.getAttack().toString(),1200,690);
        // Отрисовка HP героя и текста
        partHealth = new Pixmap(120, 26, Pixmap.Format.RGB888);
        partHealth.drawPixmap(healthRed, 0, 0, hero.getHealthMax() - hero.getHealth(), 0, 120, 26);
        heroHealthTexture = new Texture(partHealth, Pixmap.Format.RGB888, false);
        font.draw(sb, hpString + hero.getHealth() + "/" + hero.getHealthMax(), 40, 750);
        font.draw(sb,"Attack:"+hero.getAttack().toString(),40,690);
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
        font.dispose();
    }
}