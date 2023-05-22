package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class EnemyEntity(
    screen: BaseLocationScreen,
    enemyTexture: TextureRegion,
    spawnPosition: Point,
    private val player: Player,
) : BaseEntity(screen, enemyTexture, spawnPosition) {
    override var health: Float = 100f
    override val damage: Float = 0.5f
    override val movementSpeed: Float = 150f

    var disposable: Boolean = false

    private val vectorToPlayer: Float
    get() = atan2(
        player.originBasedX - originBasedX,
        player.originBasedY - originBasedY
    )

    override fun update(delta: Float) {
        move(delta)
        attack()
    }

//    private fun checkIfHit() {
//        player.combatManager.launchedProjs.forEach { proj ->
//            if (proj.collisionBounds.overlapsWith(collisionBounds)) {
//                receiveDamage(proj.projDamage)
//                proj.disposable = true
//                println("--hit------$health")
//            }
//        }
//    }

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

    fun attack() {
        if (player.collisionBounds.overlapsWith(collisionBounds))
            player.receiveDamage(damage)
    }

    override fun receiveDamage(damage: Float) {
        health -= damage
        if (health <= 0) die()
    }

    override fun die() {
        disposable = true
    }
}
