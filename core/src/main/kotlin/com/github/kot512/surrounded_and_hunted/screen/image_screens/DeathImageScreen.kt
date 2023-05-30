package com.github.kot512.surrounded_and_hunted.screen.image_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CURRENT_SCORE
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.RECORD_SCORE
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_HEIGHT
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_WIDTH
import ktx.app.KtxGame
import ktx.app.KtxScreen

class DeathImageScreen : BaseImageScreen(
    Texture("graphics/screen_backgrounds/dead_menu.png")
) {
//    кнопка возвращения к главному меню
    private val returnButtonTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_dead_return")
    )
    private val returnButtonPressedTexture = TextureRegion(
        SurroundedAndHunted.TEXTURE_ATLAS.findRegion("menu_dead_return_pressed")
    )

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
                    CURRENT_SCORE = 0

                    (Gdx.app.applicationListener as KtxGame<KtxScreen>).apply {
                        addScreen(MainMenuImageScreen())
                        setScreen<MainMenuImageScreen>()
                        removeScreen<DeathImageScreen>()
                    }
                    this@DeathImageScreen.dispose()
                }
            }
        )
        setSize(this.width * 0.3f * scaleCoeff, this.height * 0.3f * scaleCoeff)
        setPosition(SurroundedAndHunted.SCREEN_WIDTH / 2 - width / 2, height / 2 * scaleCoeff)
    }

//    вывод счета и рекорда на экран
    private val score = object : Actor() {
        private val font = BitmapFont().apply {
            data.setScale(4f)
            color = Color.WHITE
        }

        init {
            setPosition(
                SurroundedAndHunted.SCREEN_WIDTH / 2 + 120f,
                SurroundedAndHunted.SCREEN_HEIGHT - 600f
            )
            width = 200f
            height = 200f
        }

        override fun draw(batch: Batch, parentAlpha: Float) {
            val score = """YOUR SCORE: $CURRENT_SCORE
                |YOUR RECORD: $RECORD_SCORE
            """.trimMargin()
            font.draw(
                batch, score,
                SCREEN_WIDTH / 2 + width / 2 - 15f * score.length,
                SCREEN_HEIGHT / 2 + 125f,
            )
        }
    }


    override fun show() {
        stage.addActor(returnButton)
        stage.addActor(score)
    }
}
