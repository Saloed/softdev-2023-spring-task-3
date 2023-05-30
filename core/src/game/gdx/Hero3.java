package game.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Hero3 {
    private final Vector2 position = new Vector2();
    private final Texture texture;

    public Hero3(float x, float y) {
        texture = new Texture("pngwing.com.png");
        position.set(x, y);
    }
    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
        if(position.y > 586) position.y = 586;
        if(position.y > 550 && position.y < 562 && position.x < 1109) position.y = 562;
        if(position.y < 561 && position.y > 66 && position.x > 1090 && position.x < 1109) position.x = 1109;
        if(position.x > 1135) position.x = 1135;
        if(position.x < 56) position.x = 56;
        if(position.y < 34) position.y = 34;
        if(position.x < 1108 && position.y < 75 && position.y > 58 && position.x > 80) position.y = 58;
        if(position.x < 90 && position.x > 80 && position.y > 59 && position.y < 392) position.x = 80;
        if(position.y < 548 && position.y > 418 && position.x < 1050) position.y = 418;
        if(position.x > 81 && position.x < 942 && position.y < 393 && position.y > 378) position.y = 393;
        if(position.x < 1090 && position.x > 968 && position.y < 440 && position.y > 80) position.x = 968;
        if(position.x > 930 && position.x < 942 && position.y < 393 && position.y > 227) position.x = 942;
        if(position.x > 100 && position.x < 970 && position.y > 90 && position.y < 202) position.y = 202;
        if(position.x > 100 && position.x < 941 && position.y < 240  && position.y > 227) position.y = 227;
        if(position.y < 240 && position.y > 190 && position.x > 100 && position.x < 224) position.x = 224;
    }
    public void dispose() {
        texture.dispose();
    }
    public float getPositionX(){
        return position.x;
    }
    public float getPositionY(){
        return position.y;
    }
    public void setPositionX(float pos){
        position.x = pos;
    }public void setPositionY(float pos){
        position.y = pos;
    }

    public void moveTo(Vector2 direction) {
        position.add(direction);
    }
}
