package com.github.kot512.surrounded_and_hunted.screen.image_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_HEIGHT
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_WIDTH
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxScreen
import java.lang.Float.max


open class BaseImageScreen(
    protected val backgroundTexture: Texture,
    protected val backgroundPosition: Point = Point(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)
) : KtxScreen {
//    для экрана и графики
    private val batch: SpriteBatch = SpriteBatch()
    private val camera: Camera = OrthographicCamera(
        SurroundedAndHunted.SCREEN_WIDTH,
        SurroundedAndHunted.SCREEN_HEIGHT
    )
    private val viewport: Viewport = FitViewport(
        SurroundedAndHunted.SCREEN_WIDTH,
        SurroundedAndHunted.SCREEN_HEIGHT,
        camera
    )
    var stage: Stage = Stage(viewport, batch)

//    масштабирование фона
    val scaleCoeff: Float
    var scaledWidth: Float = backgroundTexture.width.toFloat()
    var scaledHeight: Float = backgroundTexture.height.toFloat()

    init {
        Gdx.input.inputProcessor = stage

//        определяем масштабирование фона
        val widthDiff = SCREEN_WIDTH - scaledWidth
        val heightDiff = SCREEN_HEIGHT - scaledHeight
        scaleCoeff =
            if (widthDiff > heightDiff) SCREEN_WIDTH / scaledWidth
            else SCREEN_HEIGHT / scaledHeight

        scaledWidth *= scaleCoeff
        scaledHeight *= scaleCoeff
    }

//    в метод show необходимо добавить кнопки и их слушатели
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.82353f, 0.27843f, 0.14902f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(
            backgroundTexture,
            backgroundPosition.x - scaledWidth / 2,
            backgroundPosition.y - scaledHeight / 2,
            scaledWidth, scaledHeight
        )
        batch.end()

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        backgroundTexture.dispose()
        stage.dispose()
    }
}
