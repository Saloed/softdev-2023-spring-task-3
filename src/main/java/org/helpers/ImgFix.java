package org.helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImgFix {

    public static BufferedImage getRotImg(BufferedImage img, int rotAngle) {
        //Поворачивает картинку по центральной оси (Нужен для поворота тайлов только дорог)
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage newImg = new BufferedImage(w, h, img.getType());
        Graphics2D g2d = newImg.createGraphics();

        g2d.rotate(Math.toRadians(rotAngle), w / 2, h / 2);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return newImg;
    }

    public static BufferedImage[] getBuildRotImg(BufferedImage[] imgs, BufferedImage secondImage, int rotAngle) {
        //Отвечает за поворот + анимацию, только для воды
        int w = imgs[0].getWidth();
        int h = imgs[0].getHeight();

        BufferedImage[] arr = new BufferedImage[imgs.length];

        for (int i = 0; i < imgs.length; i++) {
            BufferedImage newImg = new BufferedImage(w, h, imgs[0].getType());
            Graphics2D g2d = newImg.createGraphics();

            g2d.drawImage(imgs[i], 0, 0, null);
            g2d.rotate(Math.toRadians(rotAngle), w / 2, h / 2);
            g2d.drawImage(secondImage, 0, 0, null);
            g2d.dispose();

            arr[i] = newImg;
        }
        return arr;
    }
}
