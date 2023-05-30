import java.util.ArrayList;
import javax.swing.*;
import java.awt.Font;
import java.util.List;

public class Game {

    List<Card> dealerHand;
    List<Card> playerHand;

    public boolean faceDown;
    public boolean dealerWon;
    public boolean done;
    public boolean busted;
    public volatile boolean roundOver;


    JFrame frame;
    Deck deck;
    GameComponent atmosphereComponent;
    GameComponent cardComponent;

    JButton btnHit;
    JButton btnStand;
    JButton btnHint;
    JButton btnExit;

    public Game(JFrame f) {
        deck = new Deck();
        deck.shuffleDeck();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();
        atmosphereComponent = new GameComponent(dealerHand, playerHand);
        frame = f;
        faceDown = true;
        dealerWon = true;
        done = false;
        busted = false;
        roundOver = false;
    }

    public void formGame() {

        frame.setTitle("BLACKJACK");
        frame.setSize(1130, 665);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        btnHit = new JButton("HIT");
        btnHit.setBounds(390, 550, 100, 50);
        btnHit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        btnStand = new JButton("STAND");
        btnStand.setBounds(520, 550, 100, 50);
        btnStand.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        btnHint = new JButton("HINT");
        btnHint.setBounds(650, 550, 100, 50);
        btnHint.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        btnExit = new JButton("EXIT CASINO");
        btnExit.setBounds(930, 240, 190, 50);
        btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        frame.add(btnHit);
        frame.add(btnStand);
        frame.add(btnHint);
        frame.add(btnExit);

        btnExit.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Better luck next time, but always remember to gamble responsibly!");
            System.exit(0);
        });

        atmosphereComponent = new GameComponent(dealerHand, playerHand);
        atmosphereComponent.setBounds(0, 0, 1130, 665);
        frame.add(atmosphereComponent);
        frame.setVisible(true);
    }

    public void startGame() {

        for(int i = 0; i<2; i++) {
            dealerHand.add(deck.getCard(i));
        }
        for(int i = 2; i<4; i++) {
            playerHand.add(deck.getCard(i));
        }
        for (int i = 0; i < 4; i++) {
            deck.removeCard(0);
        }
    }
    public void activatingBtns(){

        cardComponent = new GameComponent(dealerHand, playerHand);
        cardComponent.setBounds(0, 0, 1130, 665);
        frame.add(cardComponent);
        frame.setVisible(true);

        checkHand(dealerHand);
        checkHand(playerHand);

        btnHit.addActionListener(e -> {
            addCard(playerHand);
            checkHand(playerHand);
            if (getSumOfHand(playerHand)<17 && getSumOfHand(dealerHand)<17){
                addCard(dealerHand);
                checkHand(dealerHand);
            }
        });

        btnHint.addActionListener(e -> JOptionPane.showMessageDialog(frame, "The probability of drawing the desired card:" + decision(playerHand, dealerHand)));
        btnStand.addActionListener(e -> {
            while (getSumOfHand(dealerHand)<17) {
                addCard(dealerHand);
                checkHand(dealerHand);
            }
            if ((getSumOfHand(dealerHand)<21) && getSumOfHand(playerHand)<21) {
                if(getSumOfHand(playerHand) > getSumOfHand(dealerHand)) {
                    faceDown = false;
                    dealerWon = false;
                    JOptionPane.showMessageDialog(frame, "PLAYER HAS WON BECAUSE OF A BETTER HAND!");
                    roundOver = true;
                }
                else {
                    faceDown = false;
                    JOptionPane.showMessageDialog(frame, "DEALER HAS WON BECAUSE OF A BETTER HAND!");
                    roundOver = true;
                }
            }
        });
    }

    public void checkHand (List<Card> hand) {
        if (hand.equals(playerHand)) {
            if(getSumOfHand(hand) == 21){
                faceDown = false;
                dealerWon = false;
                done = true;
                System.out.print("PLAYER HAS WON!");
                roundOver = true;
            }
            else if (getSumOfHand(hand) > 21) {
                faceDown = false;
                busted = true;
                System.out.print("DEALER HAS WON!");
                roundOver = true;
            }
        }
        else {
            if(getSumOfHand(hand) == 21) {
                faceDown = false;
                done = true;
                System.out.print("DEALER HAS WON!");
                roundOver = true;
            }
            else if (getSumOfHand(hand) > 21) {
                faceDown = false;
                dealerWon = false;
                busted = true;
                System.out.print("PLAYER HAS WON!");
                roundOver = true;
            }
        }
    }

    public void addCard(List<Card> hand) {
        hand.add(deck.getCard(0));
        deck.removeCard(0);
        faceDown = true;
    }

    public boolean hasAceInHand(List<Card> hand) {
        for (Card card : hand) {
            if (card.getValue() == 11) {
                return true;
            }
        }
        return false;
    }

    public int aceCountInHand(List<Card> hand){
        int aceCount = 0;
        for (Card card : hand) {
            if (card.getValue() == 11) {
                aceCount++;
            }
        }
        return aceCount;
    }

    public int getSumWithHighAce(List<Card> hand) {
        int handSum = 0;
        for (Card card : hand) {
            handSum += card.getValue();
        }
        return handSum;
    }

    public int getSumOfHand (List<Card> hand) {
        int sumOfHand = 0;
        if(hasAceInHand(hand)) {
            if(getSumWithHighAce(hand) <= 21) {
                return getSumWithHighAce(hand);
            }
            else{
                for (int i = 0; i < aceCountInHand(hand); i++) {
                    sumOfHand = getSumWithHighAce(hand)-(i+1)*10;
                    if(sumOfHand <= 21) {
                        break;
                    }
                }
            }
        }
        else {
            for (Card card : hand) {
                sumOfHand += card.getValue();
            }
        }
        return sumOfHand;
    }

    public  double decision (List<Card> phand, List<Card> dhand) {
        int n = 21 - getSumOfHand(phand);
        double dec = 0.0;
        for (int i = 1; i <= n; i++) {
            int t = 4;
            if (i > 10 && i != 11) {
                break;
            } else if (i == 10) {
                t *= 4;
            }
            for (Card j: phand){
                int s = j.getValue();
                if (i == s){
                    t--;
                    if (t == 0){
                        break;
                    }
                }
            }
            double a = (double) t / deck.size();
            double b = (t > dhand.size())? (double) (t - dhand.size()) / deck.size() : 0.0;
            dec += (a + b)/2;
        }
        return dec;
    }
}


