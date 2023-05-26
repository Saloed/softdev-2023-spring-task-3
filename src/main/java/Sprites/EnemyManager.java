package Sprites;

import GameStates.Playing;
import Levels.Level;
import Scenes.DataProcessing;

import static Sprites.Constants.Enemy.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private final Playing playing;
    private BufferedImage[][] octopusImg;
    private ArrayList<Octopus> octopuses = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadImage();
    }

    public void loadEnemies(Level level) {
        octopuses = level.getOctopuses();
    }

    public void update(int[][] lvlData, MainCharacter mainCharacter) {
        boolean isActive = false;
        for (Octopus octopus : octopuses)
            if (octopus.isActive()) {
                octopus.update(lvlData, mainCharacter);
                isActive = true;
            }
        if (!isActive) playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawOctopuses(g, xLvlOffset);
    }

    private void drawOctopuses(Graphics g, int xLvlOffset) {
        for (Octopus octopus : octopuses) {
            if (octopus.isActive())
                g.drawImage(octopusImg[octopus.getState()][octopus.getAnimationIndex()],
                        (int) octopus.getHitbox().x - OctopusOffsetX - xLvlOffset + octopus.flipX(),
                        (int) octopus.getHitbox().y - OctopusOffsetY,
                        OctopusWidthScaled * octopus.flipW(),
                        OctopusHeightScaled, null);
            //octopus.drawHitbox(g, xLvlOffset);
            //octopus.drawAttackHitbox(g, xLvlOffset);
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackHitbox) {
        for (Octopus octopus : octopuses)
            if (octopus.isActive())
                if (attackHitbox.intersects(octopus.getHitbox())) octopus.hurt(10);
    }

    private void loadImage() {
        octopusImg = new BufferedImage[5][9];
        BufferedImage temp = DataProcessing.GetSprite(DataProcessing.OctopusImages);
        for (int j = 0; j < octopusImg.length; j++)
            for (int i = 0; i < octopusImg[j].length; i++)
                octopusImg[j][i] = temp.getSubimage(i * OctopusWidth, j * OctopusHeight, OctopusWidth, OctopusHeight);
    }

    public void resetAll() {
        for (Octopus octopus : octopuses)
            octopus.resetEnemy();
    }
}
