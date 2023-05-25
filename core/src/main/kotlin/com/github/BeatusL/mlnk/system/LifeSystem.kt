package com.github.BeatusL.mlnk.system

import com.github.BeatusL.mlnk.component.DeadComponent
import com.github.BeatusL.mlnk.component.LifeComponent
import com.github.BeatusL.mlnk.component.PlayerComponent
import com.github.BeatusL.mlnk.game
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.NoneOf


@AllOf([LifeComponent::class])
@NoneOf([DeadComponent::class])
class LifeSystem(
    private val lifeCmps: ComponentMapper<LifeComponent>,
    private val deadCmps: ComponentMapper<DeadComponent>,
    private val playerCmps: ComponentMapper<PlayerComponent>
): IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val lifeCmp = lifeCmps[entity]

        if (lifeCmp.takeDamage > 0f){
            lifeCmp.life -= lifeCmp.takeDamage
            lifeCmp.takeDamage = 0f
        }

        if (lifeCmp.isDead){
            configureEntity(entity) {
                deadCmps.add(it)
                if (it in playerCmps) game.playerDead()
            }
        }
    }
}
