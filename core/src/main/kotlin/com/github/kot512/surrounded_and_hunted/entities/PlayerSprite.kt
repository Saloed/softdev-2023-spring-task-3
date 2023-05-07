package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.github.kot512.surrounded_and_hunted.controls.Joystick
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.LEVEL_HEIGHT
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.LEVEL_WIDTH
import com.github.kot512.surrounded_and_hunted.tools.Point

class PlayerSprite(characterTexture: Texture, private val movementController: Joystick) : Sprite(characterTexture) {
    private var movementSpeed = GameScreen.LEVEL_WIDTH / 10
    private val velocity = Point(0f, 0f)

    init {
        setPosition(LEVEL_WIDTH / 2, LEVEL_HEIGHT / 2)
    }

    override fun draw(batch: Batch) {
        update(Gdx.graphics.deltaTime)
        super.draw(batch)
    }

    fun update(delta: Float) {
        val powerOutput = movementController.knobDeviation
        velocity.x = movementSpeed * powerOutput.x
        velocity.y = movementSpeed * powerOutput.y

        if (velocity.x > movementSpeed) velocity.x = movementSpeed
        if (velocity.x < -movementSpeed) velocity.x = -movementSpeed
        if (velocity.y > movementSpeed) velocity.y = movementSpeed
        if (velocity.y < -movementSpeed) velocity.y = -movementSpeed

        x += velocity.x * delta
        y += velocity.y * delta
        setPosition(x, y)
    }
}
