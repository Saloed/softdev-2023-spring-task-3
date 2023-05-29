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

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(RestartScreen(0))
        addScreen(EntryScreen())
        setScreen<EntryScreen>()
    }

    override fun render() {
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) &&
            !this.containsScreen<GameScreen>()) {
            removeScreen<RestartScreen>()
            addScreen(GameScreen())
            setScreen<GameScreen>()
        }
        super.render()
    }

    fun playerDead() {
        this.addScreen(RestartScreen(gameScore))
        this.setScreen<RestartScreen>()
        this.removeScreen<GameScreen>()
    }

    fun setScore(score: Long) {
        gameScore = score
    }



    companion object {
        private var gameScore: Long = 0
        const val scale = 1/20f
        const val entityCount = 64
        val debug = false
    }
}

val game = MLNK()
