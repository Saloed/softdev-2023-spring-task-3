package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.graphics.Texture
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class EnemyEntity(
    enemyTexture: Texture,
    spawnPosition: Point,
    val player: Player
) : BaseEntity(enemyTexture, spawnPosition) {
    override var health: Float = 100f
    override val damage: Float = 10f
    override val movementSpeed: Float = 150f

    private val vectorToPlayer: Float
    get() = atan2(
        player.originBasedX - originBasedX,
        player.originBasedY - originBasedY
    )

    override fun update(delta: Float) {
        move(delta)
    }

    override fun move(delta: Float) {
        val differenceX = player.originBasedX - originBasedX
        val differenceY = player.originBasedY - originBasedY

        if (
            differenceX != 0f && differenceY != 0f
            && !player.collisionBounds.overlapsWith(collisionBounds)
        ) {
            x += delta * sin(vectorToPlayer) * movementSpeed
            y += delta * cos(vectorToPlayer) * movementSpeed
        }

        collisionBounds.set(originBasedX, originBasedY)

    }

    override fun changeViewDirection(delta: Float) {
        TODO("Not yet implemented")
    }

    override fun attack() {
        TODO("Not yet implemented")
    }

    override fun receiveDamage(damage: Float) {
        health -= damage
    }

    override fun die() {
        TODO("Not yet implemented")
    }
}
