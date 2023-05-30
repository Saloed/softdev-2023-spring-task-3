package Sprites;

import static GameEngine.Game.Scale;
import static GameEngine.Game.TileSize;
import static HelperClasses.Constants.Directions.Left;
import static HelperClasses.Constants.Directions.Right;
import static HelperClasses.Constants.Enemy.*;
import static HelperClasses.HelpMethods.CanMoveHere;
import static HelperClasses.HelpMethods.IsFloor;

public class Octopus extends Enemy{

    public Octopus(float x, float y) {
        super(x, y, OctopusWidthScaled, OctopusHeightScaled, Octopus);
        createHitbox( 26, 24);
        createAttackHitbox(30, 24, 35);
        speed = 0.35f * Scale;
        attackRange = (int) (TileSize * 1.5);
    }

    public void update(int[][] lvlData, MainCharacter mainCharacter) {
        updateBehavior(lvlData, mainCharacter);
        updateAnimation();
        updateAttackHitbox();
        flipAttackHitbox();
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
                    if (animationIndex == 3) {
                        if (!attackChecked) {
                            checkMainCharacterHit(mainCharacter, attackHitbox);
                        }
                        attackMove(lvlData);
                    }
                    break;
                case Hit:
                    break;
            }
        }
    }
    protected void attackMove(int[][] lvlData) {
        float xSpeed;
        if (dir == Left) xSpeed = -speed;
        else xSpeed = speed;

        if (CanMoveHere(hitbox.x + xSpeed * 5, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed * 5, lvlData)) {
                hitbox.x += xSpeed * 5;
                return;
            }
        changeAction(Idle);
    }

    public int flipX() {
        if (dir == Right) return 0;
        else return w;
    }

    public int flipW() {
        if (dir == Right) return 1;
        else return -1;
    }
}
