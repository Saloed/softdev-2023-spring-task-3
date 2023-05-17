package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import ktx.assets.disposeSafely

class DebugSystem(
    private val oWorld: World,
    private val stage: Stage
) :IntervalSystem(enabled = true) {
    private lateinit var physRender: Box2DDebugRenderer

    init {
        if (enabled) {
            physRender = Box2DDebugRenderer()
        }
    }

    override fun onTick() {
        physRender.render(oWorld, stage.camera.combined)
    }

    override fun onDispose() {
        if (enabled) physRender.disposeSafely()
    }
}
