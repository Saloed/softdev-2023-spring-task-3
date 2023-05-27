import org.enemies.*;

import org.helpers.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.main.Game;
import org.main.GameStates;
import org.objects.Projectile;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.main.GameStates.*;
import static org.main.LangStates.*;
import static org.main.LangTexts.bMenuStr;
import static org.main.LangTexts.bSaveStr;

public class AppTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        System.setProperty("java.awt.headless", "true");
        game = new Game();
    }

    @Test
    public void whenSetUpSuccessful_thenHeadlessIsTrue() {
        assertTrue(GraphicsEnvironment.isHeadless());
    }

    @Test
    void hurtEnemyUntilDead() {
        Golem golem = new Golem(0, 0, 0, game.getPlaying().getEnemyManager());
        game.getPlaying().getEnemyManager().getEnemies().add(golem);
        int i = game.getPlaying().getEnemyManager().getAmountOfAllLiveEnemies();
        assertEquals(1, i);
        golem.hurt(250);
        i = game.getPlaying().getEnemyManager().getAmountOfAllLiveEnemies();
        assertEquals(0, i);
    }

    @Test
    void createProjThenKillEnemy() {
        Projectile arrow = new Projectile(0, 0, 0, 0, 250, 0, 0, Constants.Projectiles.ARROW);
        game.getPlaying().getProjectileManager().getProjectiles().add(arrow);
        Golem golem = new Golem(0, 0, 0, game.getPlaying().getEnemyManager());
        game.getPlaying().getEnemyManager().getEnemies().add(golem);

        assert (arrow.getPos().x == golem.getX());
        assert (arrow.getPos().y == golem.getY());

        assertEquals(1.0, golem.getHealthBarFloat()); //Процент от общего здоровья
        game.getPlaying().getProjectileManager().update();

        assertFalse(golem.isAlive());
    }

    @Test
    void createBombThenHurtAllNearbyEnemies() {
        Projectile bomb = new Projectile(0, 0, 0, 0, 250, 0, 0, Constants.Projectiles.BOMB);
        game.getPlaying().getProjectileManager().getProjectiles().add(bomb);
        Golem golem = new Golem(0, 0, 0, game.getPlaying().getEnemyManager());
        Boar boar = new Boar(10, 0, 1, game.getPlaying().getEnemyManager());
        Goblin goblin = new Goblin(20, 0, 2, game.getPlaying().getEnemyManager());
        game.getPlaying().getEnemyManager().getEnemies().add(golem);
        game.getPlaying().getEnemyManager().getEnemies().add(boar);
        game.getPlaying().getEnemyManager().getEnemies().add(goblin);

        assertEquals(1.0, golem.getHealthBarFloat());
        assertEquals(1.0, boar.getHealthBarFloat());
        assertEquals(1.0, goblin.getHealthBarFloat());

        game.getPlaying().update();

        assertFalse(golem.isAlive());
        assertFalse(boar.isAlive());
        assertFalse(goblin.isAlive());
    }

    @Test
    void createWaveKillAllGetWinScreen() {
        SetGameState(GameStates.PLAYING);
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 360; i++) {
                game.getPlaying().getWaveManager().update();

                game.getPlaying().update();
            }
            assert (game.getPlaying().getEnemyManager().getEnemies().size() > 0);
            assertEquals(GameStates.PLAYING, gameState);
            for (Enemy e : game.getPlaying().getEnemyManager().getEnemies()) {
                e.kill();
            }
        }

        game.getPlaying().update();

        assertEquals(0, game.getPlaying().getEnemyManager().getAmountOfAllLiveEnemies());
        assertEquals(GameStates.GAME_WIN, gameState);
    }

    @Test
    void checkLanguage() {
        SetLangState(ENGLISH);
        assertEquals("Menu", bMenuStr.toString());
        assertEquals("Save", bSaveStr.toString());

        SetLangState(RUSSIAN);
        assertEquals("Меню", bMenuStr.toString());
        assertEquals("Сохранить", bSaveStr.toString());
    }
}
