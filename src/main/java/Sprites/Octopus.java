package Sprites;

import java.awt.geom.Rectangle2D;

import static GameEngine.Game.Scale;
import static Sprites.Constants.Enemy.*;
import static Sprites.Constants.Directions.*;

public class Octopus extends Enemy {
    //Attack

    private int attackHitboxOffsetX;

    public Octopus(float x, float y) {
        super(x, y, OctopusWidthScaled, OctopusHeightScaled, Octopus);
        createHitbox(22, 19);
        createAttackHitbox();
    }

    private void createAttackHitbox() {
        attackHitbox = new Rectangle2D.Float(x, y, (int) (82 * Scale), (int) (19 * Scale));
        attackHitboxOffsetX = (int) (30 * Scale);
    }

    public void update(int[][] lvlData, MainCharacter mainCharacter) {
        updateBehavior(lvlData, mainCharacter);
        updateAnimation();
        updateAttackHitbox();
    }

    private void updateAttackHitbox() {
        attackHitbox.x = hitbox.x - attackHitboxOffsetX;
        attackHitbox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, MainCharacter mainCharacter) {
        if (firstUpdate) firstUpdate(lvlData);
        if (inAir) inAirUpdate(lvlData);
        else {
            switch (action) {
                case Idle:
                    changeAction(Run);
                    break;
                case Run:
                    if (canSeeMainCharacter(mainCharacter, lvlData)) {
                        followTheMainCharacter(mainCharacter);
                        if (inRangeForAttack(mainCharacter))
                            changeAction(Attack);
                    }
                    move(lvlData);
                    break;
                case Attack:
                    if (animationIndex == 0) attackChecked = false;
                    if (animationIndex == 3 && !attackChecked)
                        checkMainCharacterHit(mainCharacter, attackHitbox);
                    break;
                case Hit:
                    break;
            }
        }
    }

    public int flipX() {
        if (dir == Right) return w;
        else return 0;
    }

    public int flipW() {
        if (dir == Right) return -1;
        else return 1;
    }
}
