import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;



public class GameComponent extends JComponent implements MouseListener {
    public BufferedImage backgroundImage;
    public BufferedImage chip;
    private final ArrayList<Card> dealerHand;
    private final ArrayList<Card> playerHand;
    private int dealerScore;
    private int playerScore;
    public boolean faceDown = true;
    public static boolean betMade = false;
    private int currentBalance;
    public static int currentBet;
    public GameComponent(ArrayList<Card> dH, ArrayList<Card> pH) {
        dealerHand = dH;
        playerHand = pH;
        dealerScore = 0;
        playerScore = 0;
        currentBalance = 100;
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        try {
            backgroundImage = ImageIO.read(new File("images/background.png"));
            chip = ImageIO.read(new File("images/chip.png"));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }

        g2.drawImage(backgroundImage, 0, 0, null);
        g2.drawImage(chip, 50, 300, null);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        g2.drawString("DEALER", 515, 50);
        g2.drawString("PLAYER", 515, 380);
        g2.drawString("DEALER WON: ", 50, 100);
        g2.drawString(Integer.toString(dealerScore), 300, 100);
        g2.drawString("PLAYER WON: ", 50, 150);
        g2.drawString(Integer.toString(playerScore), 300, 150);
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        g2.drawString("bet an amount by clicking the chip below.", 50, 270);
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g2.drawString("CURRENT BALANCE: " + currentBalance, 50, 570);


        try {
            for (int i = 0; i < dealerHand.size(); i++) {
                if (i == 0) {
                    dealerHand.get(i).printCard(g2, true, faceDown, i);
                }
                else {
                    dealerHand.get(i).printCard(g2, true, false, i);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            for (int i = 0; i < playerHand.size(); i++) {
                playerHand.get(i).printCard(g2, false, false, i);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void refresh(int cB, int uS, int dS, boolean fD) {
        currentBalance = cB;
        playerScore = uS;
        dealerScore = dS;
        faceDown = fD;
        this.repaint();
    }

    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if(mouseX>= 50 && mouseX<=200 && mouseY>=300 && mouseY<=450) {
            betMade = true;
            String[] options = new String[] {"1", "5", "10", "25","50", "75", "100"};

            while (true){
                int response = JOptionPane.showOptionDialog(null, "Please enter your betting amount!", "BETTING",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                switch (response) {
                    case 0 -> currentBet = 1;
                    case 1 -> currentBet = 5;
                    case 2 -> currentBet = 10;
                    case 3 -> currentBet = 25;
                    case 4 -> currentBet = 50;
                    case 5 -> currentBet = 75;
                    case 6 -> currentBet = 100;
                }

                if (currentBalance - currentBet >= 0){
                    currentBalance -= currentBet;
                    break;
                }
                else {
                    JFrame frame = new JFrame();
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setResizable(false);
                    JOptionPane.showMessageDialog(frame, "YOU can't bet that much.");
                }
            }


            Tester.newGame.startGame();
        }

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseClicked(MouseEvent e) {

    }

}