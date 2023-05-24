package object;

import Util.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static UI.Screen.GROUDNY;
import static UI.Screen.SCREEN_WIDTH;

public class Land {

    public Random random;
    public List<ImageLand> listImage;
    public BufferedImage imageLand1;
    public BufferedImage imageLand2;
    public BufferedImage imageLand3;
    public MainCharacter mainCharacter;

    public Land(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
        random = new Random();
        imageLand1 = Resource.getImage("files/land1.png");
        imageLand2 = Resource.getImage("files/land2.png");
        imageLand3 = Resource.getImage("files/land3.png");
        listImage = new ArrayList<>();
        int numberOfLandTitle = SCREEN_WIDTH / imageLand1.getWidth() + 2;
        for (int i = 0; i < numberOfLandTitle; i++) {
            ImageLand imageLand = new ImageLand();
            imageLand.posX = i * imageLand1.getWidth();
            imageLand.image = getImageLand();
            listImage.add(imageLand);
        }
    }

    public void update() {
        for (ImageLand imageLand : listImage) {
            imageLand.posX -= mainCharacter.getSpeedX();
        }
        ImageLand firstElement = listImage.get(0);
        if (firstElement.posX + imageLand1.getWidth() < 0) {
            firstElement.posX = listImage.get(listImage.size() - 1).posX + imageLand1.getWidth();
            listImage.add(firstElement);
            listImage.remove(0);
        }
    }

    public BufferedImage getImageLand(){
        return switch (random.nextInt(10)) {
            case 0 -> imageLand1;
            case 1 -> imageLand3;
            default -> imageLand2;
        };
    }

    public void draw(Graphics g) {
        for (ImageLand imageLand : listImage) {
            g.drawImage(imageLand.image, imageLand.posX, (int) GROUDNY - 15, null);
        }
    }

    public static class ImageLand {
        public int posX;
        BufferedImage image;
    }
}
