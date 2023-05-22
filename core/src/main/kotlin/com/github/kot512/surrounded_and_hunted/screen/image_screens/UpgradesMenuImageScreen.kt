package com.github.kot512.surrounded_and_hunted.screen.image_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_WIDTH
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.MainLocationScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class UpgradesMenuImageScreen : BaseImageScreen(
    Texture("graphics/screen_backgrounds/empty_menu.png")
) {
    //    загрузка текстур для кнопок
    private val returnButtonTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_dead_return")
    )
    private val returnButtonPressedTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_dead_return_pressed")
    )

    //    создание кнопок
    //    кнопка старта игры
    private val returnButtonStyle = TextButton.TextButtonStyle().apply {
        font = BitmapFont()
        up = TextureRegionDrawable(returnButtonPressedTexture)
        down = TextureRegionDrawable(returnButtonTexture)
        checked = TextureRegionDrawable(returnButtonTexture)
    }
    private val returnButton: TextButton = TextButton(null, returnButtonStyle).apply {
        addListener(
            object : ClickListener() {
                override fun clicked(event: InputEvent, x: Float, y: Float) {
                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).apply {
                        addScreen(MainMenuImageScreen())
                        setScreen<MainMenuImageScreen>()
                        removeScreen<UpgradesMenuImageScreen>()
                    }
                    this@UpgradesMenuImageScreen.dispose()
                }
            }
        )
        setSize(this.width * 0.3f * scaleCoeff, this.height * 0.3f * scaleCoeff)
        setPosition(SCREEN_WIDTH / 2 - width / 2, height / 2 * scaleCoeff)
    }

    override fun show() {
        stage.addActor(returnButton)
    }
}
