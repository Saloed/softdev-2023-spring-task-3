package com.github.kot512.surrounded_and_hunted.entities_and_objects.enemies

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surrounded_and_hunted.entities_and_objects.BaseEntity
import com.github.kot512.surrounded_and_hunted.entities_and_objects.Player
import com.github.kot512.surrounded_and_hunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


abstract class EnemyEntity(
    screen: BaseLocationScreen,
    enemyTexture: TextureRegion,
    spawnPosition: Point,
    private val player: Player,
    spriteWidth: Float,
    spriteHeight: Float
) : BaseEntity(screen, enemyTexture, spawnPosition, spriteWidth, spriteHeight) {
    var disposable: Boolean = false
    private val vectorToPlayer: Float
        get() = atan2(
            player.originBasedX - originBasedX,
            player.originBasedY - originBasedY
        )

    abstract var scoreReward: Int
    protected abstract val damage: Float

    override fun update(delta: Float) {
        move(delta)
        attack()
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

    private fun attack() {
        if (player.collisionBounds.overlapsWith(collisionBounds))
            player.receiveDamage(damage)
    }

    override fun receiveDamage(damage: Float) {
        health -= damage
        if (health <= 0) die()
    }

    override fun die() {
        disposable = true
        CONST_AND_VAR.CURRENT_SCORE += scoreReward
    }
}
