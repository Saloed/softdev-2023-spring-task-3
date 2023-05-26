package Sprites;

import GameStates.Playing;
import Scenes.DataProcessing;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static GameEngine.Game.Scale;
import static Sprites.Constants.Gravity;
import static Sprites.Constants.MainCharacter.*;
import static Scenes.HelpMethods.*;
import static Sprites.Constants.MainCharacter.Health.*;

public class MainCharacter extends Entity {

    //Animations
    private BufferedImage[][] animations;
    private int flipX = 0;
    private int flipW = 1;

    //Moving
    private boolean moving = false, attacking = false;
    private boolean left, right, jump;
    private int[][] lvlData;

    //Health Bar
    private BufferedImage statusBarImg;
    private int healthWidth = HealthBarWidth;

    private final Playing playing;

    public MainCharacter(float x, float y, int w, int h, Playing playing) {
        super(x, y, w, h);
        this.playing = playing;
        this.action = Idle;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.speed = 0.7f * Scale;
        animation();
        createHitbox(20, 28);
        createAttackHitbox();
    }

    public void setSpawnPoint(Point spawnPoint) {
        this.x = spawnPoint.x;
        this.y = spawnPoint.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    public void update() {
        updateHealthBar();
        if (currentHealth <= 0) {
            playing.gameOver(true);
            return;
        }
        updateAttackHitbox();
        updatePosition();
        if (attacking)
            checkAttack();
        updateAnimation();
        setAnimation();
    }

    private void checkAttack() {
        if (attackChecked || animationIndex != 1) return;
        attackChecked = true;
        playing.checkEnemyHit(attackHitbox);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * HealthBarWidth);
    }

    public void changeHealth(int hp) {
        currentHealth += hp;
        if (currentHealth <= 0) {
            currentHealth = 0;
            //gameOver();
        } else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    private void createAttackHitbox() {
        attackHitbox = new Rectangle2D.Float(x, y, (int) (20 * Scale), (int) (20 * Scale));
    }

    private void updateAttackHitbox() {
        if (right) {
            attackHitbox.x = hitbox.x + hitbox.width + Scale;
        } else if (left) {
            attackHitbox.x = hitbox.x - hitbox.width - Scale;
        }
        attackHitbox.y = hitbox.y + (Scale * 10);
    }

    private void animation() {
        BufferedImage image = DataProcessing.GetSprite(DataProcessing.MainCharacter);
        animations = new BufferedImage[6][5];

        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = image.getSubimage(i * 32, j * 32, 32, 32);
            }

        statusBarImg = DataProcessing.GetSprite(DataProcessing.HealthBar);
    }

    public void lvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (IsEntityOnFloor(hitbox, lvlData)) inAir = true;
    }

    public void render(Graphics g, int xLvlOffset) {
        float coordinateOffsetY = 7 * Scale;
        float coordinateOffsetX = 15 * Scale;
        g.drawImage(animations[action][animationIndex],
                (int) (hitbox.x - coordinateOffsetX) - xLvlOffset + flipX,
                (int) (hitbox.y - coordinateOffsetY),
                w * flipW, h, null);
        //drawHitbox(g, xLvlOffset);
        //drawAttackHitbox(g, xLvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, StatusBarX, StatusBarY, StatusBarWidth, StatusBarHeight, null);
        g.setColor(new Color(196, 16, 16));
        g.fillRect(HealthBarX + StatusBarX, HealthBarY + StatusBarY, healthWidth, HealthBarHeight);
    }

    private void updateAnimation() {
        animationTick++;
        int speed = 35;
        if (action == Attack) speed = 20;
        if (animationTick >= speed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= AmountOfFrames(action)) {
                animationIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updatePosition() {
        moving = false;
        if (jump) jump();
        if (!inAir)
            if ((!left && !right) || (left && right)) return;

        float xSpeed = 0;

        if (left) {
            xSpeed -= speed;
            flipX = 0;
            flipW = 1;
        }
        if (right) {
            xSpeed += speed;
            flipX = w;
            flipW = -1;
        }
        if (!inAir) {
            if (IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
        }
        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += Gravity;
                changeX(xSpeed);
            } else {
                hitbox.y = GetYPos(hitbox, airSpeed);
                float fallSpeed = 0.5f * Scale;
                if (airSpeed > 0) {
                    inAirReset();
                } else airSpeed = fallSpeed;
                changeX(xSpeed);
            }
        } else {
            changeX(xSpeed);
        }
        moving = true;
    }

    private void jump() {
        if (inAir) return;
        inAir = true;
        //Jumping
        airSpeed = -2.25f * Scale;
    }

    private void inAirReset() {
        inAir = false;
        airSpeed = 0;
    }

    private void changeX(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void setAnimation() {
        int startAnimation = action;

        if (moving) action = Run;
        else action = Idle;
        if (inAir) {
            action = Jump;
        }

        if (attacking) action = Attack;
        if (startAnimation != action) resetAnimationTick();
    }

    public void attack(boolean attacking) {
        this.attacking = attacking;
    }

    public void resetBool() {
        left = false;
        right = false;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetBool();
        inAir = false;
        attacking = false;
        moving = false;
        action = Idle;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;
        if (IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
}
