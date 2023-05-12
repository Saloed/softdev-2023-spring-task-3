package com.github.BeatusL.mlnk

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.BeatusL.mlnk.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen


class MLNK : KtxGame<KtxScreen>() {

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}
