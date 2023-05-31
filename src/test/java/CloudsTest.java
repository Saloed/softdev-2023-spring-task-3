import object.Clouds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloudsTest {

    @Test
    public void testUpdate() {
        Clouds clouds = new Clouds();

        assertEquals(100, clouds.cloudList.get(0).posX);

        clouds.update();

        assertEquals(99, clouds.cloudList.get(0).posX);
    }
}