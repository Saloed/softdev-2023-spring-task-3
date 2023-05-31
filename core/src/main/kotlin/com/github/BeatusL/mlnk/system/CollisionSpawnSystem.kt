package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.github.BeatusL.mlnk.MLNK.Companion.scale
import com.github.BeatusL.mlnk.component.PhysicsComponent
import com.github.BeatusL.mlnk.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.box2d.body
import ktx.box2d.loop
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.width


@AllOf([PhysicsComponent::class])
class CollisionSpawnSystem(
    private val oWorld: World
): EventListener, IteratingSystem() {

    override fun onTickEntity(entity: Entity) = Unit

    override fun handle(event: Event?): Boolean {
        when(event) {
            is MapChangeEvent -> {

                world.entity {
                    val w = event.map.width.toFloat() * scale
                    val h = event.map.height.toFloat() * scale

                    add<PhysicsComponent> {
                        body = oWorld.body(BodyDef.BodyType.StaticBody) {
                            position.set(0f, 0f)
                            fixedRotation = true
                            allowSleep = false
                            loop(
                                vec2(0f, 0f),
                                vec2(w, 0f),
                                vec2(w, h),
                                vec2(0f, h)
                                )
                        }
                    }
                }

                return true
            }
        }
        return false
    }
}
