package com.github.kot512.surrounded_and_hunted

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import jdk.internal.org.jline.utils.WCWidth
import ktx.app.KtxGame
import ktx.app.KtxScreen
import kotlin.properties.Delegates

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class SurroundedAndHunted : KtxGame<KtxScreen>() {
    var HEIGHT: Int = 0
    var WIDTH: Int = 0

    override fun create() {
        WIDTH = Gdx.graphics.height
        HEIGHT = Gdx.graphics.width

//        Gdx.app.logLevel = Application.LOG_DEBUG
//        addScreen(GameScreen())

        setScreen<GameScreen>()
    }

}
