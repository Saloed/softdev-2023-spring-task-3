package Sprites;

import static GameEngine.Game.Scale;
import static HelperClasses.Constants.Enemy.*;
import static HelperClasses.Constants.Directions.*;

public class Crab extends Enemy {

    public Crab(float x, float y) {
        super(x, y, CrabWidthScaled, CrabHeightScaled, Crab);
        createHitbox(22, 19);
        createAttackHitbox(82, 19, 30);
        speed = 0.4f * Scale;
    }

    public void update(int[][] lvlData, MainCharacter mainCharacter) {
        updateBehavior(lvlData, mainCharacter);
        updateAnimation();
        updateAttackHitbox();
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
