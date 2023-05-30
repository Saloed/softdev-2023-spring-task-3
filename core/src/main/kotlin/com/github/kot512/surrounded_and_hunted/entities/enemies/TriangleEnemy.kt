package com.github.kot512.surrounded_and_hunted.entities.enemies

import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.TRIANGLE_ENEMY_TXTR
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.entities.enemy_manager.EnemyManager
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point

class TriangleEnemy(
    screen: BaseLocationScreen,
    spawnPosition: Point,
    player: Player
) : EnemyEntity(
    screen, TRIANGLE_ENEMY_TXTR,
    spawnPosition, player,
    150f, 150f
) {
    override var health: Float = 100f
    override val damage: Float = 1f
    override val movementSpeed: Float = 350f
    override var scoreReward: Int = 2

    override fun changeViewDirection(delta: Float) {
        TODO()
    }
}
