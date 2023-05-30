package com.github.kot512.surroundedAndHunted.entities_and_objects.enemies

import com.github.kot512.surroundedAndHunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surroundedAndHunted.entities_and_objects.Player
import com.github.kot512.surroundedAndHunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surroundedAndHunted.tools.Point

class GiantEnemy (
    screen: BaseLocationScreen,
    spawnPosition: Point,
    player: Player
) : EnemyEntity(
    screen, CONST_AND_VAR.GIANT_ENEMY_TXTR,
    spawnPosition, player,
    500f, 500f
) {
    override var health: Float = 300f
    override val damage: Float = 5f
    override val movementSpeed: Float = 50f
    override var scoreReward: Int = 10

    override fun changeViewDirection(delta: Float) {
        TODO()
    }
}
