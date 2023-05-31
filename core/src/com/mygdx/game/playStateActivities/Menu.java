package com.mygdx.game.playStateActivities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.IndieGame;

public class Menu {
    private final Texture Background;
    private final Texture ButtonTexture;
    private static boolean isOpen;
    private final Rectangle Button;

    public Menu() {
        isOpen = false;
        Background = new Texture("playWindow/menuBackground.png");
        ButtonTexture = new Texture("playWindow/menuButton.png");
        Button = new Rectangle();
        Button.x = IndieGame.WIDTH / 4 - 20;
        Button.y = IndieGame.HEIGHT / 4 - 50;
        Button.height = 32;
        Button.width = 32;
    }
    public Texture getMenuBackground(){
        return Background;
    }public Texture getMenuButtonTexture(){
        return ButtonTexture;
    }
    public Rectangle getMenuButton(){
        return Button;
    }
    public boolean isOpen(){
        return isOpen;
    }
    public void changeOpen(){
        isOpen = !isOpen;
    }
}
