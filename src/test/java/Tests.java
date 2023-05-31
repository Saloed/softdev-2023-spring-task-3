import GameEngine.Game;
import GameStates.Playing;
import Sprites.Crab;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    private Game game;
    private Playing playing;

    @BeforeEach
    public void setUp() {
        System.setProperty("java.awt.headless", "true");
        playing = new Playing(game);
    }

    @Test
    public void setUpSuccessful() {
        assertTrue(GraphicsEnvironment.isHeadless());
    }

    @Test
    void deadEnemy() {
        Crab crab = new Crab(0, 0);
        playing.getLevelManager().getLevel().getCrabs().add(crab);
        int currentHealth = crab.getCurrentHealth();
        assertEquals(20, currentHealth);
        int action = crab.getAction();
        assertEquals(0, action);
        crab.hurt(20);
        currentHealth = crab.getCurrentHealth();
        assertEquals(0, currentHealth);
        action = crab.getAction();
        assertEquals(4, action);
    }

    @Test
    void collectMoney() {
        int i = playing.getMainCharacter().getCurrentMoney();
        assertEquals(0, i);
        playing.getObjectManager().collectMoney();
        i = playing.getMainCharacter().getCurrentMoney();
        assertEquals(1, i);
    }

    @Test
    void lvlCompleted() {
        int money = playing.getMainCharacter().getCurrentMoney();
        assertEquals(0, money);
        int size = playing.getLevelManager().levels.get(0).getMoneys().size();
        for (int i = 0; i < size; i++) {
            playing.getObjectManager().collectMoney();
        }
        money = playing.getMainCharacter().getCurrentMoney();
        assertEquals(10, money);
    }
}
