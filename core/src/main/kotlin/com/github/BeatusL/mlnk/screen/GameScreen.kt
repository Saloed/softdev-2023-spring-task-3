package com.github.BeatusL.mlnk.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.BeatusL.mlnk.component.AnimationComponent
import com.github.BeatusL.mlnk.component.AnimationType
import com.github.BeatusL.mlnk.component.ImageComponent
import com.github.BeatusL.mlnk.system.AnimationSystem
import com.github.BeatusL.mlnk.system.RenderSystem
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen: KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(9f, 16f))
    private val background: Texture = Texture("png/background.png")
    private val textureAtlas = TextureAtlas("assets/atlas/GameObj.atlas")
    private val world: World = World {
        entityCapacity = 64
        inject(stage)
        inject(textureAtlas)
        componentListener<ImageComponent.Companion.ImageComponentListener>()

        system<AnimationSystem>()
        system<RenderSystem>()
    }
    override fun show() {

        world.entity {
            add<ImageComponent> {
                image = Image(background).apply {
                    setPosition(0f, 0f)
                    setSize(9f, 16f)
                    rotation = 0f
                    setScaling(Scaling.stretch)
                }
            }
        }

        world.entity {
            add<ImageComponent> {
                image = Image().apply {
                    setSize(1f, 1f)
                    setPosition(1f, 0f)
                }
            }

            add<AnimationComponent> {
                nextAnimation("B", AnimationType.B)
            }
        }

        world.entity {
            add<ImageComponent> {
                image = Image().apply {
                    setSize(1f, 1f)
                    setPosition(2f, 0f)
                }
            }

            add<AnimationComponent> {
                nextAnimation("M", AnimationType.M)
            }
        }

        world.entity {
            add<ImageComponent> {
                image = Image().apply {
                    setSize(1f, 1f)
                    setPosition(3f, 0f)
                }
            }

            add<AnimationComponent> {
                nextAnimation("S", AnimationType.S)
            }
        }

        world.entity {
            add<ImageComponent> {
                image = Image().apply {
                    setSize(1f, 1f)
                    setPosition(4f, 0f)
                }
            }

            add<AnimationComponent> {
                nextAnimation("player", AnimationType.player)
            }
        }

        world.entity {
            add<ImageComponent> {
                image = Image().apply {
                    setSize(1f, 1f)
                    setPosition(5f, 0f)
                }
            }

            add<AnimationComponent> {
                nextAnimation("BP", AnimationType.BP)
            }
        }

        world.entity {
            add<ImageComponent> {
                image = Image().apply {
                    setSize(1f, 1f)
                    setPosition(6f, 0f)
                }
            }

            add<AnimationComponent> {
                nextAnimation("RP", AnimationType.RP)
            }
        }

        log.debug { "GameScreen shown" }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        log.debug { "View resized" }
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        stage.disposeSafely()
        background.disposeSafely()
        textureAtlas.disposeSafely()
        world.dispose()
        log.debug { "Resources disposed" }
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}
