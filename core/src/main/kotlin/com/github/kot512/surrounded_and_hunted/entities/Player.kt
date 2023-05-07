package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.kot512.surrounded_and_hunted.controls.Joystick
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.math.abs

class Player(
    texture: Texture,
    spawnPosition: Point,
    val moveController: Joystick
) : BaseEntity(texture, spawnPosition) {
    private val speed = GameScreen.LEVEL_WIDTH / 15
    private val position = spawnPosition
    init {
        sprite.setBounds(position.x, position.y, width, height)
    }

    override fun update() {
        move()
    }

    override fun draw(batch: SpriteBatch) {
        sprite.setPosition(position.x, position.y)
        batch.draw(sprite, width, height, position.x, position.y)
    }

    fun move() {
        val velocityInput = moveController.knobDeviation

        val velocityX =
            if (abs(velocityInput.x) < 0.05) 0f else velocityInput.x
        val velocityY =
            if (abs(velocityInput.y) < 0.05) 0f else velocityInput.y

        position.x += velocityX * speed
        position.y += velocityY * speed
    }

    override fun dealDamage() {
        TODO("Not yet implemented")
    }

    override fun receiveDamage() {
        TODO("Not yet implemented")
    }

    override fun die() {
        TODO("Not yet implemented")
    }

}
