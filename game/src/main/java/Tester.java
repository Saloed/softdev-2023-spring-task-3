import javax.swing.*;

public class Tester {
    public JFrame menuFrame = new JFrame();
    public static JFrame gameFrame = new JFrame();
    private int playerScore = 0;
    private int dealerScore = 0;
    public int currentBalance = 100;
    public static Game newGame = new Game(gameFrame);
    private boolean isFirstTime = true;

    public enum STATE{
        MENU,
        GAME
    }

    public static STATE currentState = STATE.MENU;

    public static void main(String[] args) {
        Tester tester = new Tester();
        if(currentState == STATE.MENU) {
            tester.openMenu();
        }
    }

    public void openMenu() {
        menuFrame.setTitle("BLACKJACK");
        menuFrame.setSize(1130, 665);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setResizable(false);

        OptionsComponent beginningComponent = new OptionsComponent();
        menuFrame.add(beginningComponent);
        menuFrame.setVisible(true);
    }

    public void btnPlay() {
        currentState = Tester.STATE.GAME;
        menuFrame.dispose();
        gameRefreshThread.start();
        gameCheckThread.start();
    }

    public Thread gameRefreshThread = new Thread(() -> {
        while(true){
            newGame.atmosphereComponent.refresh(currentBalance, playerScore, dealerScore-1, newGame.faceDown);
        }
    });

    public Thread gameCheckThread = new Thread(() -> {
        while(true) {
            if (isFirstTime||newGame.roundOver) {
                if (newGame.dealerWon){
                    dealerScore++;
                    currentBalance-= GameComponent.currentBet;
                    if (newGame.done){
                        JOptionPane.showMessageDialog(newGame.frame, "DEALER HAS DONE BLACKJACK! DEALER HAS WON!");
                    }
                    else if (newGame.busted){
                        JOptionPane.showMessageDialog(newGame.frame, "PLAYER HAS BUSTED! DEALER HAS WON!");
                    }
                }
                else {
                    playerScore++;
                    currentBalance+= GameComponent.currentBet*2;
                    if (newGame.done){
                        JOptionPane.showMessageDialog(newGame.frame, "PLAYER HAS DONE BLACKJACK! PLAYER HAS WON!");
                    }
                    else if (newGame.busted){
                        JOptionPane.showMessageDialog(newGame.frame, "DEALER HAS BUSTED! PLAYER HAS WON!");
                    }
                }
                if (currentBalance <= 0){
                    dealerScore = 1;
                    playerScore = 0;
                    currentBalance = 100;
                    JOptionPane.showMessageDialog(gameFrame, "A NEW GAME HAS STARTED");

                }
                gameFrame.getContentPane().removeAll();
                newGame = new Game(gameFrame);
                newGame.formGame();

                isFirstTime = false;
            }
        }
    });
}

