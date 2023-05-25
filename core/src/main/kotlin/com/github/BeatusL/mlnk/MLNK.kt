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
    private var screen = ScreenSelect.Entry

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(RestartScreen())
        addScreen(EntryScreen())
        setScreen<EntryScreen>()
    }

    override fun render() {
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) &&
            (screen == ScreenSelect.Entry || screen == ScreenSelect.Restart)) {
            addScreen(GameScreen())
            setScreen<GameScreen>()
            screen = ScreenSelect.Game
            playerAlive = true
        }
        else if (!playerAlive && screen != ScreenSelect.Restart) {
            removeScreen<GameScreen>()
            setScreen<RestartScreen>()
            screen = ScreenSelect.Restart
        }
        super.render()
    }

    enum class ScreenSelect {
        Entry, Restart, Game
    }



    companion object {
        const val scale = 1/20f
        const val entityCount = 64
        var playerAlive = true
        val debug = false
    }
}
