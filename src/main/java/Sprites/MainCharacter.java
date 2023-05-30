package Sprites;

import GameStates.Playing;
import HelperClasses.DataProcessing;
import Levels.Level;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static GameEngine.Game.Scale;
import static HelperClasses.Constants.Gravity;
import static HelperClasses.Constants.MainCharacter.*;
import static HelperClasses.HelpMethods.*;
import static HelperClasses.Constants.MainCharacter.StatusBar.*;

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

    // Moneys

    private int moneyWidth = 0;
    private int maxMoneyWidth;
    private int currentMoney;

    private final Playing playing;

    public MainCharacter(float x, float y, int w, int h, Playing playing) {
        super(x, y, w, h);
        this.playing = playing;
        this.action = Idle;
        this.maxHealth = 100;
        this.maxMoneyWidth = playing.getLevelManager().getLevel().getMoneys().size();
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

    public void setMaxMoneyWidth(Level level) {
        maxMoneyWidth = level.getMoneys().size();
    }

    public void update() {
        updateHealthBar();
        if (currentHealth <= 0) {
            if (action != Dead) {
                action = Dead;
                animationIndex = 0;
                animationTick = 0;
                playing.setMainCharacterDead(true);
            } else if (animationIndex == AmountOfFrames(Dead) - 1 && animationTick >= animationSpeed - 1) {
                playing.setGameOver(true);
            } else updateAnimation();
            return;
        }
        updateMoneyBar();
        if (currentMoney >= maxMoneyWidth) {
            playing.setLevelCompleted(true);
            return;
        }
        updateAttackHitbox();
        updatePosition();
        if (moving) {
            checkObjectContact();
            checkSpikesContact();
            checkInsideWater();
        }
        if (attacking) checkAttack();
        updateAnimation();
        setAnimation();
    }

    private void checkAttack() {
        if (attackChecked || animationIndex != 1) return;
        attackChecked = true;
        playing.checkEnemyHit(attackHitbox);
    }

    private void checkObjectContact() {
        playing.checkObjectContact(hitbox);
    }

    private void checkSpikesContact() {
        playing.checkSpikesContact(this);
    }

    private void checkInsideWater() {
        if (IsInWater(hitbox, playing.getLevelManager().getLevel().getLevelData()))
            currentHealth = 0;
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * HealthBarWidth);
    }

    private void updateMoneyBar() {
        moneyWidth = (int) ((currentMoney / (float) maxMoneyWidth) * MoneyBarWidth);
    }

    public void changeHealth(int hp) {
        currentHealth += hp;
        if (currentHealth <= 0) {
            currentHealth = 0;
        } else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    public void killMainCharacter() {
        currentHealth = 0;
    }

    public void changeMoney(int value) {
        currentMoney += value;
        if (currentMoney >= maxMoneyWidth) currentMoney = maxMoneyWidth;
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

        statusBarImg = DataProcessing.GetSprite(DataProcessing.HealthMoneyBar);
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
        drawHealthBar(g);
        drawMoneyBar(g);
    }

    private void drawHealthBar(Graphics g) {
        g.drawImage(statusBarImg, StatusBarX, StatusBarY, StatusBarWidth, StatusBarHeight, null);
        g.setColor(new Color(196, 16, 16));
        g.fillRect(HealthBarX + StatusBarX, HealthBarY + StatusBarY, healthWidth, HealthBarHeight);
    }

    private void drawMoneyBar(Graphics g) {
        g.setColor(new Color(255, 221, 64));
        g.fillRect(MoneyBarX + StatusBarX, MoneyBarY + StatusBarY, moneyWidth, MoneyBarHeight);
    }

    private void updateAnimation() {
        animationTick++;
        animationSpeed = 35;
        if (action == Attack) animationSpeed = 20;
        if (animationTick >= animationSpeed) {
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
            } else {
                hitbox.y = GetYPos(hitbox, airSpeed);
                float fallSpeed = 0.5f * Scale;
                if (airSpeed > 0) {
                    inAirReset();
                } else airSpeed = fallSpeed;
            }
        }
        changeX(xSpeed);
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

    public void setLeft(boolean left) {
        this.left = left;
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
        airSpeed = 0f;
        action = Idle;
        currentHealth = maxHealth;
        currentMoney = 0;
        setSpawnPoint(playing.getLevelManager().getLevel().getSpawnPoint());

        hitbox.x = x;
        hitbox.y = y;
        if (IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
}
