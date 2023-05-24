package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.Texture;

import java.util.Objects;

public class Enemy {
    protected int health;
    protected int healthMax;
    protected int attack;
    protected String name;
    private Texture slimeTexture;
    private Texture demonTexture;
    protected Texture currentTexture;

    public Enemy(String name) {
        demonTexture = new Texture("characters/demon.png");
        slimeTexture = new Texture("characters/slime.png");
        if (Objects.equals(name, "slime")) {
            this.name = "slime";
            this.attack = 10;
            this.health = 110;
            this.healthMax = 110;
            currentTexture = slimeTexture;
        }
        if (Objects.equals(name, "demon")) {
            this.name = "demon";
            this.attack = 25;
            this.health = 150;
            this.healthMax = 150;
            currentTexture = demonTexture;
        }
    }

    public Texture getTexture() {
        return currentTexture;
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

}
