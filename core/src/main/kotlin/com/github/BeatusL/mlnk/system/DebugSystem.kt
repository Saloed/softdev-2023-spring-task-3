package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import ktx.assets.disposeSafely
import ktx.graphics.use

class DebugSystem(
    private val oWorld: World,
    private val stage: Stage
) :IntervalSystem(enabled = true) {

    private lateinit var physRender: Box2DDebugRenderer
    private lateinit var hbRenderer: ShapeRenderer

    init {
        if (enabled) {
            physRender = Box2DDebugRenderer()
            hbRenderer = ShapeRenderer()
        }
    }

    override fun onTick() {
        physRender.render(oWorld, stage.camera.combined)
        hbRenderer.use(ShapeRenderer.ShapeType.Line, stage.camera.combined) {
            it.setColor(1f, 0f, 0f, 0f)
//            it.rect(rect.x, rect.y, rect.width - rect.x, rect.height - rect.y)
        }
    }

    override fun onDispose() {
        if (enabled) {
            physRender.disposeSafely()
            hbRenderer.disposeSafely()
        }
    }
}
