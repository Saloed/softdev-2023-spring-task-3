package Sprites;

import Scenes.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static GameEngine.Game.Scale;
import static Sprites.Constants.MainCharacter.*;
import static Scenes.HelpMethods.*;

public class MainCharacter extends Entity {
    private BufferedImage[][] animations;
    private int animationTick;
    private int animationIndex;
    private int mainCharacterAction = idle;
    private boolean moving = false, attacking = false;
    private boolean left, down, right, up;
    private int[][] lvlData;

    public MainCharacter(float x, float y, int w, int h) {
        super(x, y, w, h);
        animation();
        createHitbox(x, y, 26 * Scale, 35 * Scale);
    }

    public void update() {
        updatePosition();
        updateAnimation();
        setAnimation();
    }

    private void animation() {
        BufferedImage image = LoadSave.GetSprite(LoadSave.MainCharacter);
        animations = new BufferedImage[4][5];

        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = image.getSubimage(i * 48, j * 48, 48, 48);
            }
    }

    public void lvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public void render(Graphics g) {
        float coordinateOffsetY = 6 * Scale;
        float coordinateOffsetX = 10 * Scale;
        g.drawImage(animations[mainCharacterAction][animationIndex], (int) (hitbox.x - coordinateOffsetX), (int) (hitbox.y - coordinateOffsetY), w, h, null);
        drawHitbox(g);
    }

    private void updateAnimation() {
        animationTick++;
        int speed = 40;
        if (animationTick >= speed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= AmountOfFrames(mainCharacterAction)) {
                animationIndex = 0;
                attacking = false;
            }
        }
    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updatePosition() {
        moving = false;
        if (!left && !right && !down && !up) return;

        float xSpeed = 0, ySpeed = 0;
        float mainCharacterSpeed = 0.75f;

        if (left && !right) xSpeed = -mainCharacterSpeed;
        else if (right && !left) xSpeed = mainCharacterSpeed;

        if (down && !up) ySpeed = mainCharacterSpeed;
        else if (up && !down) ySpeed = -mainCharacterSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
            moving = true;
        }
    }

    private void setAnimation() {
        int startAnimation = mainCharacterAction;

        if (moving) mainCharacterAction = run;
        else mainCharacterAction = idle;

        if (attacking) mainCharacterAction = attack;
        if (startAnimation != mainCharacterAction) resetAnimationTick();
    }

    public void attack(boolean attacking) {
        this.attacking = attacking;
    }

    public void resetBool() {
        left = false;
        down = false;
        right = false;
        up = false;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }
}
