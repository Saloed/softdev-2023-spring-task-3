package Sprites;

import java.awt.geom.Rectangle2D;

import static GameEngine.Game.Scale;
import static GameEngine.Game.TileSize;
import static HelperClasses.HelpMethods.*;
import static HelperClasses.Constants.Directions.Left;
import static HelperClasses.Constants.Directions.Right;
import static HelperClasses.Constants.Enemy.*;
import static HelperClasses.Constants.Gravity;

public abstract class Enemy extends Entity {

    //Animations

    protected int enemyType;

    //Moving

    protected boolean firstUpdate = true;
    protected int dir = Left;
    protected int tileY;

    //Attack

    protected int attackRange = TileSize;
    protected int attackHitboxOffsetX;

    //Health

    protected boolean active = true;

    public Enemy(float x, float y, int w, int h, int enemyType) {
        super(x, y, w, h);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void updateAnimation() {
        animationTick++;
        animationSpeed = 25;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= AmountOfFrames(enemyType, action)) {
                animationIndex = 0;
                switch (action) {
                    case Attack:
                    case Hit:
                        action = Idle;
                        break;
                    case Dead:
                        active = false;
                        break;
                }
            }
        }
    }

    protected void firstUpdate(int[][] lvlData) {
        if (IsEntityOnFloor(hitbox, lvlData)) inAir = true;
        firstUpdate = false;
    }

    protected void inAirUpdate(int[][] lvlData) {
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += Gravity;
        } else {
            inAir = false;
            hitbox.y = GetYPos(hitbox, airSpeed);
            tileY = (int) (hitbox.y / TileSize);
        }
    }

    protected void createAttackHitbox(int w, int h, int attackHitboxOffsetX) {
        attackHitbox = new Rectangle2D.Float(x, y, (int) (w * Scale), (int) (h * Scale));
        this.attackHitboxOffsetX = (int) (attackHitboxOffsetX * Scale);
    }

    protected void updateAttackHitbox() {
        attackHitbox.x = hitbox.x - attackHitboxOffsetX;
        attackHitbox.y = hitbox.y;
    }

    protected void flipAttackHitbox() {
        if (dir == Right)
            attackHitbox.x = hitbox.x + hitbox.width + Math.abs(hitbox.width - attackHitboxOffsetX);
        else attackHitbox.x = hitbox.x - attackHitboxOffsetX;
        attackHitbox.y = hitbox.y;
    }

    protected void move(int[][] lvlData) {
        float xSpeed;

        if (dir == Left)
            xSpeed = -speed;
        else xSpeed = speed;
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        changeDir();
    }

    protected void checkMainCharacterHit(MainCharacter mainCharacter, Rectangle2D.Float attackHitbox) {
        if (attackHitbox.intersects(mainCharacter.hitbox))
            mainCharacter.changeHealth(-GetAmountOfDamage(enemyType));
        attackChecked = true;
    }

    public void hurt(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) changeAction(Dead);
        else changeAction(Hit);
    }

    protected boolean canSeeMainCharacter(MainCharacter mainCharacter, int[][] lvlData) {
        int mcTileY = (int) (mainCharacter.hitbox.y / TileSize);
        if (mcTileY == tileY)
            if (inRange(mainCharacter))
                return IsWayClear(lvlData, hitbox, mainCharacter.hitbox, tileY);
        return false;
    }

    protected void followTheMainCharacter(MainCharacter mainCharacter) {
        if (mainCharacter.hitbox.x > hitbox.x) dir = Right;
        else dir = Left;
    }

    protected boolean inRange(MainCharacter mainCharacter) {
        int abs = (int) Math.abs(mainCharacter.hitbox.x - hitbox.x);
        return abs <= attackRange * 5;
    }

    protected boolean inRangeForAttack(MainCharacter mainCharacter) {
        int abs = (int) Math.abs(mainCharacter.hitbox.x - hitbox.x);
        return abs <= attackRange;
    }

    protected void changeAction(int enemyAction) {
        this.action = enemyAction;
        animationTick = 0;
        animationIndex = 0;
    }

    protected void changeDir() {
        if (dir == Left)
            dir = Right;
        else dir = Left;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        changeAction(Idle);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }
}
