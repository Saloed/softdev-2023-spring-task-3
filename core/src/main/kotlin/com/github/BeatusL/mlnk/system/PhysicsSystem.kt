package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.physics.box2d.World
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.component.PhysicsComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

@AllOf([PhysicsComponent::class, ImageComponent::class])
class PhysicsSystem(
    private val oWorld: World,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physCmps: ComponentMapper<PhysicsComponent>
): IteratingSystem(interval = Fixed(1/60f)) { // fixed timestep`s good for optimization

    override fun onUpdate() {
        if (oWorld.autoClearForces) {
            oWorld.autoClearForces = false
            log.error { "autoClearForces must be false but was true instead" }
        }
        super.onUpdate()
        oWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        oWorld.step(deltaTime, 6, 2) // 6 & 2 just taken from docs. dunno what they`re for
    }

    override fun onTickEntity(entity: Entity) {
        val physCmp = physCmps[entity]
        val imageCmp = imageCmps[entity]

        if (!physCmp.impulse.isZero) {
            physCmp.body.applyLinearImpulse(physCmp.impulse, physCmp.body.worldCenter, true)
            physCmp.impulse.setZero()
        }

        val (x, y) = physCmp.body.position
        imageCmp.image.run {
            setPosition(x - width * 0.5f, y - height * 0.5f)
        }
    }

    companion object {
        private val log = logger<PhysicsSystem>()
    }
}

