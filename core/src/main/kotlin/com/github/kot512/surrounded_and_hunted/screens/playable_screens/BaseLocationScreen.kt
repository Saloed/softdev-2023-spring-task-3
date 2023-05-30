package com.github.kot512.surrounded_and_hunted.screens.playable_screens

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
import com.github.kot512.surrounded_and_hunted.entities_and_objects.Player
import com.github.kot512.surrounded_and_hunted.entities_and_objects.bonuses.BonusesManager
import com.github.kot512.surrounded_and_hunted.entities_and_objects.enemy_manager.EnemyManager
import com.github.kot512.surrounded_and_hunted.hud.UIManager
import ktx.app.KtxScreen
import ktx.assets.disposeSafely

abstract class BaseLocationScreen : KtxScreen {
//    параметры локации
    abstract val locationTexture: Texture
    abstract val locationWidth: Float
    abstract val locationHeight: Float

//    для экрана
    private val camera: Camera = OrthographicCamera(CONST_AND_VAR.SCREEN_WIDTH, CONST_AND_VAR.SCREEN_HEIGHT) // камера
    private val hudCamera: Camera = OrthographicCamera() // камера, рисующая интерфейс
    private val viewport: Viewport = FitViewport(CONST_AND_VAR.SCREEN_WIDTH, CONST_AND_VAR.SCREEN_HEIGHT, hudCamera)

//    графика
    private val batch: SpriteBatch = SpriteBatch()

//    интерфейс
    private val uiManager: UIManager = UIManager(this)
    val stage: Stage = Stage(viewport) // сцена, ответственная за рендер UI

//    инициализация игровых сущностей/объектов
    val player: Player =
        Player(
            this,
            CONST_AND_VAR.PLAYER_TXTR,
            uiManager.movJoystick,
            uiManager.aimJoystick
        )
    val enemyManager: EnemyManager = EnemyManager(this)
    val bonusesManager: BonusesManager = BonusesManager(this)

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_CLEAR_VALUE)

//        рендер игровых объектов
        batch.begin() // начало рендера объектов
        batch.projectionMatrix = camera.combined

        camera.position.set(player.originBasedX, player.originBasedY, 0f)  // обновление позиции камеры
        camera.update()

        batch.draw(locationTexture, 0f, 0f, locationWidth, locationHeight) // рендерим текстуру локации
        player.draw(batch)
        enemyManager.draw(batch)
        bonusesManager.draw(batch)

        batch.end() // конец рендера объектов

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
