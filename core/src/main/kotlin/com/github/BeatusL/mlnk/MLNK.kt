package com.github.BeatusL.mlnk

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.github.BeatusL.mlnk.screen.EntryScreen
import com.github.BeatusL.mlnk.screen.GameScreen
import com.github.BeatusL.mlnk.screen.RestartScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen


class MLNK : KtxGame<KtxScreen>() {
    private var screen = "E"

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(RestartScreen())
        addScreen(EntryScreen())
        setScreen<EntryScreen>()
    }

    override fun render() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && (screen == "E" || screen == "R")) {
            addScreen(GameScreen())
            setScreen<GameScreen>()
            screen = "G"
            playerAlive = true
        }
        else if (!playerAlive && screen != "R") {
            removeScreen<GameScreen>()
            setScreen<RestartScreen>()
            screen = "R"
        }
        super.render()
    }


    companion object {
        const val scale = 1/20f
        var playerAlive = true
        val debug = true
    }
}
