import java.util.Scanner;

public class Blackjack {

    public static void main(String[] args){
        Deck playingDeck = new Deck();
        playingDeck.createFullDeck();
        playingDeck.shuffle();

        Deck playerDeck = new Deck();
        Deck dealerDeck = new Deck();

        double playerMoney = 100.00;

        Scanner userInput = new Scanner(System.in);

        while (playerMoney > 0){
            System.out.println("Баланс: " + playerMoney + "\n" + "Твоя ставка:");
            double playerBet = userInput.nextDouble();
            while (playerBet > playerMoney){
                playerBet = userInput.nextDouble();
            }

            playerDeck.draw(playingDeck);
            playerDeck.draw(playingDeck);

            dealerDeck.draw(playingDeck);
            dealerDeck.draw(playingDeck);

            boolean endRoundP = false;
            boolean endRoundD = false;

            while (true){
                System.out.print("Твоя рука: ");
                System.out.println(playerDeck.toString());
                System.out.println("Очки: " + playerDeck.cardsValue() + "\n----------" );

                System.out.println("Дилер:" + dealerDeck.getCard(0).toString() + " и ...");

                if (playerDeck.cardsValue() >= 21){
                    endRoundP = true;
                }

                if (!endRoundP) {
                    System.out.println("(1): ЕЩЁ, (2): ПАС");
                    int response = userInput.nextInt();
                    if (response == 1){
                        playerDeck.draw(playingDeck);
                        System.out.println("Карта: " + playerDeck.getCard(playerDeck.deckSize() - 1).toString()+"\n----------");

                    } else if (response == 2){
                        if (endRoundD) {
                            break;
                        } else {
                            endRoundP = true;
                        }
                    }
                }
                if (dealerDeck.hit(playerDeck.deckSize(), playingDeck.deckSize())){
                    dealerDeck.draw(playingDeck);
                } else {
                    if (endRoundP) {
                        break;
                    } else {
                        endRoundD = true;
                    }
                }
            }

            if (playerDeck.cardsValue() > 21){
                System.out.println("LOSE !!!");
                System.out.println("Твои очки: " + playerDeck.cardsValue());
                System.out.println("Дилера очки: " + dealerDeck.cardsValue());
                playerMoney -= playerBet;
            }
            else if (playerDeck.cardsValue() == dealerDeck.cardsValue()){
                System.out.println("Nichya");
                System.out.println("Твои очки: " + playerDeck.cardsValue());
                System.out.println("Дилера очки: " + dealerDeck.cardsValue());
            }
            else if (playerDeck.cardsValue() > dealerDeck.cardsValue() || dealerDeck.cardsValue() > 21){
                System.out.println("WIN !!!");
                System.out.println("Твои очки: " + playerDeck.cardsValue());
                System.out.println("Дилера очки: " + dealerDeck.cardsValue());
                playerMoney += playerBet;
            }
            //"Но не "очко" обычно губит, а к одиннадцати туз"
            else if (dealerDeck.cardsValue() > playerDeck.cardsValue()){
                System.out.println("LOSE !!!");
                System.out.println("Твои очки: " + playerDeck.cardsValue());
                System.out.println("Дилера очки: " + dealerDeck.cardsValue());
                playerMoney -= playerBet;
            }

            playerDeck.moveAllToDeck(playingDeck);
            dealerDeck.moveAllToDeck(playingDeck);
            System.out.println("Конец раздачи\n");
        }
        System.out.println("Баланс:" + playerMoney + "\n-------------");
        System.out.println("Чертовски неудачное казино, это просто невероятно!");
    }
}
