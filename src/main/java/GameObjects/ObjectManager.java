package GameObjects;

import GameStates.Playing;
import HelperClasses.DataProcessing;
import Levels.Level;
import Sprites.MainCharacter;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static HelperClasses.Constants.Objects.*;

public class ObjectManager {
    private final Playing playing;
    private BufferedImage[] moneyImages;
    private BufferedImage spikeImage;
    private ArrayList<Money> moneys;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImage();
    }

    private void loadImage() {
        moneyImages = new BufferedImage[6];
        BufferedImage moneyImg = DataProcessing.GetSprite(DataProcessing.Money);
        for (int i = 0; i < moneyImages.length; i++)
            moneyImages[i] = moneyImg.getSubimage(i * MoneySize, 0, MoneySize, MoneySize);

        spikeImage = DataProcessing.GetSprite(DataProcessing.Spike);
        for (int i = 0; i < moneyImages.length; i++)
            moneyImages[i] = moneyImg.getSubimage(i * MoneySize, 0, MoneySize, MoneySize);
    }

    public void loadObjects(Level nextLevel) {
        moneys = new ArrayList<>(nextLevel.getMoneys());
        spikes = nextLevel.getSpikes();
    }

    public void checkSpikesContact(MainCharacter mainCharacter) {
        for (Spike spike: spikes) {
            if (spike.getHitbox().intersects(mainCharacter.getHitbox())) {
                mainCharacter.killMainCharacter();
            }
        }
    }

    public void checkObjectContact(Rectangle2D.Float hitbox) {
        for (Money money : moneys)
            if (money.isActive())
                if (hitbox.intersects(money.getHitbox())) {
                    money.setActive(false);
                    collectMoney();
                }
    }

    public void collectMoney() {
        playing.getMainCharacter().changeMoney(Value);
    }

    public void update() {
        for (Money money : moneys) {
            if (money.isActive()) money.update();
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawMoneys(g, xLvlOffset);
        drawSpikes(g, xLvlOffset);
    }

    private void drawSpikes(Graphics g, int xLvlOffset) {
        for (Spike spike : spikes) {
            g.drawImage(spikeImage, (int) (spike.getHitbox().x - xLvlOffset),
                    (int) (spike.getHitbox().y - spike.getOffsetY()),
                    SpikeWidthScaled, SpikeHeightScaled, null);
        }
    }

    private void drawMoneys(Graphics g, int xLvlOffset) {
        for (Money money : moneys) {
            if (money.isActive()) g.drawImage(moneyImages[money.getAnimationIndex()],
                    (int) (money.getHitbox().x - xLvlOffset - money.getOffsetX()),
                    (int) (money.getHitbox().y - money.getOffsetY()), MoneySizeScaled, MoneySizeScaled, null);
            //money.drawHitbox(g, xLvlOffset);
        }
    }

    public void resetAll() {
        for (Money money : moneys)
            money.resetAll();
    }
}
