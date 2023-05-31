import UI.Screen;
import object.MainCharacter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class ScreenTest {

    private Screen screen;

    @BeforeEach
    public void setUp() {
        screen = new Screen();
    }


    @Test
    public void testUpdate_GamePlayState() {
        screen.setGameState(Screen.GAME_PLAY_STATE);
        MainCharacter mainCharacter = screen.getMainCharacter();
        mainCharacter.setAlive(true);

        screen.update();

        assertTrue(mainCharacter.getAlive());
    }

    @Test
    public void testUpdate_GameOverState() {
        screen.setGameState(Screen.GAME_OVER_STATE);
        MainCharacter mainCharacter = screen.getMainCharacter();
        mainCharacter.setAlive(false);

        screen.update();

        assertFalse(mainCharacter.getAlive());
    }

    @Test
    public void testPlusScore() {
        screen.setScore(50);

        screen.plusScore();

        assertEquals(50.3f, screen.getScore());
    }

    @Test
    public void testResetGame() {
        screen.setScore(50);
        screen.setHighScore(100);
        MainCharacter mainCharacter = screen.getMainCharacter();
        mainCharacter.setAlive(false);
        mainCharacter.setY(120);

        screen.resetGame();

        assertEquals(0, screen.getScore());
        assertEquals(100, screen.getHighScore());
        assertTrue(mainCharacter.getAlive());
        assertEquals(60, mainCharacter.getY());
    }

    @Test
    public void testKeyPressed_GameFirstState_SpaceKey() {
        screen.setGameState(Screen.GAME_FIRST_STATE);
        KeyEvent keyEvent = createKeyEvent(KeyEvent.VK_SPACE, KeyEvent.KEY_PRESSED);

        screen.keyPressed(keyEvent);

        assertEquals(Screen.GAME_PLAY_STATE, screen.getGameState());
    }

    @Test
    public void testKeyPressed_GameOverState_SpaceKey() {
        screen.setGameState(Screen.GAME_OVER_STATE);
        KeyEvent keyEvent = createKeyEvent(KeyEvent.VK_SPACE, KeyEvent.KEY_PRESSED);

        screen.keyPressed(keyEvent);

        assertEquals(Screen.GAME_PLAY_STATE, screen.getGameState());
    }


    private @NotNull KeyEvent createKeyEvent(int keyCode, int keyEventType) {
        Component source = new JPanel();
        int modifiers = 0;
        long when = System.currentTimeMillis();
        int id = KeyEvent.KEY_PRESSED;
        return new KeyEvent(source, keyEventType, when, modifiers, keyCode, KeyEvent.CHAR_UNDEFINED);
    }
}

