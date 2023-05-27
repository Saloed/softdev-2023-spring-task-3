package org.enemies;

import org.managers.EnemyManager;

import static org.helpers.Constants.Enemies.SLIME;

public class Slime extends Enemy {

    public Slime(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, SLIME, em);
    }
}
