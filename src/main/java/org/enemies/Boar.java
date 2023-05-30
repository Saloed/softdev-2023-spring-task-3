package org.enemies;

import org.managers.EnemyManager;

import static org.helpers.Constants.Enemies.BOAR;

public class Boar extends Enemy {

    public Boar(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, BOAR, em);
    }
}
