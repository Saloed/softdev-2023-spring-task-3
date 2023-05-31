package game.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Hero2 {
    private final Vector2 position = new Vector2();
    private final Texture texture;

    public Hero2(float x, float y) {
        texture = new Texture("pngwing.com.png");
        position.set(x, y);
    }
    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
        if(position.x < 104) position.x = 104;
        if(position.x > 100 && position.x < 243){
            if(position.y > 394) position.y = 394;
            if(position.y < 275) position.y = 275;
        }
        if(position.x > 240 &&(position.y > 395 || position.y < 274)) {
            if(position.x < 248) position.x = 248;
        }
        if(position.x > 242 && position.y > 563) position.y = 563;
        if(position.x > 242 && position.y < 59) position.y = 59;
        if(position.x > 943 &&(position.y > 351 || position.y < 315)) position.x = 943;
        if(position.y < 351 && position.y > 315 && position.x > 942) {
            if(position.y > 345) position.y = 345;
            if(position.y < 322) position.y = 322;
        }
        if(position.x > 1088) position.x = 1088;
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
