package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.kot512.surrounded_and_hunted.controls.Joystick
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.tools.Point

class Player(
    texture: Texture,
    spawnPosition: Point,
    val moveController: Joystick
) : BaseEntity(texture, spawnPosition) {
    val speed = GameScreen.LEVEL_WIDTH / 15
    val position = spawnPosition

    override fun update() {
        move()
    }

    override fun draw(batch: SpriteBatch) {
        batch.draw(sprite, width, height, position.x, position.y)
    }

    fun move() {
        val powerOfMovement = moveController.knobDeviation

        position.x += powerOfMovement.x * speed
        position.y += powerOfMovement.y * speed
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
