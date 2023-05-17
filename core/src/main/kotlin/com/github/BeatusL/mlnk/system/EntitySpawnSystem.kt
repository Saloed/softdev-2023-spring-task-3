package com.github.BeatusL.mlnk.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.TimeUtils
import com.github.BeatusL.mlnk.MLNK.Companion.scale
import com.github.BeatusL.mlnk.component.AnimationComponent
import com.github.BeatusL.mlnk.component.AnimationModel
import com.github.BeatusL.mlnk.component.AnimationPlaymode
import com.github.BeatusL.mlnk.component.CollisionComponent
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.component.MoveComponent
import com.github.BeatusL.mlnk.component.PhysicsComponent.Companion.physicCmpFromImage
import com.github.BeatusL.mlnk.component.PlayerComponent
import com.github.BeatusL.mlnk.component.ProjectileComponent
import com.github.BeatusL.mlnk.component.SpawnComponent
import com.github.BeatusL.mlnk.component.SpawnConfig
import com.github.BeatusL.mlnk.event.MapChangeEvent
import com.github.BeatusL.mlnk.event.ObjCreation
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y


@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val oWorld: World,
    private val atlas: TextureAtlas,
    private val spawnCmps: ComponentMapper<SpawnComponent>,
): EventListener, IteratingSystem() {
    private val cachedCfgs = mutableMapOf<String, SpawnConfig>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
            val config = spawmConfig(type)
            val relativeSize = size(config.model)
            val collisionEnabled = type == "Player"

            world.entity {
                val imageCmp = add<ImageComponent> {
                    image = Image().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }

                add<AnimationComponent> {
                    nextAnimation(config.model, config.animationMode)
                }

                physicCmpFromImage(oWorld, imageCmp.image, config.bodyType){
                    phCmp, width, height ->
                    box(width, height) {
                        userData = entity.id
                        //friction = 0f from 0 to 10
                        isSensor = false
                    }

                }

                add<CollisionComponent> {
                    borderCollisionEnabled = collisionEnabled
                }


                if (config.speed > 0f) {
                    add<MoveComponent> {
                        speed = config.speed
                        sin = when (type) {
                            "BP" -> 1f
                            "Player" -> 0f
                            else -> -1f
                        }
                    }

                    if (type == "Player") add<PlayerComponent>()

                    if (type in listOf("Player", "B", "M", "S")) add<ProjectileComponent>()
                    {
                        prevTime = TimeUtils.nanoTime()
                        prjType = when(type) {
                            "Player" -> "BP"
                            else -> "RP"
                        }
                    }
                }
            }
        }
        world.remove(entity)
    }

    private fun spawmConfig(type: String): SpawnConfig = cachedCfgs.getOrPut(type) {
        when (type) {
            "Player" -> SpawnConfig(AnimationModel.player, AnimationPlaymode.Loop, 7f)
            "B" -> SpawnConfig(AnimationModel.B, AnimationPlaymode.Loop, 5f)
            "M" -> SpawnConfig(AnimationModel.M, AnimationPlaymode.Loop, 4f)
            "S" -> SpawnConfig(AnimationModel.S, AnimationPlaymode.Loop, 4f)
            "exps" -> SpawnConfig(AnimationModel.exps, AnimationPlaymode.Normal, 0f)
            "BP" -> SpawnConfig(AnimationModel.BP, AnimationPlaymode.Loop, 10f)
            "RP" -> SpawnConfig(AnimationModel.RP, AnimationPlaymode.Loop, 8f)
            //"bound" -> SpawnConfig() to be implemented
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

            is ObjCreation -> {
                val type = event.type
                val loc = event.location

                world.entity {
                    add<SpawnComponent> {
                        this.type = type
                        this.location.set(loc.x, loc.y)
                    }
                }
                return true
            }

        }
        return false
    }


}


