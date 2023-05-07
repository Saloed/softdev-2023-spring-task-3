package com.github.kot512.surrounded_and_hunted

import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class SurroundedAndHunted : KtxGame<KtxScreen>() {

    override fun create() {
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
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
