package com.github.BeatusL.mlnk.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.component.PhysicsComponent
import com.github.BeatusL.mlnk.event.MapChangeEvent
import com.github.BeatusL.mlnk.event.fire
import com.github.BeatusL.mlnk.input.KeyboardProcessor
import com.github.BeatusL.mlnk.system.AnimationSystem
import com.github.BeatusL.mlnk.system.DebugSystem
import com.github.BeatusL.mlnk.system.EntitySpawnSystem
import com.github.BeatusL.mlnk.system.MoveSystem
import com.github.BeatusL.mlnk.system.PhysicsSystem
import com.github.BeatusL.mlnk.system.RenderSystem
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2

class GameScreen: KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(9f, 16f))
    private val background: Texture = Texture("png/background.png")
    private val textureAtlas = TextureAtlas("assets/atlas/GameObj.atlas")

    private val oWorld = createWorld(gravity = vec2(0f, 0f)).apply {  // physic (object) world
        autoClearForces = false

    }

    private val rWorld: World = World { // world for render objects
        entityCapacity = 64
        inject(stage)
        inject(textureAtlas)
        inject(oWorld)

        componentListener<ImageComponent.Companion.ImageComponentListener>()
        componentListener<PhysicsComponent.Companion.PhysicsComponentListener>()

        system<MoveSystem>()
        system<PhysicsSystem>()
        system<EntitySpawnSystem>()
        system<AnimationSystem>()
        system<RenderSystem>()
        system<DebugSystem>()
    }
    override fun show() {
        rWorld.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }
        val map = TmxMapLoader().load("map/map.tmx")
        stage.fire(MapChangeEvent(map))

        KeyboardProcessor(rWorld, rWorld.mapper())

        log.debug { "GameScreen shown" }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        log.debug { "View resized" }
    }

    override fun render(delta: Float) {
        rWorld.update(delta.coerceAtMost(1/4f)) // delta cap needed to avoid stuttering
    }

    override fun dispose() {
        stage.disposeSafely()
        background.disposeSafely()
        textureAtlas.disposeSafely()
        rWorld.dispose()
        oWorld.dispose()
        log.debug { "Resources disposed" }
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}
