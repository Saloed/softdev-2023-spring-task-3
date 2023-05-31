package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.Texture;

import java.util.Objects;

public class Enemy {
    protected int health;
    protected int healthMax;
    protected int attack;
    protected String name;
    protected Texture currentTexture;
    protected Texture attackSheet;

    public Enemy(String name) {
        Texture demonAttackSheet = new Texture("characters/demonAttack.png");
        Texture slimeAttackSheet = new Texture("characters/slimeAttack.png");
        Texture demonTexture = new Texture("characters/demon.png");
        Texture slimeTexture = new Texture("characters/slime.png");
        if (Objects.equals(name, "slime")) {
            this.name = "slime";
            this.attack = 10;
            this.health = 110;
            this.healthMax = 110;
            this.attackSheet = slimeAttackSheet;
            currentTexture = slimeTexture;
        }
        if (Objects.equals(name, "demon")) {
            this.name = "demon";
            this.attack = 25;
            this.health = 150;
            this.healthMax = 150;
            this.attackSheet = demonAttackSheet;
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

    public Texture getAttackSheet() {
        return attackSheet;
    }

    public void setHealth(int newHealth) {
        this.health = newHealth;
    }
}
