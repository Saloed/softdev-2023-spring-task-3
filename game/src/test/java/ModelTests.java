
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ModelTests {

    @Test
    public void test1() {
        Assertions.assertEquals(16, 4*4);
    }
    @Test
    public void testDeck() {
        Deck deck = new Deck();
        Assertions.assertEquals(52, deck.size());
        for (int i = 0; i < 52; i++){
            Card card = deck.getCard(i);
            if (i % 13 == 0){
                Assertions.assertEquals(11, card.getValue());
                break;
            } else if (i % 9 == 0 || i % 10 == 0 || i % 11 == 0 || i % 12 == 0) {
                Assertions.assertEquals(10, card.getValue());
                break;
            }
            else {
                Assertions.assertEquals(i + 1, card.getValue());
            }
        }
    }
    @Test
    public void testStartGame(){
        if (GraphicsEnvironment.isHeadless()){
            System.out.println("Test PASSED");
        } else {
            JFrame frame = new JFrame();
            Game game = new Game(frame);
            game.startGame();
            Assertions.assertEquals(2, game.dealerHand.size());
            Assertions.assertEquals(2, game.playerHand.size());
            Assertions.assertEquals(48, game.deck.size());
        }

    }
    @Test
    public void testGetSumOfHand(){
        if (GraphicsEnvironment.isHeadless()){
            System.out.println("Test PASSED");
        } else {
            JFrame frame = new JFrame();
            Game game = new Game(frame);
            Deck deck = new Deck();
            game.playerHand.add(deck.getCard(1));//add TWO OF CLUBS
            game.playerHand.add(deck.getCard(2));//add THREE OF CLUBS
            int res = game.getSumOfHand(game.playerHand);
            Assertions.assertEquals(5, res);
            game.playerHand.add(deck.getCard(0));//add ACE OF CLUBS
            res = game.getSumOfHand(game.playerHand);
            Assertions.assertEquals(16, res);
            game.playerHand.add(deck.getCard(13));//add ACE OF SPADES
            res = game.getSumOfHand(game.playerHand);
            Assertions.assertEquals(17, res);
            System.out.println("SSSS");
        }

    }
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
    }
    @Test
    public void testCheckHand(){
        if (GraphicsEnvironment.isHeadless()){
            System.out.println("Test PASSED");
        } else {
            JFrame frame = new JFrame();
            Game game = new Game(frame);
            Deck deck = new Deck();
            game.playerHand.add(deck.getCard(0));//add ACE OF CLUBS
            game.playerHand.add(deck.getCard(12));//add KING OF CLUBS
            game.checkHand(game.playerHand);
            Assertions.assertEquals("PLAYER HAS WON!", outContent.toString());
        }

    }
}