package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.BeatusL.mlnk.component.AnimationComponent
import com.github.BeatusL.mlnk.component.AnimationComponent.Companion.no_animation
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.collections.map
import ktx.log.logger

@AllOf([AnimationComponent::class, ImageComponent::class])
class AnimationSystem(
    private val textureAtlas: TextureAtlas,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val imageCmps: ComponentMapper<ImageComponent>
): IteratingSystem() {

    private val cachedAnim = mutableMapOf<String, Animation<TextureRegionDrawable>>()

    override fun onTickEntity(entity: Entity) {
        val aCmps = animationCmps[entity]


        if (aCmps.nextAnimation == no_animation) {
            aCmps.stateTime += deltaTime
        } else {
            aCmps.animation = animation(aCmps.nextAnimation)
            aCmps.stateTime = 0f
            aCmps.nextAnimation = no_animation
        }
        aCmps.animation.playMode = aCmps.playMode
        imageCmps[entity].image.drawable = aCmps.animation.getKeyFrame(aCmps.stateTime)

        if (aCmps.playMode == Animation.PlayMode.NORMAL && aCmps.stateTime >= 1/4f) // 4 frames times 1/16 fps
            world.remove(entity)
    }

    private fun animation(keyPath: String): Animation<TextureRegionDrawable> {
        return cachedAnim.getOrPut(keyPath) {
            log.debug { "New animation created for $keyPath" }
            val regions = textureAtlas.findRegions(keyPath)
            if (regions.isEmpty) {
                gdxError("No such texture regions!")
            }
            Animation(default_duration, regions.map { TextureRegionDrawable(it) }) // map from ktx.collection
                                // ^ 16fps
        }
    }

    companion object {
        private val log = logger<AnimationSystem>()
        private const val default_duration = 1/16f
    }
}
