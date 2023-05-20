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
    private val moveCmp: ComponentMapper<MoveComponent>
): KtxInputAdapter {
    private var touchX = 0f
    private var touchY = 0f
    private var hyp = 0f
    private val player = world.family(allOf = arrayOf(PlayerComponent::class))

    init {
        Gdx.input.inputProcessor = InputMultiplexer(this)
    }

    private fun updatePlayerMovement() {
        player.forEach { player ->
            if (touchX != 0f && touchY != 0f)
            with(moveCmp[player]) {
                hyp = sqrt(touchX * touchX + touchY * touchY)
                sin = touchY / hyp
                cos = touchX / hyp
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


