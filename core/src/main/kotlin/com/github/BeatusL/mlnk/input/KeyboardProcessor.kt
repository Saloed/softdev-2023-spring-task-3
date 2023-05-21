package com.github.BeatusL.mlnk.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.DOWN
import com.badlogic.gdx.Input.Keys.LEFT
import com.badlogic.gdx.Input.Keys.RIGHT
import com.badlogic.gdx.Input.Keys.UP
import com.github.quillraven.fleks.World
import com.github.BeatusL.mlnk.component.MoveComponent
import com.github.BeatusL.mlnk.component.PlayerComponent
import com.github.quillraven.fleks.ComponentMapper
import ktx.app.KtxInputAdapter

class KeyboardProcessor(
    world: World,
    private val moveCmps: ComponentMapper<MoveComponent>
): KtxInputAdapter {
    private var sinus = 0f
    private var cosinus = 0f
    private val player = world.family(allOf = arrayOf(PlayerComponent::class))


    private fun updatePlayerMovement() {
        player.forEach { player ->
            with(moveCmps[player]) {
                sin = sinus
                cos = cosinus
            }
        }
    }

    private fun Int.isMovementKey(): Boolean {
        return this in listOf(RIGHT, LEFT, UP, DOWN)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when(keycode){
                UP -> sinus = 1f
                DOWN -> sinus = -1f
                RIGHT -> cosinus = 1f
                LEFT -> cosinus = -1f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when(keycode){
                UP -> sinus = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN -> sinus = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
                RIGHT -> cosinus = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
                LEFT -> cosinus = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        }
       return false
    }


}
