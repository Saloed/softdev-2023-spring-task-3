package com.github.kot512.surrounded_and_hunted.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.app.KtxScreen
import ktx.log.logger
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.entities.tools.Point
import ktx.assets.disposeSafely
import javax.swing.text.View

class GameScreen: KtxScreen {
//    параметры мира
    private val levelWidth = Gdx.graphics.width.toFloat()
    private val levelHeight = Gdx.graphics.height.toFloat()

//    для экрана
    private val camera: Camera = OrthographicCamera(levelWidth, levelHeight) // камера
    private val viewport: Viewport =
        StretchViewport(levelWidth, levelHeight, camera)

//    графика
    private val batch: SpriteBatch = SpriteBatch()
    private val locationTexture: Texture = Texture("graphics/test_image/dark_grass_background.jpg") // TODO()
//    игровые объекты
//    private val player: Player = Player(
//    Texture("graphics/test_image/red_dot_png.png"),
//    levelWidth / 20, levelWidth / 20, Point(levelWidth / 2, levelHeight / 2),
//    10f, 100
//    )
    private val stage: Stage = Stage()



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
//        batch.projectionMatrix = camera.combined TODO(Чета не работает)

//        super.render(delta)
        batch.begin() // начало рендера

        batch.draw(locationTexture, 0f, 0f, levelWidth, levelHeight) // рендерим текстуру локации
        player.draw(batch)

        batch.end() // конец рендера
    }

    override fun resume() {
        super.resume()
    }

    override fun show() {
        super.show()
    }
}
