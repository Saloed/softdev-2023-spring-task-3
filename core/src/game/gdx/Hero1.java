package game.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Hero1 {
    private final Vector2 position = new Vector2();
    public final Texture texture;

    public Hero1(float x, float y) {
        texture = new Texture("pngwing.com.png");
        position.set(x, y);
    }
    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
        if(position.x < 38) position.x = 33;
        if(position.x > 50 && position.x < 90 && position.y > 88) position.x = 55;
        if(0 < position.x && position.x < 60) {
            if (position.y > 322) position.y = 322;
        }
        if(position.x > 180 && position.x < 888) {
            if (position.y > 516) position.y = 516;
        }
        if (position.x > 100 && position.x < 199 && position.y > 90) position.x = 199;
        if(60 < position.x && position.x < 190) {
            if (position.y > 82) position.y = 82;
        }
        if(position.y < 198 && position.x > 220) position.x = 225;
        if(position.x > 225 && position.x < 1000 && position.y < 203) position.y = 203;
        if(position.y < 58) position.y = 58;
        if(position.x > 915 && position.x < 928 && position.y < 557) {
            if(position.x > 920) position.x = 920;
        }
        if (position.x > 924 && position.x < 1083 && position.y < 563) position.y = 563;
        if(position.x > 1085 && position.y < 562){
            if(position.x < 1091) position.x = 1091;
        }
        if(position.x > 890 && position.x < 896 && position.y > 510) position.x = 896;
        if(position.x > 890 && position.y > 588) position.y = 588;
        if(position.x > 1111) position.x = 1111;
        if(position.x > 1085 && position.y < 370) position.y = 370;
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
