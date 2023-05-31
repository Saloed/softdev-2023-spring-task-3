package com.mygdx.game.playStateActivities;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.items.Swords;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inventory {
    private boolean isOpen;
    private final Texture equippedText;
    private final Texture menuBackground;
    private final Texture backgroundTexture;
    private final Texture backpackTexture;
    private final Texture slotTexture;
    protected List<Texture> currentSwordTextures;

    protected String swordName;
    protected Swords sword;
    protected List<String> swords;


    public Inventory() {
        equippedText = new Texture("playWindow/equippedMessage.png");
        menuBackground = new Texture("playWindow/inventoryMenuBackground.png");
        backgroundTexture = new Texture("playWindow/inventoryBackground.jpeg");
        backpackTexture = new Texture("playWindow/inventory.png");
        slotTexture = new Texture("playWindow/inventorySlot.png");
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

    public Texture getBackpackTexture() {
        return backpackTexture;
    }

    public Texture getMenuBackgroundTexture() {
        return menuBackground;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public Texture getSlotTexture() {
        return slotTexture;
    }

    public String getSwordName() {
        return swordName;
    }

    public void setSwordName(String swordName) {
        this.swordName = swordName;
        sword.setSwordName(swordName);
    }

    public List<Texture> getSwordTextures() {
        currentSwordTextures.set(0, sword.getSwordTexture(swordName));
        if (Objects.equals(this.swordName, "fireSword")) {
            currentSwordTextures.set(0, sword.getSwordEffectsTextures().get(0));
            currentSwordTextures.set(1, sword.getSwordEffectsTextures().get(1));
            currentSwordTextures.set(2, sword.getSwordEffectsTextures().get(2));
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
    public boolean isOpen(){
        return  isOpen;
    }
    public void changeOpen(){
        isOpen = !isOpen;
    }

    public Texture getEquippedText() {
        return equippedText;
    }
    public int getSwordAttack (String swordName){
        return sword.getAttack(swordName);
    }
}
