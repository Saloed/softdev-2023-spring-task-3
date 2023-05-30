package com.github.kot512.surrounded_and_hunted.screen.playable_screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.PLAYER_TXTR
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_HEIGHT
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_WIDTH
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.entities.enemy_manager.EnemyManager
import com.github.kot512.surrounded_and_hunted.hud.UIManager
import ktx.app.KtxScreen
import ktx.assets.disposeSafely

abstract class BaseLocationScreen : KtxScreen {
//    настройка кнопок и состояния паузы
    enum class ScreenState {
        PAUSED, PLAYING
    }


    //    параметры экрана
    var state: ScreenState = ScreenState.PLAYING
//    параметры локации
    abstract val locationTexture: Texture
    abstract val locationWidth: Float
    abstract val locationHeight: Float

//    для экрана
    private val camera: Camera = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT) // камера
    private val hudCamera: Camera = OrthographicCamera() // камера, рисующая интерфейс
    private val viewport: Viewport = FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, hudCamera)

//    графика
    private val batch: SpriteBatch = SpriteBatch()

//    интерфейс
    val uiManager: UIManager = UIManager(this)
    val stage: Stage = Stage(viewport) // сцена, ответственная за рендер UI
//    private val movJoystick: MovementJoystick =
//        MovementJoystick(L_JOYSTICK_POS)
//    private val aimJoystick: AimJoystick =
//        AimJoystick(R_JOYSTICK_POS)

//    игровые сущности
    val player: Player =
        Player(
            this,
            PLAYER_TXTR,
            uiManager.movJoystick,
            uiManager.aimJoystick
        )

    val enemyManager: EnemyManager = EnemyManager(
        this, player
    )

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_CLEAR_VALUE)

//        рендер игровых объектов
        batch.begin() // начало рендера
        batch.projectionMatrix = camera.combined

        camera.position.set(player.originBasedX, player.originBasedY, 0f)  // обновление позиции камеры
        camera.update()

        batch.draw(locationTexture, 0f, 0f, locationWidth, locationHeight) // рендерим текстуру локации
        player.draw(batch)
        enemyManager.draw(batch)

        batch.end() // конец рендера

//        рендер HUD
        stage.draw()
    }

    override fun dispose() {
        locationTexture.dispose()
        stage.disposeSafely()
    }

    override fun resume() {
        super.resume()
    }

    override fun show() {
        uiManager.setupActors()
    }

    override fun hide() {
        super.hide()
    }

    override fun pause() {
        super.pause()
    }
}
