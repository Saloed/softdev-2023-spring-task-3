package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.IndieGame;
import com.mygdx.game.items.Swords;
import com.mygdx.game.states.PlayState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainHero {
    protected int money;
    protected String swordName;
    protected Swords sword;
    protected List<String> swords;
    protected List<Texture> currentSwordTextures;
    private Vector2 position;
    protected int health;
    protected int attack;
    protected int healthMax;
    protected Rectangle heroHitBox;
    private Texture heroTexture;

    public MainHero(int x, int y, int health, int attack) {
        money = 0;
        position = new Vector2(x, y);
        heroHitBox = new Rectangle();
        heroHitBox.x = position.x;
        heroHitBox.y = position.y;
        heroHitBox.height = 16;
        heroHitBox.width = 16;
        heroTexture = new Texture("characters/farmer.png");
        this.healthMax = health;
        this.health = health;
        this.attack = attack;
        this.swords = new ArrayList<>();
        swords.add("startSword");
        this.swordName = "startSword";
        sword = new Swords(swordName);
        currentSwordTextures = new ArrayList<>();
        currentSwordTextures.add(sword.getEmpty());
        currentSwordTextures.add(sword.getEmpty());
        currentSwordTextures.add(sword.getEmpty());
        currentSwordTextures.add(sword.getEmpty());

    }

    public void update() {
        if (!PlayState.isDialog && !PlayState.isInventoryOpen && !PlayState.isMenuOpen
                && PlayState.actionTime + 0.5 < PlayState.stateTime) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                position.x += 17;
                PlayState.actionTime = PlayState.stateTime;
                if (PlayState.isTraining) {
                    if (!PlayState.isRightPressed) {
                        PlayState.trainingCountKeyPressed++;
                        PlayState.isRightPressed = true;
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                position.x -= 17;
                PlayState.actionTime = PlayState.stateTime;
                if (PlayState.isTraining) {
                    if (!PlayState.isLeftPressed) {
                        PlayState.trainingCountKeyPressed++;
                        PlayState.isLeftPressed = true;
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                position.y += 17;
                PlayState.actionTime = PlayState.stateTime;
                if (PlayState.isTraining) {
                    if (!PlayState.isUpPressed) if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                        PlayState.trainingCountKeyPressed++;
                        PlayState.isUpPressed = true;
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                position.y -= 17;
                PlayState.actionTime = PlayState.stateTime;
                if (PlayState.isTraining) {
                    if (!PlayState.isDownPressed) if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                        PlayState.trainingCountKeyPressed++;
                        PlayState.isDownPressed = true;
                    }
                }
            }
            heroHitBox.x = position.x;
            heroHitBox.y = position.y;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return heroTexture;
    }


    public Rectangle getHitBox() {
        return heroHitBox;
    }

    public Integer getHealthMax() {
        return healthMax;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setHealth(int newHealth) {
        this.health = newHealth;
    }

    public String getSwordName() {
        return swordName;
    }

    public List<Texture> getSwordTextures() {
        currentSwordTextures.set(0, sword.getSwordTexture(swordName));
        if (!Objects.equals(this.swordName, "startSword")) {
            currentSwordTextures.set(1, sword.getSwordIcon());
            currentSwordTextures.set(2, sword.getSwordEffect());
            currentSwordTextures.set(3, sword.getFireSwordAttackSheet());
        } else for (int i = 1; i < 3; i++)
            currentSwordTextures.set(i, sword.getEmpty());
        return currentSwordTextures;
    }

    public void findSword(String swordName) {
        this.swords.add(swordName);
    }

    public List<String> getSwordsNames() {
        return swords;
    }

    public int getMoney() {
        return money;
    }
    public void earnMoney(int moneyCount){
        this.money+=moneyCount;
    }
    public void spendMoney(int price){
        this.money-=price;
    }
    public void setPosition(int x,int y){
        position.x = x;
        position.y = y;
    }
}