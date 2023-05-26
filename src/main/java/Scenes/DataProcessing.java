package Scenes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class DataProcessing {
    public static final String MainCharacter = "images/mainCharacter.png";
    public static final String ForLevels = "images/forLevels.png";
    public static final String ButtonsForMenu = "images/ButtonsForMenu.png";
    public static final String MenuBackground = "images/MenuBackground.png";
    public static final String PlayingBackgroundDay = "images/backgroundImageDay.png";
    public static final String PlayingBackgroundNight = "images/backgroundImageNight.png";
    public static final String BigClouds = "images/bigCloud.png";
    public static final String SmallClouds = "images/smallCloud.png";
    //public static final String OctopusImages = "images/OctopusSprite.png";
    public static final String OctopusImages = "images/crabby_sprite.png";
    public static final String HealthBar = "images/Health.png";
    public static final String ButtonsForPause = "images/ButtonsForPause.png";
    public static final String PauseBackground = "images/PauseBackground.png";
    public static final String LevelCompletedBackground = "images/LevelCompletedBackground.png";

    public static BufferedImage GetSprite(String fileName) {
        BufferedImage image = null;

        InputStream inputStream = DataProcessing.class.getResourceAsStream("/" + fileName);
        try {
            assert inputStream != null;
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = DataProcessing.class.getResource("/images/lvlsData");
        File file = null;

        try {
            if (url != null) file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        Arrays.sort(files);

        BufferedImage[] images = new BufferedImage[files.length];
        for (int i = 0; i < images.length; i++)
            try {
                images[i] = ImageIO.read(files[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return images;
    }
}
