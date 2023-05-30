package com.github.kot512.surroundedAndHunted.screens.image_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.kot512.surroundedAndHunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surroundedAndHunted.screens.playable_screens.MainLocationScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen


class MainMenuImageScreen : BaseImageScreen(
    Texture("graphics/screen_backgrounds/main_menu.png")
) {
//    загрузка текстур для кнопок
    private val startButtonTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("menu_begin_button")
    )
    private val startButtonPressedTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("menu_begin_button_pressed")
    )
    private val upgradeButtonTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("menu_upgrades_button")
    )
    private val upgradeButtonPressedTexture = TextureRegion(
        CONST_AND_VAR.TEXTURE_ATLAS.findRegion("menu_upgrades_button_pressed")
    )


//    создание кнопок
//    кнопка старта игры
    private val startButtonStyle = TextButtonStyle().apply {
        font = BitmapFont()
        up = TextureRegionDrawable(startButtonTexture)
        down = TextureRegionDrawable(startButtonPressedTexture)
        checked = TextureRegionDrawable(startButtonPressedTexture)
    }
    private val startButton: TextButton = TextButton(null, startButtonStyle).apply {
        addListener(
            object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).apply {
                        addScreen(MainLocationScreen())
                        setScreen<MainLocationScreen>()
                        removeScreen<MainMenuImageScreen>()
                    }
                    this@MainMenuImageScreen.dispose()
                }
            }
        )
        setSize(this.width * 0.3f * scaleCoeff, this.height * 0.3f * scaleCoeff)
        setPosition(
            CONST_AND_VAR.SCREEN_WIDTH / 2 - width / 2,
            CONST_AND_VAR.SCREEN_HEIGHT / 2 - height / 2
        )
    }

//    кнопка для перехода на экран перманентной прокачки
    private val upgradesButtonStyle = TextButtonStyle().apply {
    font = BitmapFont()
    up = TextureRegionDrawable(upgradeButtonTexture)
    down = TextureRegionDrawable(upgradeButtonPressedTexture)
    checked = TextureRegionDrawable(upgradeButtonPressedTexture)
}
    private val upgradesButton: TextButton = TextButton(null, upgradesButtonStyle).apply {
        addListener(
            object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).apply {
                        addScreen(UpgradesMenuImageScreen())
                        setScreen<UpgradesMenuImageScreen>()
                        removeScreen<MainMenuImageScreen>()
                    }
                    this@MainMenuImageScreen.dispose()
                }
            }
        )
        setSize(this.width * 0.3f * scaleCoeff, this.height * 0.3f * scaleCoeff)
        setPosition(
            CONST_AND_VAR.SCREEN_WIDTH / 2 - width / 2,
            CONST_AND_VAR.SCREEN_HEIGHT / 2 - 1.5f * height
        )
    }

    override fun show() {
        stage.addActor(startButton)
        stage.addActor(upgradesButton)
    }
}
