package com.github.BeatusL.mlnk.system



import com.badlogic.gdx.utils.TimeUtils
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.component.ProjectileComponent
import com.github.BeatusL.mlnk.component.SpawnComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.logger


@AllOf([ProjectileComponent::class, ImageComponent::class])
class ProjectileSystem(
    private val imCmps: ComponentMapper<ImageComponent>,
    private val prjCmps: ComponentMapper<ProjectileComponent>
): IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val prjCmp = prjCmps[entity]
        val imsCmp = imCmps[entity]

        if (TimeUtils.nanoTime() - prjCmp.prevTime > 1000000000) {
            prjCmp.prevTime = TimeUtils.nanoTime()
            val x = imsCmp.image.x + (imsCmp.image.width / 2f) - 0.4f
            val y = imsCmp.image.y + if (prjCmp.prjType == EntityType.BP) imsCmp.image.height
            else - imsCmp.image.height
            world.entity {
                add<SpawnComponent> {
                    this.type = prjCmp.prjType
                    this.location.set(x, y)
                }
            }
            //log.debug { "projectile created at $x:$y" }
        }
    }



    companion object{
        val log = logger<ProjectileSystem>()
    }

}
