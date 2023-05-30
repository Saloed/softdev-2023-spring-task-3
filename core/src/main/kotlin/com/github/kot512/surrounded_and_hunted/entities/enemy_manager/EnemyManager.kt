package com.github.kot512.surrounded_and_hunted.entities.enemy_manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.BASIC_ENEMY_TXTR
import com.github.kot512.surrounded_and_hunted.entities.EnemyEntity
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.screen.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.math.random
import kotlin.random.Random

class EnemyManager(
    private val screen: BaseLocationScreen,
    private val player: Player
) {

    enum class EnemyType {
        NORMAL, GIANT
    }

    var launchedEnemies: MutableList<EnemyEntity> = mutableListOf()
    var survivedTime: Float = 0f

//    количественные параметры
    private val enemiesAtOnce: Int
        get() = (1.2 * survivedTime).toInt()

    private fun createEnemies(amount: Int, type: EnemyType) {
        if (type == EnemyType.NORMAL) {
            repeat(amount) {
                launchedEnemies += EnemyEntity( //TODO(отдельный класс врага)
                    screen,
                    BASIC_ENEMY_TXTR,
                    randomiseSpawn(),
                    player
                )
            }
        }
    }

    private fun randomiseSpawn(): Point {
        val spawnX: Float
        val spawnY: Float

        when (Random.nextInt(1, 5)) {
            1 -> { // у левой границы
                spawnX = 0f - 500f // TODO(лучше подобрать значение)
                spawnY = (0f..screen.locationHeight).random()
            }
            2 -> { // у верхней границы
                spawnX = (0f..screen.locationWidth).random()
                spawnY = screen.locationHeight + 500f
            }
            3 -> { // у правой стены
                spawnX = screen.locationHeight + 500f // TODO(лучше подобрать значение)
                spawnY = (0f..screen.locationHeight).random()
            }
            4 -> { // у нижней границы
                spawnX = (0f..screen.locationWidth).random()
                spawnY = 0f - 500f
            }
            else -> {
                spawnX = 0f
                spawnY = 0f
            }
        }

        return Point(spawnX, spawnY)
    }

    fun draw(batch: Batch) {
        update(Gdx.graphics.deltaTime)

        launchedEnemies = launchedEnemies.filter { !it.disposable }.toMutableList()
        for (enemy in launchedEnemies)
            enemy.draw(batch)
    }

    private fun update(delta: Float) {
//        when {
//            enemiesLeft == 0 && launchedEnemies.size == 0 -> {
//                difficultyLevel++
//                enemiesLeft = amountOfEnemiesOverall
//            }
//
//            launchedEnemies.size < enemiesAtOnce && enemiesLeft != 0-> {
//                val enemiesToSpawn = min(enemiesAtOnce - launchedEnemies.size, enemiesLeft)
//                createEnemies(enemiesToSpawn, EnemyType.NORMAL)
//                enemiesLeft -= enemiesToSpawn
//            }
//        }
        survivedTime += delta

        if (launchedEnemies.size < enemiesAtOnce) {
            val enemiesToSpawn = enemiesAtOnce - launchedEnemies.size
            createEnemies(enemiesToSpawn, EnemyType.NORMAL) // TODO(добавить гигантов)
        }
    }



}
