package com.github.kot512.surrounded_and_hunted.screen.image_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_HEIGHT
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_WIDTH
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.TestLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxGame
import ktx.app.KtxScreen


class MainMenuImageScreen : BaseImageScreen(
    Texture("graphics/screen_backgrounds/main_menu.png")
) {
//    загрузка текстур для кнопок
    private val startButtonTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_begin_button")
    )
    private val startButtonPressedTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_begin_button_pressed")
    )
    private val slidesButtonTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_slide_button")
    )
    private val slidesButtonPressedTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_slide_button_pressed")
    )
    private val upgradeButtonTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_upgrades_button")
    )
    private val upgradeButtonPressedTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_upgrades_button_pressed")
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
                        addScreen(TestLocationScreen())
                        setScreen<TestLocationScreen>()
                        removeScreen<MainMenuImageScreen>()
                    }
                    this@MainMenuImageScreen.dispose()
                }
            }
        )
        setSize(this.width * 0.3f * scaleCoeff, this.height * 0.3f * scaleCoeff)
        setPosition(SCREEN_WIDTH / 2 - width / 2, SCREEN_HEIGHT / 2 - height / 2)
    }

//    кнопка для перехода на экран смены уровня/слайда
    private val slidesButtonStyle = TextButtonStyle().apply {
        font = BitmapFont()
        up = TextureRegionDrawable(slidesButtonTexture)
        down = TextureRegionDrawable(slidesButtonPressedTexture)
        checked = TextureRegionDrawable(slidesButtonPressedTexture)
    }
    private val slidesButton: TextButton = TextButton(null, slidesButtonStyle).apply {
        addListener(
            object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).apply {
                        addScreen(SlidesMenuImageScreen())
                        setScreen<SlidesMenuImageScreen>()
                        removeScreen<MainMenuImageScreen>()
                    }
                        this@MainMenuImageScreen.dispose()
                }
            }
        )
            setSize(this.width * 0.3f * scaleCoeff, this.height * 0.3f * scaleCoeff)
            setPosition(SCREEN_WIDTH / 2 - width / 2, SCREEN_HEIGHT / 2 - 1.5f * height)
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
                        addScreen(SlidesMenuImageScreen())
                        setScreen<SlidesMenuImageScreen>()
                        removeScreen<MainMenuImageScreen>()
                    }
                    this@MainMenuImageScreen.dispose()
                }
            }
        )
        setSize(this.width * 0.3f * scaleCoeff, this.height * 0.3f * scaleCoeff)
        setPosition(SCREEN_WIDTH / 2 - width / 2, SCREEN_HEIGHT / 2 - 2.5f * height)
    }
    override fun show() {
        stage.addActor(startButton)
        stage.addActor(slidesButton)
        stage.addActor(upgradesButton)
    }
}
