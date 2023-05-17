package com.github.BeatusL.mlnk.system

import com.github.BeatusL.mlnk.component.MoveComponent
import com.github.BeatusL.mlnk.component.PhysicsComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.math.component1
import ktx.math.component2


@AllOf([MoveComponent::class, PhysicsComponent::class])
class MoveSystem (
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val physCmps: ComponentMapper<PhysicsComponent>,
): IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val moveCmp = moveCmps[entity]
        val physCmp = physCmps[entity]
        val mass = physCmp.body.mass
        val (velocityX, velocityY) = physCmp.body.linearVelocity

        if (moveCmp.cos == 0f && moveCmp.sin == 0f) {
            physCmp.impulse.set(  // this part is needed to stop the body once no input is provided
                mass * (-velocityX),
                mass * (-velocityY)
            )
            return
        }

        physCmp.impulse.set(       // keeps the speed constant
            mass * (moveCmp.speed * moveCmp.cos - velocityX),
            mass * (moveCmp.speed * moveCmp.sin - velocityY)
        )
    }

}
