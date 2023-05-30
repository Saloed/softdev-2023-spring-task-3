package game.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Speed {
    public Texture texture;
    public boolean p;
    public Speed() {
        this.texture = new Texture(Gdx.files.internal("free-icon-runer-silhouette-running-fast-55240.png"));
        p = true;
    }
    public void render(Batch batch) {
        if(p) batch.draw(texture, 540, 480);
        if(!p) batch.draw(texture, 4400, 200);
    }
    public void dispose() {
        texture.dispose();
    }
    public void swap() {
        p = false;
    }
}
