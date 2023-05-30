package com.github.kot512.surrounded_and_hunted.screens.image_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxScreen


open class BaseImageScreen(
    protected val backgroundTexture: Texture,
    protected val backgroundPosition: Point =
        Point(CONST_AND_VAR.SCREEN_WIDTH / 2, CONST_AND_VAR.SCREEN_HEIGHT / 2)
) : KtxScreen {
//    для экрана и графики
    protected val batch: SpriteBatch = SpriteBatch()
    private val camera: Camera = OrthographicCamera(
        CONST_AND_VAR.SCREEN_WIDTH,
        CONST_AND_VAR.SCREEN_HEIGHT
    )
    private val viewport: Viewport = FitViewport(
        CONST_AND_VAR.SCREEN_WIDTH,
        CONST_AND_VAR.SCREEN_HEIGHT,
        camera
    )
    var stage: Stage = Stage(viewport, batch)

//    масштабирование фона
    val scaleCoeff: Float
    var scaledWidth: Float = backgroundTexture.width.toFloat()
    var scaledHeight: Float = backgroundTexture.height.toFloat()

    init {
        Gdx.input.inputProcessor = stage

//        определение масштабирование фона
        val widthDiff = CONST_AND_VAR.SCREEN_WIDTH - scaledWidth
        val heightDiff = CONST_AND_VAR.SCREEN_HEIGHT - scaledHeight
        scaleCoeff =
            if (widthDiff > heightDiff) CONST_AND_VAR.SCREEN_WIDTH / scaledWidth
            else CONST_AND_VAR.SCREEN_HEIGHT / scaledHeight

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
