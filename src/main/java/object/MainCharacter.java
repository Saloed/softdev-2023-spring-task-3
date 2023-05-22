package object;

import Util.Animation;
import Util.Resource;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

import static UI.Screen.GRAVITY;
import static UI.Screen.GROUDNY;

public class MainCharacter {
    private float x;
    private float y = 0;
    private float speedY = 0;
    private float speedX;
    public static final int RUNNING = 0;
    public static final int JUMPING = 1;
    public static final int DOWN = 2;
    public static final int DEATH = 3;

    private final Animation characterRun;
    private final Animation characterDown;
    private final BufferedImage characterDead;
    private final BufferedImage jumping;
    private final Rectangle rectangle, rectangleBody;
    private boolean isAlive = true;
    private int state = RUNNING;


    public MainCharacter() {
        x = 50;
        characterRun = new Animation(90);
        characterRun.addFrame(Resource.getImage("files/main-character1.png"));
        characterRun.addFrame(Resource.getImage("files/main-character2.png"));
        rectangle = new Rectangle();
        rectangleBody = new Rectangle();
        characterDown = new Animation(90);
        characterDown.addFrame(Resource.getImage("files/main-character5.png"));
        characterDown.addFrame(Resource.getImage("files/main-character6.png"));
        characterDead = Resource.getImage("files/main-character4.png");
        jumping = Resource.getImage("files/main-character3.png");
    }


    public void update () {
        characterRun.update();
        characterDown.update();
        if (y >= GROUDNY - characterRun.getFrame().getHeight()) {
            speedY = 0;
            y = GROUDNY - characterRun.getFrame().getHeight();
            if (state != DOWN) {
                state = RUNNING;
            }
        } else {
            speedY += GRAVITY;
            y += speedY;
        }
        if (speedX < 10) {
            speedX += 0.001;
        }
    }

    public Rectangle getBoundBody() {
        if (state == DOWN) {
            rectangle.x = (int) x;
            rectangle.y = (int) y + 20;
            rectangle.width = characterRun.getFrame().getWidth();
        } else {
            rectangle.x = (int) x + 10;
            rectangle.y = (int) y;
            rectangle.width = characterRun.getFrame().getWidth() - 25;
        }
        rectangle.height = characterRun.getFrame().getHeight();
        return rectangle;
    }

    public Rectangle getBoundHead() {
        if (state == RUNNING) {
            rectangleBody.x = (int) x + 20;
            rectangleBody.y = (int) y;
            rectangleBody.width = characterRun.getFrame().getWidth() - 20;
            rectangleBody.height = characterRun.getFrame().getHeight() - 30;
            return rectangleBody;
        } else return rectangle;
    }

    public void draw(@NotNull Graphics g) {
        g.setColor(Color.black);
        switch (state) {
            case RUNNING -> g.drawImage(characterRun.getFrame(), (int) x, (int) y, null);
            case JUMPING -> g.drawImage(jumping, (int) x, (int) y, null);
            case DOWN -> g.drawImage(characterDown.getFrame(), (int) x, (int) (y + 20), null);
            case DEATH -> g.drawImage(characterDead, (int) x, (int) y, null);
        }
    }

    public void jump() {
        if (y >= GROUDNY - characterRun.getFrame().getHeight()) {
            speedY = -7.5f;
            y += speedY;
            state = JUMPING;
            Clip jumpSound = Resource.getAudio("files/jump.wav");
            jumpSound.start();
        }
    }

    public void down(boolean isDown) {
        if (state == JUMPING) {
            return;
        }
        if (isDown) {
            state = DOWN;
        } else {
            state = RUNNING;
        }
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        if (isAlive) {
            state = RUNNING;
        } else {
            state = DEATH;
        }
    }

    public boolean getAlive() {
        return isAlive;
    }

    public int getState() {
        return state;
    }

    public float getSpeedY() {
        return speedY;
    }

    public float getY() {
        return y;
    }
}
