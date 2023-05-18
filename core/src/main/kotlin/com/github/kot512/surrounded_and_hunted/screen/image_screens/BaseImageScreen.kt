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
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxScreen


open class BaseImageScreen(
    private val backgroundTexture: Texture,
    private val width: Float,
    private val height: Float,
    protected val backgroundPosition: Point = Point(0f, 0f),
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

    init {
        Gdx.input.inputProcessor = stage
    }

//    в метод show необходимо добавить кнопки и их слушатели
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin()
        batch.draw(
            backgroundTexture, backgroundPosition.x, backgroundPosition.y, width, height
        )
        batch.end()

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        backgroundTexture.dispose()
    }
}
