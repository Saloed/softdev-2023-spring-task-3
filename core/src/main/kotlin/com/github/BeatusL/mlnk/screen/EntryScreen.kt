package com.github.BeatusL.mlnk.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.BeatusL.mlnk.MLNK
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.event.MapChangeEvent
import com.github.BeatusL.mlnk.event.fire
import com.github.BeatusL.mlnk.system.AnimationSystem
import com.github.BeatusL.mlnk.system.RenderSystem
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class EntryScreen: KtxScreen {
    private val gStage: Stage = Stage(ExtendViewport(9f, 16f))
    private val uiStage: Stage = Stage(ExtendViewport(360f, 640f))
    private val textureAtlas = TextureAtlas("atlas/GameObj.atlas")
    private val font = BitmapFont(Gdx.files.internal("font.fnt"))
    private val textStyle = Label.LabelStyle(font, Color.BLACK)


    private val rWorld: World = World { // world for render objects
        entityCapacity = MLNK.entityCount
        inject(gStage)
        inject(textureAtlas)
        inject("UI", uiStage)

        componentListener<ImageComponent.Companion.ImageComponentListener>()

        system<AnimationSystem>()
        system<RenderSystem>()
    }
    override fun show() {

        rWorld.systems.forEach { system ->
            if (system is EventListener) {
                gStage.addListener(system)
                uiStage.addListener(system)
            }
        }

        val map = TmxMapLoader().load("map/entry.tmx")

        gStage.fire(MapChangeEvent(map))

        createLabel()

        log.debug { "GameScreen shown" }
    }

    override fun resize(width: Int, height: Int) {
        gStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
        log.debug { "View resized" }
    }

    override fun render(delta: Float) {
        rWorld.update(delta.coerceAtMost(1/4f)) // delta cap needed to avoid stuttering
    }

    override fun dispose() {
        font.disposeSafely()
        gStage.disposeSafely()
        uiStage.disposeSafely()
        textureAtlas.disposeSafely()
        rWorld.dispose()
        log.debug { "Resources disposed" }
    }

    private fun createLabel() {
        val label1 = Label(labelText1, textStyle)
        label1.setPosition(labelLocation.x, labelLocation.y)
        uiStage.addActor(label1)

        val label2 = Label(labelText2, textStyle)
        label2.setPosition(labelLocation.x, labelLocation.y - 30)
        uiStage.addActor(label2)
    }



    companion object {
        private const val labelText1 = "Hello there!"
        private const val  labelText2 = "Touch anywhere to start"
        private val labelLocation = Vector2(10f, 410f)
        private val log = logger<GameScreen>()
    }
}
