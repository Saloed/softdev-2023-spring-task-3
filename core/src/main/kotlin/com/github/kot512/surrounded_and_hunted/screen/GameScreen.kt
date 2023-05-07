package com.github.kot512.surrounded_and_hunted.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxScreen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class GameScreen: KtxScreen {
//    параметры мира
    private val LEVEL_WIDTH = Gdx.graphics.width.toFloat()
    private val LEVEL_HEIGHT = Gdx.graphics.height.toFloat()

//    для экрана
    private val camera: Camera = OrthographicCamera() // камера
    private val viewport: Viewport = FitViewport(LEVEL_WIDTH, LEVEL_HEIGHT, camera)


//    графика
    private val batch: SpriteBatch = SpriteBatch()
    private val locationTexture: Texture = Texture("graphics/test_image/dark_grass_background.jpg") // TODO()

//    игровые объекты
    private val stage: Stage = Stage(viewport) // сцена, ответственная за рендер UI



    override fun dispose() {
        super.dispose()
    }

    override fun hide() {
        super.hide()
    }

    override fun pause() {
        super.pause()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_CLEAR_VALUE)
        batch.projectionMatrix = camera.combined //TODO(Чета не работает)
        Gdx.input.inputProcessor = stage

//        super.render(delta)
        batch.begin() // начало рендера

        batch.draw(locationTexture, 0f, 0f, LEVEL_WIDTH, LEVEL_HEIGHT) // рендерим текстуру локации

        batch.end() // конец рендера
    }

    override fun resume() {
        super.resume()
    }

    override fun show() {
        super.show()
    }
}
