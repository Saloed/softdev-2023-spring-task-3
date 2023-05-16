package com.github.kot512.surrounded_and_hunted.entities.enemy_manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.BALL_ENEMY_TXTR
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.RED_BALL
import com.github.kot512.surrounded_and_hunted.entities.EnemyEntity
import com.github.kot512.surrounded_and_hunted.entities.Player
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.math.random
import kotlin.math.min
import kotlin.random.Random

class EnemyManager(
    private val boundsX: ClosedFloatingPointRange<Float>,
    private val boundsY: ClosedFloatingPointRange<Float>,
    private val player: Player
) {

    enum class EnemyType {
        NORMAL, GIANT
    }

    private var launchedEnemies: MutableList<EnemyEntity> = mutableListOf()

//    количественные параметры
    private val enemiesAtOnce: Int
        get() = when(difficultyLevel) {
            1 -> 3
            2 -> 5
            3 -> 8
            4 -> 10
            5 -> 15
            else -> 40
        }
    private val amountOfEnemiesOverall: Int // количество врагов, оставшихся для уничтожения
        get() = when(difficultyLevel) {
            1 -> 20
            2 -> 50
            3 -> 100
            4 -> 180
            5 -> 300
            else -> 1000
        }
    private var enemiesLeft: Int = amountOfEnemiesOverall

    private var cooldownBetweenWaves: Float = 5f // TODO(применить)
    private var currentCooldown: Float = cooldownBetweenWaves // TODO(применить)

//    общие параметры параметры
    private var difficultyLevel: Int = 0 // уровень волны (для опр-я сложности)


//    private val giantEnemiesOverall: Int TODO()
//        get() = when(difficultyLevel) {
//            1 -> 0
//            2 -> 1
//            3 -> 3
//            4 -> 8
//            5 -> 15
//            else -> 100
//        }


    private fun createEnemies(amount: Int, type: EnemyType) {
        if (type == EnemyType.NORMAL) {
            repeat(amount) {
                launchedEnemies += EnemyEntity( //TODO(отдельный класс врага)
                    BALL_ENEMY_TXTR,
                    randomiseSpawn(),
                    player
                )
            }
        }
    }

    private fun randomiseSpawn(): Point {
        val spawnX: Float
        val spawnY: Float

        val side = Random.nextInt(1, 5)
        when (side) {
            1 -> { // у левой границы
                spawnX = boundsX.start - 200f // TODO(лучше подобрать значение)
                spawnY = boundsY.random()
            }
            2 -> { // у верхней границы
                spawnX = boundsX.random()
                spawnY = boundsY.endInclusive + 200f
            }
            3 -> { // у правой стены
                spawnX = boundsX.endInclusive + 200f // TODO(лучше подобрать значение)
                spawnY = boundsY.random()
            }
            4 -> { // у нижней границы
                spawnX = boundsX.random()
                spawnY = boundsY.start + 200f
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
        when {
            enemiesLeft == 0 && launchedEnemies.size == 0 -> {
                difficultyLevel++
                enemiesLeft = amountOfEnemiesOverall
            }

            launchedEnemies.size < enemiesAtOnce && enemiesLeft != 0-> {
                val enemiesToSpawn = min(enemiesAtOnce - launchedEnemies.size, enemiesLeft)
                createEnemies(enemiesToSpawn, EnemyType.NORMAL)
                enemiesLeft -= enemiesToSpawn
            }
        }
    }



}
