package org.enemies;

import org.managers.EnemyManager;

import static org.helpers.Constants.Enemies.GOLEM;

public class Golem extends Enemy {

    public Golem(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, GOLEM, em);
    }
}
