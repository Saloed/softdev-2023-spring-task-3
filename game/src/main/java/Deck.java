import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards;

    public Deck(){
        this.cards = new ArrayList<>();
    }
    //Создает колоду карт
    public void createFullDeck(){
        for (Suit cardSuit : Suit.values()){
            for (Value cardValue: Value.values()){
                this.cards.add(new Card(cardSuit, cardValue));
            }
        }
    }
    //Выдает всю колоду в виде строки
    public String toString(){
        StringBuilder cardListOutput = new StringBuilder();
        for (Card aCard: this.cards){
            cardListOutput.append("\n").append(aCard.toString());
        }
        return cardListOutput.toString();
    }

    public void shuffle(){
        ArrayList<Card> tmpDeck = new ArrayList<>();
        Random random = new Random();
        int randomCardIndex;
        int originalSize = this.cards.size();
        for (int i = 0; i < originalSize; i++){
            randomCardIndex = random.nextInt(this.cards.size());
            tmpDeck.add(this.cards.get(randomCardIndex));
            this.cards.remove(randomCardIndex);
        }
        this.cards = tmpDeck;
    }

    public void removeCard (int i){
        this.cards.remove(i);
    }

    public Card getCard(int i){
        return this.cards.get(i);
    }

    public void addCard(Card addCard){
        this.cards.add(addCard);
    }

    public void draw(Deck comingFrom){
        this.cards.add(comingFrom.getCard(0));
        comingFrom.removeCard(0);
    }

    public int deckSize(){
        return this.cards.size();
    }

    public int cardsValue(){
        int totalValue = 0;
        ArrayList<Integer> val;

        for (Card aCard: this.cards){
            switch (aCard.getValue()){
                case TWO -> totalValue += 2;
                case THREE -> totalValue += 3;
                case FOUR -> totalValue += 4;
                case FIVE -> totalValue += 5;
                case SIX -> totalValue += 6;
                case SEVEN -> totalValue += 7;
                case EIGHT -> totalValue += 8;
                case NINE -> totalValue += 9;
                case TEN, JACK, QUEEN, KING -> totalValue += 10;
                case ACE -> totalValue += 11;
            }
        }

        return totalValue;
    }

    public ArrayList<Integer> numbers(){
        ArrayList<Integer> val = new ArrayList<>();

        for (Card aCard: this.cards){
            switch (aCard.getValue()){
                case TWO -> val.add(2);
                case THREE -> val.add(3);
                case FOUR -> val.add(4);
                case FIVE -> val.add(5);
                case SIX -> val.add(6);
                case SEVEN -> val.add(7);
                case EIGHT -> val.add(8);
                case NINE -> val.add(9);
                case TEN, JACK, QUEEN, KING -> val.add(10);
                case ACE -> val.add(11);
            }
        }

        return val;
    }

    public void moveAllToDeck(Deck moveTo){
        int thisDeckSize = this.cards.size();

        for (int i = 0; i < thisDeckSize; i++){
            moveTo.addCard(this.getCard(i));
        }
        for (int i = 0; i < thisDeckSize; i++){
            this.removeCard(0);
        }
    }
    // opponent - это количество карт игрока
    // deck - это количество карт, лежащих в колоде
    public boolean hit(int opponent, int deck){
        int c = this.cardsValue();
        int n = 21 - c;
        int t = 4;
        double per = 0.0;
        for (int i = 2; i <= n; i++){
            if (i > 10 && i != 11){
                break;
            } else if (i == 10) {
                t = t * 4;
            }
            for (int j: this.numbers()){
                if (i == j){
                    t--;
                    if (t == 0){
                        break;
                    }
                }
            }
            double a = (double) t / deck;
            double b = (t > opponent)? (double) (t - opponent) / deck : 0.0;
            per += (a + b) / 2;
        }
        return per > 0.055;
    }
}
