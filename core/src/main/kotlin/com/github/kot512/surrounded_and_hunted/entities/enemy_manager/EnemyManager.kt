package com.github.kot512.surrounded_and_hunted.entities.enemy_manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.kot512.surrounded_and_hunted.entities.enemies.BallerEnemy
import com.github.kot512.surrounded_and_hunted.entities.enemies.EnemyEntity
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.entities.enemies.GiantEnemy
import com.github.kot512.surrounded_and_hunted.entities.enemies.TriangleEnemy
import com.github.kot512.surrounded_and_hunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.math.random
import kotlin.math.pow
import kotlin.random.Random

class EnemyManager(
    private val screen: BaseLocationScreen,
    private val player: Player
) {

    enum class EnemyType {
        NORMAL, FAST, GIANT
    }

    val launchedEnemies: MutableList<EnemyEntity> = mutableListOf()
    var survivedTime: Float = 0f

//    количественные параметры
    private val enemiesAtOnce: Int
        get() = (1.5 * survivedTime).toInt()

    private val giantEnemiesPercentage: Float
        get() = if (1.004f.pow(survivedTime) - 1 >= 1) 1f
                else 1.004f.pow(survivedTime) - 1
    private val fastEnemiesPercentage: Float
        get() = if (1.0085f.pow(survivedTime) - 1 + giantEnemiesPercentage >= 1)
                    1 - giantEnemiesPercentage
                else 1.0085f.pow(survivedTime) - 1
    private val basicEnemiesPercentage: Float
        get() = if (1 - fastEnemiesPercentage - giantEnemiesPercentage <= 0)
                    0f
                else 1 - fastEnemiesPercentage - giantEnemiesPercentage


    fun draw(batch: Batch) {
        update(Gdx.graphics.deltaTime)

        launchedEnemies.removeIf { it.disposable }
        for (enemy in launchedEnemies)
            enemy.draw(batch)
    }

    private fun update(delta: Float) {
        survivedTime += delta
        spawnEnemies()
    }

    private fun spawnEnemies() {
        if (launchedEnemies.size < enemiesAtOnce) {
            val enemiesToSpawn = enemiesAtOnce - launchedEnemies.size
            createEnemies((enemiesToSpawn * basicEnemiesPercentage).toInt(), EnemyType.NORMAL)
            createEnemies((enemiesToSpawn * fastEnemiesPercentage).toInt(), EnemyType.FAST)
            createEnemies((enemiesToSpawn * giantEnemiesPercentage).toInt(), EnemyType.GIANT)

            println("$basicEnemiesPercentage - $fastEnemiesPercentage - $giantEnemiesPercentage")
        }
    }

    private fun createEnemies(amount: Int, type: EnemyType) {
        repeat(amount) {
            launchedEnemies +=
                when (type) {
                    EnemyType.NORMAL -> BallerEnemy(
                        screen,
                        randomiseSpawn(),
                        player
                    )
                    EnemyType.FAST -> TriangleEnemy(
                        screen,
                        randomiseSpawn(),
                        player
                    )
                    EnemyType.GIANT -> GiantEnemy(
                        screen,
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
}
