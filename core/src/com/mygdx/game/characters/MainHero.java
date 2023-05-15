package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.states.PlayState;


public class MainHero {
    private Vector2 position;
    protected int health;
    protected int attack;
    protected int healthMax;
    protected Rectangle heroHitBox;

    private Texture heroTexture;
    private Texture heroFightTexture;

    public MainHero(int x, int y, int health, int attack) {
        position = new Vector2(x, y);
        heroHitBox = new Rectangle();
        heroHitBox.x = position.x;
        heroHitBox.y = position.y;
        heroHitBox.height = 16;
        heroHitBox.width = 16;
        heroTexture = new Texture("characters/farmer.png");
        heroFightTexture = new Texture("characters/farmerFight.png");
        this.healthMax = health;
        this.health = health;
        this.attack = attack;
    }

    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += 17;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= 17;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += 17;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y -= 17;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        heroHitBox.x = position.x;
        heroHitBox.y = position.y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return heroTexture;
    }
    public Texture getFightTexture() {return heroFightTexture;}

    public Rectangle getHitBox() {
        return heroHitBox;
    }
    public Integer getHealthMax(){return healthMax;}
    public Integer getHealth() { return  health;}

    public Integer getAttack() { return  attack;}
    public void setHealth(int newHealth){this.health = newHealth;}

}