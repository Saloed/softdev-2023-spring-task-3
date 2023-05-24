import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics2D;

public class Card {
    private final int suit; //Clubs 0, Diamonds 1, Hearts 3, Spades 4
    private final int rank; //Ace 0, 2 (1) - 10 (9), Jack 10, Queen 11, King 12
    private final int value;

    public Card() {
        suit = 0;
        rank = 0;
        value = 0;
    }

    public Card(int s, int r, int v) {
        suit = s;
        rank = r;
        value = v;
    }

    public int getSuit() { //this method returns you the suit of the card.
        return suit;
    }
    public int getRank() { //this method returns you the rank of the card.
        return rank;
    }
    public int getValue() { //this method returns you the value of the card.
        return value;
    }

    public void printCard(Graphics2D g2, boolean dealerTurn, boolean faceDown, int cardNumber) throws IOException {
        BufferedImage deckImg = ImageIO.read(new File("images/cardSpriteSheet.png"));
        int imgWidth = 950;
        int imgHeight = 392;

        int suits = 4;
        int ranks = 13;

        BufferedImage[][] cardPictures = new BufferedImage[suits][ranks];
        BufferedImage backOfACard = ImageIO.read(new File("images/backsideOfACard.jpg"));

        for (int s = 0; s < 4; s++) {
            for (int r = 0; r < 13; r++) {
                cardPictures[s][r] = deckImg.getSubimage(r*imgWidth/13, s *imgHeight/4, imgWidth/13, imgHeight/4);
            }
        }

        int yPosition;
        if (dealerTurn) {
            yPosition = 75;
        }
        else {
            yPosition = 400;
        }

        int xPosition = 500 + 75 * cardNumber;

        if (faceDown) {
            g2.drawImage(backOfACard, xPosition, yPosition, null );
        }
        else {
            g2.drawImage(cardPictures[suit][rank], xPosition, yPosition, null);
        }
    }
}
