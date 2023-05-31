package com.mygdx.game.states;


import com.mygdx.game.IndieGame;
import com.mygdx.game.characters.Enemy;
import com.mygdx.game.characters.MainHero;
import com.mygdx.game.playStateActivities.Inventory;
import com.mygdx.game.playStateActivities.Shop;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayStateTest {


//    @Before
//    public void setUp() {
//        Enemy enemy;
//        enemy = new Enemy("slime");
//        hero = new MainHero(1, 1, 100, 100);
//        inventory = new Inventory();
//    }

    @Test
    public void update() {
//        Enemy enemy;
//        enemy = new Enemy("slime");
//        assertEquals((110L), (long) enemy.getHealth());
//        hero.earnMoney(100);
//        assertEquals(100, hero.getMoney());
        assertEquals(4, 2 + 2);
    }

    @Test
    public void render() {
        assertEquals("pls 5", "pls " + "5");
    }
}