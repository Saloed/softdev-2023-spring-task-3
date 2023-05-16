package com.github.kot512.surrounded_and_hunted.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.L_JOYSTICK_POS
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.PLAYER_POS
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.PLAYER_TXTR
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.RED_BALL
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.R_JOYSTICK_POS
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_HEIGHT
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.SCREEN_WIDTH
import com.github.kot512.surrounded_and_hunted.controls.AimJoystick
import com.github.kot512.surrounded_and_hunted.controls.MovementJoystick
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.entities.enemy_manager.EnemyManager
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxScreen

abstract class BaseLocationScreen(

) : KtxScreen {
//    параметры локации
    abstract val locationTexture: Texture
    abstract val locationWidth: Float
    abstract val locationHeight: Float

    private val boundsX: ClosedFloatingPointRange<Float> = 0f..locationWidth
    private val boundsY: ClosedFloatingPointRange<Float> = 0f..locationHeight

    open val playerSpawnPoint =
        Point(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)

//    для экрана
    private val camera: Camera = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT) // камера
    private val hudCamera: Camera = OrthographicCamera() // камера, рисующая интерфейс
    private val viewport: Viewport = FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, hudCamera)

//    графика
    private val batch: SpriteBatch = SpriteBatch()

//    интерфейс
    private val stage: Stage = Stage(viewport) // сцена, ответственная за рендер UI
    private val movJoystick: MovementJoystick =
        MovementJoystick(L_JOYSTICK_POS)
    private val aimJoystick: AimJoystick =
        AimJoystick(R_JOYSTICK_POS)

//    игровые сущности
    private val player: Player =
        Player(
            PLAYER_TXTR,
            PLAYER_POS,
            movJoystick,
            aimJoystick
        )

    private val enemyManager: EnemyManager = EnemyManager( // TODO(сделать абстрактным и для уник. скрина уник. менеджер)
        boundsX, boundsY, player
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
        stage.addActor(movJoystick)
        stage.addActor(aimJoystick)
        stage.draw()
    }

    override fun dispose() {
        locationTexture.dispose()
    }

    override fun resume() {
        super.resume()
    }

    override fun show() {
        super.show()
    }

    override fun hide() {
        super.hide()
    }

    override fun pause() {
        super.pause()
    }
}
