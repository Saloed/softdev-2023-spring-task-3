package com.github.BeatusL.mlnk.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.github.BeatusL.mlnk.MLNK.Companion.scale
import com.github.BeatusL.mlnk.component.MoveComponent
import com.github.BeatusL.mlnk.component.PlayerComponent
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter
import kotlin.math.sqrt

class TouchProcessor (
    world: World,
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val playerCMPS: ComponentMapper<PlayerComponent>
): KtxInputAdapter {
    private var touchX = 0f
    private var touchY = 0f
    private var hyp = 0f
    private val players = world.family(allOf = arrayOf(PlayerComponent::class))

    init {
        Gdx.input.inputProcessor = InputMultiplexer(this)
    }

    private fun updatePlayerMovement() {
        players.forEach {
            val player = playerCMPS[it]
            if (touchX != 0f && touchY != 0f) {
                with(moveCmps[it]) {
                    val deltaX = player.x
                    val deltaY = player.y
                    hyp = sqrt(deltaX * deltaX + deltaY * deltaY)
                    sin = (touchY - deltaY) / hyp
                    cos = (touchX - deltaX) / hyp
                }
            }
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touchX = screenX * scale
        touchY = screenY * scale
        updatePlayerMovement()
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touchX = 0f
        touchY = 0f
        updatePlayerMovement()
        return true
    }

}


