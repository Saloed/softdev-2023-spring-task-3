package com.github.kot512.surrounded_and_hunted

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.screen.TestScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxGame
import ktx.app.KtxScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class SurroundedAndHunted : KtxGame<KtxScreen>() {
    //    предзагружаемые ресурсы
    companion object {
//        константы
        var SCREEN_WIDTH = 0f
        var SCREEN_HEIGHT = 0f
        val PLAYER_POS = Point(0f, 0f)
        val L_JOYSTICK_POS = Point(0f, 0f)
        val R_JOYSTICK_POS = Point(0f, 0f)

//        текстуры
        lateinit var JOYSTICK_KNOB_TXTR: Texture
        lateinit var JOYSTICK_BASE_TXTR: Texture

        lateinit var RED_BALL: Texture
        lateinit var PLAYER_TXTR: Texture
        lateinit var BALL_ENEMY_TXTR: Texture
    }

    override fun create() {
        SCREEN_WIDTH = Gdx.graphics.width.toFloat()
        SCREEN_HEIGHT = Gdx.graphics.height.toFloat()
        PLAYER_POS.setPoint(Point(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2))
        L_JOYSTICK_POS.setPoint(Point(150f, 95f))
        R_JOYSTICK_POS.set(Point(GameScreen.SCREEN_WIDTH - 550f, 95f))

        JOYSTICK_KNOB_TXTR = Texture("graphics/test_image/joystick_knob.png")
        JOYSTICK_BASE_TXTR = Texture("graphics/test_image/joystick_base.png")
        RED_BALL = Texture("graphics/test_image/red_dot_png.png")
        PLAYER_TXTR = Texture("graphics/test_image/player_test_png.png")
        BALL_ENEMY_TXTR = Texture("graphics/test_image/ball_enemy_test.png")

//        addScreen(GameScreen())
//        setScreen<GameScreen>()

        addScreen(TestScreen())
        setScreen<TestScreen>()
    }

    override fun dispose() {
        JOYSTICK_BASE_TXTR.dispose()
        JOYSTICK_KNOB_TXTR.dispose()
        RED_BALL.dispose()
    }


    override fun pause() {
        super.pause()
    }

    override fun render() {
        super.render()
    }


    override fun resume() {
        super.resume()
    }
}
