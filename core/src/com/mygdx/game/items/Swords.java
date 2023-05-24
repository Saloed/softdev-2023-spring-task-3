package com.mygdx.game.items;

import com.badlogic.gdx.graphics.Texture;

import java.util.Objects;

public class Swords {
    protected String swordName;
//    protected boolean isSpecialSword;
    protected Texture startSword;
    protected Texture fireSwordTexture;
    protected Texture fireSwordAttackSheet;
    protected Texture fireSwordEffect;
    protected Texture fireSwordIcon;
    protected Texture empty;

    public Swords(String name) {
        swordName = name;
        empty = new Texture("items/swords/empty.png");
        startSword = new Texture("items/swords/freeSword.png");
        fireSwordTexture = new Texture("items/swords/fireSword.png");
        fireSwordAttackSheet = new Texture("items/swords/farmerFireSwordAttack.png");
        fireSwordEffect = new Texture("items/swords/fireAttackEffect.png");
        fireSwordIcon = new Texture("items/swords/fireAttackIcon.png");
    }

    public Texture getSwordTexture(String name) {
        if (Objects.equals(name, "fireSword")) return fireSwordTexture;
        return startSword;
    }

    public Texture getFireSwordAttackSheet() {
        if (Objects.equals(this.swordName, "fireSword")) return fireSwordAttackSheet;
        return startSword;
    }

    public Texture getSwordEffect() {
        if (Objects.equals(this.swordName, "fireSword")) return fireSwordEffect;
        return startSword;
    }

    public Texture getSwordIcon() {
        if (Objects.equals(this.swordName, "fireSword")) return fireSwordIcon;
        return startSword;
    }

    public Texture getEmpty() {
        return empty;
    }
}
