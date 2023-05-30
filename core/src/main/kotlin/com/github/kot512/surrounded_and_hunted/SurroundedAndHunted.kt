package com.github.kot512.surrounded_and_hunted

import com.github.kot512.surrounded_and_hunted.screens.image_screens.MainMenuImageScreen
import com.github.kot512.surrounded_and_hunted.tools.ConstantsAndVariables
import ktx.app.KtxGame
import ktx.app.KtxScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class SurroundedAndHunted : KtxGame<KtxScreen>() {
    companion object {
        lateinit var CONST_AND_VAR: ConstantsAndVariables
    }

    override fun create() {
        CONST_AND_VAR = ConstantsAndVariables()

//        активируем экран главного меню
        addScreen(MainMenuImageScreen())
        setScreen<MainMenuImageScreen>()
    }

    override fun dispose() {
        CONST_AND_VAR.dispose()
    }
}
