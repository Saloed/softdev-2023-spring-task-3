import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import game.gdx.Hero1;
import game.gdx.KeyboardAdapter;
import game.gdx.Start;

public class Test implements Testgdx {
    Start game;
    Texture background;
    Hero1 me;
    OrthographicCamera camera;
    KeyboardAdapter inputProcessor = new KeyboardAdapter();

    public Test(Start gam) {
        this.game = gam;
        Gdx.input.setInputProcessor(inputProcessor);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        me = new Hero1(45, 250);
        background = new Texture(Gdx.files.internal("map_rendered_size_7761_1 (2).png"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 1, 1);
        me.render(game.batch);
        game.batch.end();
        me.moveTo(inputProcessor.getDirection());
    }
    @Override
    public void dispose(){
        me.dispose();
        background.dispose();
    }
}