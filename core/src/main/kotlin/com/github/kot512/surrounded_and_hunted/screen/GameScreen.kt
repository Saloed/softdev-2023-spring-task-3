package com.github.kot512.surrounded_and_hunted.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.app.KtxScreen
import ktx.log.logger
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import ktx.assets.disposeSafely

class GameScreen: KtxScreen {

//    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
//    private val texture: Texture = Texture("graphics/test_image/red_dot_png.png")


//    companion object {
//        private val log = logger<GameScreen>()
//    }

    override fun show() {
//        log.debug { "GameScreen gets shown" }
//
//        stage.addActor(
//            Image(texture).apply {
//                setPosition(1f, 1f)
//                setSize(1f, 1f)
//                setScaling(Scaling.fill)
//            }
//        )
    }

    override fun render(delta: Float) {
//        with(stage) {
//            act(delta)
//            draw()
//        }

    }

    override fun resize(width: Int, height: Int) {
//        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
//        stage.disposeSafely()
//        texture.disposeSafely()
//    }
}
