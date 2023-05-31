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
import com.github.BeatusL.mlnk.component.AttackComponent
import com.github.BeatusL.mlnk.component.CollisionComponent
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.component.LifeComponent
import com.github.BeatusL.mlnk.component.MoveComponent
import com.github.BeatusL.mlnk.component.PhysicsComponent.Companion.physicCmpFromImage
import com.github.BeatusL.mlnk.component.PlayerComponent
import com.github.BeatusL.mlnk.component.ProjectileComponent
import com.github.BeatusL.mlnk.component.SpawnComponent
import com.github.BeatusL.mlnk.component.SpawnConfig
import com.github.BeatusL.mlnk.event.EnemyDead
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
    private val cachedCfgs = mutableMapOf<EntityType, SpawnConfig>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
            val config = spawmConfig(type)
            val relativeSize = size(config.model)
            val isShip = type in listOf(EntityType.Player, EntityType.B, EntityType.M, EntityType.S)


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

                physicCmpFromImage(
                    oWorld,
                    imageCmp.image,
                    config.bodyType
                ) { phCmp, width, height ->
                    box(width, height) {
                        if (isShip) userData = HITBOX
                        //friction = 0f from 0 to 10
                        isSensor = false
                        phCmp.size.set(width, height)
                    }

                }


                if (config.speed > 0f) {
                    add<MoveComponent> {
                        speed = config.speed
                        sin = when (type) {
                            EntityType.BP -> 1f
                            EntityType.Player -> 0f
                            else -> -1f
                        }
                    }
                }

                when(type) {
                    EntityType.Player -> {
                        add<PlayerComponent> {
                            x = location.x
                            y = location.y
                        }

                        add<CollisionComponent> {
                            borderCollisionEnabled = true
                        }

                        add<ProjectileComponent>() {
                            prevTime = TimeUtils.nanoTime()
                            prjType = EntityType.BP
                            prjMultiplier = 1.2f
                        }

                        add<LifeComponent> {
                            lType = type
                        }

                        add<AttackComponent>() {
                            friendly = true
                        }
                    }

                    EntityType.S -> {
                        add<CollisionComponent>()

                        add<ProjectileComponent>() {
                            prevTime = TimeUtils.nanoTime()
                            prjType = EntityType.RP
                            prjMultiplier = 1.0f
                        }

                        add<LifeComponent>() {lType = type}

                        add<AttackComponent>()
                    }

                    EntityType.M -> {
                        add<CollisionComponent>()

                        add<ProjectileComponent>() {
                            prevTime = TimeUtils.nanoTime()
                            prjType = EntityType.RP
                            prjMultiplier = 1.2f
                        }

                        add<LifeComponent>() {lType = type}

                        add<AttackComponent>()
                    }

                    EntityType.B -> {
                        add<CollisionComponent>()

                        add<ProjectileComponent>() {
                            prevTime = TimeUtils.nanoTime()
                            prjType = EntityType.RP
                            prjMultiplier = 1.5f
                        }

                        add<LifeComponent>() {lType = type}

                        add<AttackComponent>()
                    }

                    EntityType.BP -> {
                        add<CollisionComponent>()

                        add<LifeComponent>() {lType = type}

                        add<AttackComponent>() {friendly = true}
                    }

                    EntityType.RP -> {
                        add<CollisionComponent>()

                        add<LifeComponent>() {lType = type}

                        add<AttackComponent>()
                    }

                    else -> {
                        add<LifeComponent>() {lType = type}

                        add<AttackComponent> {
                            powerUp = true
                            friendly = true
                            damage = 0
                        }
                    }
                }






            }

        }
        world.remove(entity)
    }

    private fun spawmConfig(type: EntityType): SpawnConfig =
        cachedCfgs.getOrPut(type) {
        SpawnConfigGetter().spawmConfig(type)
    }

    private fun size(model: AnimationModel) = cachedSizes.getOrPut(model) {
        if (model == AnimationModel.exps) vec2(expsModelSide, expsModelSide)
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
                    val type = SpawnConfigGetter().stringToType(obj.type)

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
                val type = SpawnConfigGetter().stringToType(event.type)
                val loc = event.location

                world.entity {
                    add<SpawnComponent> {
                        this.type = type
                        this.location.set(loc.x, loc.y)
                    }
                }
                return true
            }

            is EnemyDead ->
                if ((0..100).random() <= boxDropChance) {
                    val x = event.location.x
                    val y = event.location.y

                    world.entity {
                        add<SpawnComponent> {
                            this.type = EntityType.Box
                            this.location.set(x, y)
                        }
                    }

                }

        }
        return false
    }


    companion object {
        const val boxDropChance = 15 // in %
        const val expsModelSide = 1.75f
        const val HITBOX = "HITBOX"
    }

}


