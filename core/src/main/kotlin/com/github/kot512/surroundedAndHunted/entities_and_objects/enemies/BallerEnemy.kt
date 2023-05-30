package com.github.kot512.surroundedAndHunted.entities_and_objects.enemies

import com.github.kot512.surroundedAndHunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surroundedAndHunted.entities_and_objects.Player
import com.github.kot512.surroundedAndHunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surroundedAndHunted.tools.Point

class BallerEnemy(
    screen: BaseLocationScreen,
    spawnPosition: Point,
    player: Player
) : EnemyEntity(
    screen, CONST_AND_VAR.BALLER_ENEMY_TXTR,
    spawnPosition, player,
    160f, 160f
) {
    override var health: Float = 100f
    override val damage: Float = 0.5f
    override val movementSpeed: Float = 150f
    override var scoreReward: Int = 1

    override fun changeViewDirection(delta: Float) {
        TODO("Not yet implemented")
    }
}
