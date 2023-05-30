package org.enemies;

import org.managers.EnemyManager;

import static org.helpers.Constants.Enemies.GOBLIN;

public class Goblin extends Enemy {

    public Goblin(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, GOBLIN, em);
    }
}
