package object;

import Util.Resource;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class EnemiesManager {
    public double distanceBetweenEnemies = 600;
    private static final double DISTANCE_DEC = -0.1;
    private static final int MINIMUM_DISTANCE = 250;
    private static final int ORIGINAL_DISTANCE = 600;
    public final List<Enemy> enemyList;
    private final Random randomCactus;
    private final Random randomEnemy;
    private final Random randomBird;
    private final BufferedImage imageCactus1;
    private final BufferedImage imageCactus2;
    private final MainCharacter mainCharacter;

    public EnemiesManager(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
        enemyList = new ArrayList<>();
        imageCactus1 =  Resource.getImage("files/cactus1.png");
        imageCactus2 =  Resource.getImage("files/cactus2.png");
        randomCactus = new Random();
        randomEnemy = new Random();
        randomBird = new Random();
    }

    public void update() {
        if (mainCharacter.getSpeedX() < 9) {
            if(distanceBetweenEnemies > MINIMUM_DISTANCE) {
                distanceBetweenEnemies += DISTANCE_DEC;
            }
        } else distanceBetweenEnemies = 400;
        for (Enemy e : enemyList) {
            e.update();
            if (e.getBound().intersects(mainCharacter.getBoundBody()) || e.getBound().intersects(mainCharacter.getBoundHead())) {
                mainCharacter.setAlive(false);
                Clip deadSound = Resource.getAudio("files/1death.wav");
                deadSound.start();
            }
        }
        if (isSpaceAvailable()) {
            addRandomEnemy();
        }
        if (enemyList.get(0).getPosX() + enemyList.get(0).getWidth() < 0){
            enemyList.remove(0);
        }
    }

    public  void draw(Graphics g) {
        for (Enemy e : enemyList) {
            e.draw(g);
        }
    }

    public void reset() {
        distanceBetweenEnemies = ORIGINAL_DISTANCE;
        enemyList.clear();
    }

    private void addRandomCactus() {
        Cactus cactus;
        cactus = new Cactus(mainCharacter);
        cactus.setPosX(600);
        if (randomCactus.nextBoolean()) {
            cactus.setImage(imageCactus1);
        } else {
            cactus.setImage(imageCactus2);
            cactus.setPosY(80);
        }
        enemyList.add(cactus);
    }

    private void addRandomEnemy() {
        if (randomEnemy.nextInt(3) == 0) {
            addRandomBird();
        } else {
            addRandomCactus();
        }
    }

    private void addRandomBird() {
        Bird bird = new Bird(mainCharacter);
        bird.setPosX(600);
        bird.setPosY(randomBird.nextInt(0,  110 - bird.getFlyingBird().getFrame().getHeight() + 1));
        enemyList.add(bird);
    }

    public boolean isSpaceAvailable() {
        for (Enemy enemy : enemyList) {
            if (ORIGINAL_DISTANCE - (enemy.getPosX() + enemy.getWidth()) < distanceBetweenEnemies){
                return false;
            }
        }
        return true;
    }
}
