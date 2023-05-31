import Util.Animation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class AnimationTest {

    private Animation animation;
    private BufferedImage frame1;
    private BufferedImage frame2;

    @BeforeEach
    public void setUp() {
        animation = new Animation(100);
        frame1 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        frame2 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        animation.addFrame(frame1);
        animation.addFrame(frame2);
    }

    @Test
    public void testUpdate_FrameIndexIncremented() {
        animation.setPreviousTime(System.currentTimeMillis() - 200);

        animation.update();

        assertEquals(1, animation.getFrameIndex());
    }

    @Test
    public void testUpdate_FrameIndexLooped() {
        animation.setPreviousTime(System.currentTimeMillis() - 200);
        animation.setFrameIndex(1);
        animation.update();

        assertEquals(0, animation.getFrameIndex());
    }

    @Test
    public void testAddFrame() {
        BufferedImage frame3 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        animation.addFrame(frame3);

        assertEquals(3, animation.getFrames().size());
        assertEquals(frame3, animation.getFrames().get(2));
    }

    @Test
    public void testGetFrame() {
        BufferedImage frame = animation.getFrame();

        assertEquals(frame1, frame);
    }

    @Test
    public void testGetFrame_EmptyFramesList() {
        Animation emptyAnimation = new Animation(100);

        BufferedImage frame = emptyAnimation.getFrame();

        assertNull(frame);
    }

}
