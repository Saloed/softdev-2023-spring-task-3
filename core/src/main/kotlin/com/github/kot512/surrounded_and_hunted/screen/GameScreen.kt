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
import com.github.kot512.surrounded_and_hunted.controls.Joystick
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.entities.PlayerSprite
import com.github.kot512.surrounded_and_hunted.tools.Point

class GameScreen: KtxScreen {
//    параметры мира
    companion object {
        val LEVEL_WIDTH = Gdx.graphics.width.toFloat()
        val LEVEL_HEIGHT = Gdx.graphics.height.toFloat()
    }

//    для экрана
    private val camera: Camera = OrthographicCamera() // камера
    private val viewport: Viewport = FitViewport(LEVEL_WIDTH, LEVEL_HEIGHT, camera)


//    графика
    private val batch: SpriteBatch = SpriteBatch()
    private val locationTexture: Texture = Texture("graphics/test_image/dark_grass_background.jpg")
    private val jBaseTexture: Texture = Texture("graphics/test_image/joystick_base.png")
    private val jKnobTexture: Texture = Texture("graphics/test_image/joystick_knob.png")

//    игровые объекты
    private val stage: Stage = Stage(viewport) // сцена, ответственная за рендер UI
    private val joystick: Joystick = Joystick(jBaseTexture, jKnobTexture)
    private val player: PlayerSprite = PlayerSprite(Texture("graphics/test_image/red_dot_png.png"), joystick)
//        Point(LEVEL_WIDTH / 2, LEVEL_HEIGHT / 2),

    init {
        batch.projectionMatrix = camera.combined
        Gdx.input.inputProcessor = stage
    }



    override fun dispose() {
        locationTexture.dispose()
        jBaseTexture.dispose()
        jKnobTexture.dispose()
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

//        super.render(delta)
        batch.begin() // начало рендера

        batch.draw(locationTexture, 0f, 0f, LEVEL_WIDTH, LEVEL_HEIGHT) // рендерим текстуру локации
        player.draw(batch)

        batch.end() // конец рендера

        stage.addActor(joystick)
        stage.draw()
    }

    override fun resume() {
        super.resume()
    }

    override fun show() {
        super.show()
    }
}
