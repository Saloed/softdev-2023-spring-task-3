package com.brickgame;

import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetsManager {
    public final AssetManager manager = new AssetManager();
    private final String skin = "skin/uiskin.json";

    //Sound
    private final String hitSound = "Sounds/hit.mp3";
    private final String brickBrokeSound = "Sounds/brickIsbroke.mp3";
    private final String achivesSonud = "Sounds/achives.mp3";

    // Music
    private final String music = "Sounds/backgroundMusic.mp3";

    public void queueAddMusic() {
        manager.load(music, Music.class);
    }


    // Sounds
    public void queueAddSounds() {
        manager.load(hitSound, Sound.class);
        manager.load(brickBrokeSound, Sound.class);
        manager.load(achivesSonud, Sound.class);
    }

    // Skin
    public void queueAddSkin() {
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("skin/uiskin.atlas");
        manager.load(skin, Skin.class, params);
    }
}
