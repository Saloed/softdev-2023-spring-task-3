package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.kot512.surrounded_and_hunted.controls.Joystick
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.PLAYER_POS
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.SCREEN_HEIGHT
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.SCREEN_WIDTH
import com.github.kot512.surrounded_and_hunted.tools.Point

class PlayerSprite(characterTexture: Texture, private val movementController: Joystick)
    : Sprite(characterTexture) {
    private var movementSpeed = 500f
    private val velocity = Point(0f, 0f)

    init {
        setPosition(PLAYER_POS.x, PLAYER_POS.y)
        setSize(SCREEN_HEIGHT / 5, SCREEN_HEIGHT / 5)
    }

    val centerX: Float
    get() = x + width / 2

    val centerY: Float
    get() = y + height / 2


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
