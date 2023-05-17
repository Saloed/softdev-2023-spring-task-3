package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.BeatusL.mlnk.MLNK.Companion.scale
import com.github.BeatusL.mlnk.component.AnimationComponent
import com.github.BeatusL.mlnk.component.AnimationModel
import com.github.BeatusL.mlnk.component.AnimationPlaymode
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.component.SpawnComponent
import com.github.BeatusL.mlnk.component.SpawnConfig
import com.github.BeatusL.mlnk.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y


@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val atlas: TextureAtlas,
    private val spawnCmps: ComponentMapper<SpawnComponent>,
): EventListener, IteratingSystem() {
    private val cachedCfgs = mutableMapOf<String, SpawnConfig>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
            val config = spawmConfig(type)
            val relativeSize = size(config.model)

            world.entity {
                add<ImageComponent> {
                    image = Image().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }

                add<AnimationComponent> {
                    nextAnimation(config.model, config.animationMode)
                }
            }
        }
        world.remove(entity)
    }

    private fun spawmConfig(type: String): SpawnConfig = cachedCfgs.getOrPut(type) {
        when (type) {
            "Player" -> SpawnConfig(AnimationModel.player, AnimationPlaymode.Loop)
            "B" -> SpawnConfig(AnimationModel.B, AnimationPlaymode.Loop)
            "M" -> SpawnConfig(AnimationModel.M, AnimationPlaymode.Loop)
            "S" -> SpawnConfig(AnimationModel.S, AnimationPlaymode.Loop)
            "exps" -> SpawnConfig(AnimationModel.exps, AnimationPlaymode.Loop)
            "BP" -> SpawnConfig(AnimationModel.BP, AnimationPlaymode.Loop)
            "RP" -> SpawnConfig(AnimationModel.RP, AnimationPlaymode.Loop)
            else -> gdxError("$type has no spawn configuration!")
        }
    }

    private fun size(model: AnimationModel) = cachedSizes.getOrPut(model) {
        if (model.atlasKey == "exps") vec2(1.75f, 1.75f)
        else {
            val regions = atlas.findRegions("${model.atlasKey}/${model.atlasKey}")
            if (regions.isEmpty) gdxError("No region for ${model.atlasKey}")
            vec2(regions.first().originalWidth * scale, regions.first().originalHeight * scale)
        }
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                val entityLayer = event.map.layer("obj")
                entityLayer.objects.forEach { obj ->
                    val type = obj.type ?: gdxError("$obj has no type!")

                    world.entity {
                        add<SpawnComponent> {
                            this.type = type
                            this.location.set(obj.x * scale, obj.y * scale)
                        }
                    }

                }
                return true
            }
        }
        return false
    }
}


