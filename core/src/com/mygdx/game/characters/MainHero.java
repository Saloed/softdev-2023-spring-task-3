package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.playStateActivities.ActionTime;
import com.mygdx.game.playStateActivities.Inventory;
import com.mygdx.game.playStateActivities.Menu;
import com.mygdx.game.playStateActivities.Shop;
import com.mygdx.game.states.PlayState;


public class MainHero {
    private static final int textureSize = 16;
    protected int enemyKills;
    protected int money;
    private final Vector2 position;
    protected int health;
    protected int attack;
    protected int healthMax;
    protected Rectangle heroHitBox;
    private final Texture heroTexture;
    private Menu menu;
    private ActionTime time;
    private Shop shop;
    private final Inventory inventory;

    public MainHero(int x, int y, int health, int attack) {
        enemyKills = 0;
        inventory = new Inventory();
        money = 0;
        position = new Vector2(x, y);
        heroHitBox = new Rectangle();
        heroHitBox.x = position.x;
        heroHitBox.y = position.y;
        heroHitBox.height = textureSize;
        heroHitBox.width = textureSize;
        heroTexture = new Texture("characters/farmer.png");
        this.healthMax = health;
        this.health = health;
        this.attack = attack;
    }

    public void update() {
        if (!PlayState.isDialog && !inventory.isOpen() && !menu.isOpen() && !shop.isOpen()
                && time.getActionTime() + 0.5 < time.getStateTime()) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                position.x += (textureSize + 1);
                time.setActionTime(time.getStateTime());
                if (PlayState.isTraining) {
                    if (!PlayState.isRightPressed) {
                        PlayState.trainingCountKeyPressed++;
                        PlayState.isRightPressed = true;
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                position.x -= (textureSize + 1);
                time.setActionTime(time.getStateTime());
                if (PlayState.isTraining) {
                    if (!PlayState.isLeftPressed) {
                        PlayState.trainingCountKeyPressed++;
                        PlayState.isLeftPressed = true;
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                position.y += (textureSize + 1);
                time.setActionTime(time.getStateTime());
                if (PlayState.isTraining) {
                    if (!PlayState.isUpPressed) if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                        PlayState.trainingCountKeyPressed++;
                        PlayState.isUpPressed = true;
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                position.y -= (textureSize + 1);
                time.setActionTime(time.getStateTime());
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

    public int getMoney() {
        return money;
    }

    public void earnMoney(int moneyCount) {
        this.money += moneyCount;
    }

    public void spendMoney(int price) {
        this.money -= price;
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setMenu(Menu newMenu) {
        menu = newMenu;
    }
    public void setTime(ActionTime newTime) {time= newTime;}
    public void setShop(Shop newShop) {shop = newShop;}

    public int getEnemyKills() {
        return enemyKills;
    }

    public void enemyKill() {
        this.enemyKills++;
    }
}