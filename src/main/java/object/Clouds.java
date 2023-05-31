package object;

import Util.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static UI.Screen.SCREEN_WIDTH;

public class Clouds {

    private final BufferedImage cloudImage;
    public List<Cloud> cloudList;

    public Clouds() {
        cloudImage = Resource.getImage("files/cloud.PNG");
        cloudList = new ArrayList<>();

        Cloud cloud1 = new Cloud();
        cloud1.posX = 100;
        cloud1.posY = 50;
        cloudList.add(cloud1);

        Cloud cloud2 = new Cloud();
        cloud2.posX = 200;
        cloud2.posY = 30;
        cloudList.add(cloud2);

        Cloud cloud3 = new Cloud();
        cloud3.posX = 300;
        cloud3.posY = 60;
        cloudList.add(cloud3);

        Cloud cloud4 = new Cloud();
        cloud4.posX = 450;
        cloud4.posY = 50;
        cloudList.add(cloud4);

        Cloud cloud5 = new Cloud();
        cloud5.posX = SCREEN_WIDTH;
        cloud5.posY = 60;
        cloudList.add(cloud5);
    }

    public void update() {
        for (Cloud cloud : cloudList) {
            cloud.posX--;
        }
        Cloud firstCloud = cloudList.get(0);
        if (firstCloud.posX + cloudImage.getWidth() < 0) {
            firstCloud.posX = SCREEN_WIDTH;
            cloudList.remove(firstCloud);
            cloudList.add(firstCloud);
        }
    }

    public void draw(Graphics g) {
        for (Cloud cloud : cloudList) {
            g.drawImage(cloudImage, cloud.posX, cloud.posY, null);
        }
    }

    public static class Cloud {
        public int posX;
        int posY;
    }
}
