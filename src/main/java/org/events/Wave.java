package org.events;

import java.util.List;

public class Wave { //Простой класс волны, состоящий из списка int (которые потом переводятся в enemyType)
    private List<Integer> enemyList;

    public Wave(List<Integer> enemyList) {
        this.enemyList = enemyList;
    }

    public List<Integer> getEnemyList() {
        return enemyList;
    }
}
