package com.brickgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetsManager {
    public final AssetManager manager = new AssetManager();
    public final String skin = "skin/uiskin.json";

    //Sound
    public final String hitSound = "Sounds/hit.mp3";
    public final String brickBrokeSound = "Sounds/brickIsbroke.mp3";
    public final String achivesSonud = "Sounds/achives.mp3";

    // Music
    public final String music = "Sounds/backgroundMusic.mp3";

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
