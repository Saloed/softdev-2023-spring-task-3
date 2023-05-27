package org.events;

import java.util.ArrayList;

public class Wave { //Простой класс волны, состоящий из списка int (которые потом переводятся в enemyType)
    private ArrayList<Integer> enemyList;

    public Wave(ArrayList<Integer> enemyList) {
        this.enemyList = enemyList;
    }

    public ArrayList<Integer> getEnemyList() {
        return enemyList;
    }
}
