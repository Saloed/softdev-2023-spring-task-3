package com.mygdx.game.items;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Swords {
    protected String swordName;
    protected Texture startSword;
    protected Texture freeSwordTexture;
    protected Texture sword1Texture;
    protected Texture sword2Texture;
    protected Texture fireSwordTexture;
    protected Texture fireSwordAttackSheet;
    protected Texture fireSwordEffect;
    protected Texture fireSwordIcon;
    protected Texture empty;
    protected List<Texture> swordEffectsTextures;

    public Swords(String name) {
        swordName = name;
        swordEffectsTextures = new ArrayList<>();
        swordEffectsTextures.add(null);
        swordEffectsTextures.add(null);
        swordEffectsTextures.add(null);
        empty = new Texture("items/swords/empty.png");
        startSword = new Texture("items/swords/startSword.png");
        freeSwordTexture = new Texture("items/swords/freeSword.png");
        sword1Texture = new Texture("items/swords/sword1.png");
        sword2Texture = new Texture("items/swords/sword2.png");

        fireSwordTexture = new Texture("items/swords/fireSword.png");
        fireSwordAttackSheet = new Texture("items/swords/farmerFireSwordAttack.png");
        fireSwordEffect = new Texture("items/swords/fireAttackEffect.png");
        fireSwordIcon = new Texture("items/swords/fireAttackIcon.png");
    }

    public Texture getSwordTexture(String name) {
        if (Objects.equals(name, "fireSword")) return fireSwordTexture;
        if (Objects.equals(name, "freeSword")) return freeSwordTexture;
        if (Objects.equals(name, "sword1")) return sword1Texture;
        if (Objects.equals(name, "sword2")) return sword2Texture;
        return startSword;
    }

    public List<Texture> getSwordEffectsTextures() {
        swordEffectsTextures.set(0, fireSwordIcon);
        swordEffectsTextures.set(1, fireSwordEffect);
        swordEffectsTextures.set(2, fireSwordAttackSheet);
        return swordEffectsTextures;
    }

    public void setSwordName(String swordName) {
        this.swordName = swordName;
    }

    public Texture getEmpty() {
        return empty;
    }

    public int getAttack(String name) {
        if (Objects.equals(name, "fireSword")) return 40;
        if (Objects.equals(name, "freeSword")) return 5;
        if (Objects.equals(name, "sword1")) return 15;
        if (Objects.equals(name, "sword2")) return 20;
        return 0;
    }
}
