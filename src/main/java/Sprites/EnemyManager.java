package Sprites;

import Levels.Level;
import HelperClasses.DataProcessing;

import static HelperClasses.Constants.Enemy.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class EnemyManager {
    private BufferedImage[][] crabImg;
    private BufferedImage[][] octopusImg;
    private Level level;

    public EnemyManager() {
        loadImage();
    }

    public void loadEnemies(Level level) {
        this.level = level;
    }

    public void update(int[][] lvlData, MainCharacter mainCharacter) {
        for (Crab crab : level.getCrabs())
            if (crab.isActive()) {
                crab.update(lvlData, mainCharacter);
            }
        for (Octopus octopus : level.getOctopuses())
            if (octopus.isActive()) {
                octopus.update(lvlData, mainCharacter);
            }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
        drawOctopuses(g, xLvlOffset);
    }

    private void drawOctopuses(Graphics g, int xLvlOffset) {
        for (Octopus octopus : level.getOctopuses()) {
            if (octopus.isActive())
                g.drawImage(octopusImg[octopus.getAction()][octopus.getAnimationIndex()],
                        (int) octopus.getHitbox().x - OctopusOffsetX - xLvlOffset + octopus.flipX(),
                        (int) octopus.getHitbox().y - OctopusOffsetY,
                        OctopusWidthScaled * octopus.flipW(),
                        OctopusHeightScaled, null);
        }
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crab crab : level.getCrabs()) {
            if (crab.isActive())
                g.drawImage(crabImg[crab.getAction()][crab.getAnimationIndex()],
                        (int) crab.getHitbox().x - CrabOffsetX - xLvlOffset + crab.flipX(),
                        (int) crab.getHitbox().y - CrabOffsetY,
                        CrabWidthScaled * crab.flipW(),
                        CrabHeightScaled, null);
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackHitbox) {
        for (Crab crab : level.getCrabs())
            if (crab.isActive())
                if (crab.getCurrentHealth() > 0) {
                    if (attackHitbox.intersects(crab.getHitbox())) {
                        crab.hurt(10);
                        return;
                    }
                }
        for (Octopus octopus : level.getOctopuses())
            if (octopus.isActive())
                if (octopus.getCurrentHealth() > 0) {
                    if (attackHitbox.intersects(octopus.getHitbox())) {
                        octopus.hurt(10);
                        return;
                    }
                }
    }

    private void loadImage() {
        crabImg = GetImages(DataProcessing.GetSprite(DataProcessing.CrabImage), 9, 5, CrabWidth, CrabHeight);
        octopusImg = GetImages(DataProcessing.GetSprite(DataProcessing.OctopusImages), 8, 5, OctopusWidth, OctopusHeight);
    }

    private BufferedImage[][] GetImages(BufferedImage img, int x, int y, int w, int h) {
        BufferedImage[][] temp = new BufferedImage[y][x];
        for (int j = 0; j < temp.length; j++)
            for (int i = 0; i < temp[j].length; i++)
                temp[j][i] = img.getSubimage(i * w, j * h, w, h);
        return temp;
    }

    public void resetAll() {
        for (Crab crab : level.getCrabs())
            crab.resetEnemy();
        for (Octopus octopus : level.getOctopuses())
            octopus.resetEnemy();
    }
}
