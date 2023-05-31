package com.mygdx.game.playStateActivities;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;


public class Shop {
    private boolean isOpen;
    private final List<Integer> prices;
    private final List<String> productsNames;
    private final List<String> soldNames;
    private final Texture itemBackgroundTexture;
    private final Texture selectedTexture;
    private final Texture backgroundTexture;
    private final Rectangle buyButton;
    private final Texture buyButtonTexture;
    private final Texture soldTexture;
    private boolean isSelect;
    private int selectedNumber;

    public Shop() {
        selectedTexture = new Texture("playWindow/selectedItem.png");
        backgroundTexture = new Texture("playWindow/shopBackground.jpeg");
        itemBackgroundTexture = new Texture("playWindow/shopItemBackground.png");
        buyButtonTexture = new Texture("playWindow/buyButton.png");
        soldTexture = new Texture("playWindow/soldItem.png");
        prices = new ArrayList<>();
        productsNames = new ArrayList<>();
        soldNames = new ArrayList<>();
        productsNames.add("fireSword");
        prices.add(200);
        productsNames.add("sword2");
        prices.add(40);
        productsNames.add("sword1");
        prices.add(30);
        buyButton = new Rectangle(329, 40, 160, 96);
        isOpen = false;
        isSelect = false;
        selectedNumber = 0;
    }

    public List<Integer> getPrices() {
        return prices;
    }

    public List<String> getProductsNames() {
        return productsNames;
    }

    public Texture getItemBackgroundTexture() {
        return itemBackgroundTexture;
    }

    public Texture getSelectedTexture() {
        return selectedTexture;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void changeOpen() {
        isOpen = !isOpen;
    }

    public Texture getBuyButtonTexture() {
        return buyButtonTexture;
    }

    public Rectangle getBuyButton() {
        return buyButton;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void trueSelect() {
        isSelect = true;
    }

    public int selectedNumber() {
        return selectedNumber;
    }

    public void setSelectedNumber(int selectedNumber) {
        this.selectedNumber = selectedNumber;
    }
    public void productSold(String name){
        soldNames.add(name);
    }
    public List<String> getSoldNames(){
        return soldNames;
    }
    public Texture getSoldTexture(){
        return soldTexture;
    }
}
