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
        addScreen(RestartScreen())
        addScreen(EntryScreen())
        setScreen<EntryScreen>()
    }

    override fun render() {
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) &&
            !this.containsScreen<GameScreen>()) {
            addScreen(GameScreen())
            setScreen<GameScreen>()
        }
        super.render()
    }

    fun playerDead() {
        this.setScreen<RestartScreen>()
        this.removeScreen<GameScreen>()
    }



    companion object {
        const val scale = 1/20f
        const val entityCount = 64
        val debug = false
    }
}

val game = MLNK()
