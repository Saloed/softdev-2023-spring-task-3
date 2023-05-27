package org.scenes;

import org.main.Game;

import java.awt.image.BufferedImage;

public class GameScene {

    private Game game;
    protected int animationIndex;
    protected int ANIMATION_SPEED;
    protected  int tick;


    public GameScene(Game game) {
        this.game = game;

        ANIMATION_SPEED = 40;
    }

    protected void updateTick() {
        //Для анимации воды
        tick++;
        if (tick >= ANIMATION_SPEED) {
            tick = 0;
            animationIndex++;
            if (animationIndex >= 4)
                animationIndex = 0;
        }
    }

    protected boolean isAnimation(int spriteID) {
        return getGame().getTileManager().isSpriteAnimation(spriteID);
    }

    protected BufferedImage getSprite(int spriteID) {
        return getGame().getTileManager().getSprite(spriteID);
    }
    protected BufferedImage getSprite(int spriteID, int animationIndex) {
        return getGame().getTileManager().getAniSprite(spriteID, animationIndex);
    }

    public Game getGame() {
        return game;
    }
}
