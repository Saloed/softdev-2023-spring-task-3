package com.github.BeatusL.mlnk.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class EntryScreen: KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(9f, 16f))
    private val background: Texture = Texture("assets/png/background.png")
    private val cloud: Texture = Texture("assets/png/clouds.png")
    private val label: Texture = Texture("assets/png/entry.png")

    override fun show() {
        log.debug { "GameScreen shown" }
        stage.addActor(
            Image(background).apply {
                setPosition(0f, 0f)
                setSize(9f, 16f)
                setScaling(Scaling.stretch)
            }
        )

        stage.addActor(
            Image(cloud).apply {
                setPosition(0f, 6f)
                setSize(9f, 8f)
                setScaling(Scaling.fill)
            }
        )

        stage.addActor(
            Image(label).apply {
                setPosition(2f, 9f)
                setSize(4f, 2f)
                setScaling(Scaling.fill)
            }
        )
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        with(stage) {
            act(delta)
            draw()
        }
    }

    override fun dispose() {
        stage.dispose()
        background.disposeSafely()
        cloud.disposeSafely()
        label.disposeSafely()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}
