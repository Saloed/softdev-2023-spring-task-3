package com.github.BeatusL.mlnk.system


import com.badlogic.gdx.math.Vector2
import com.github.BeatusL.mlnk.component.DeadComponent
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.component.PhysicsComponent
import com.github.BeatusL.mlnk.component.ProjectileComponent
import com.github.BeatusL.mlnk.component.SpawnComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem

@AllOf([DeadComponent::class, PhysicsComponent::class])
class DeadSystem(
    private val deadCmps: ComponentMapper<DeadComponent>,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val projectileCmps: ComponentMapper<ProjectileComponent>,
): IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val deadCmp = deadCmps[entity]
        val location = Vector2(imageCmps[entity].image.x, imageCmps[entity].image.y)
        if (!deadCmp.revival) {

            if (entity in projectileCmps) {
                world.entity {
                    add<SpawnComponent> {
                        this.type = EntityType.exps
                        this.location.set(location)
                    }
                }
            }

            world.remove(entity)
        }
    }
}
