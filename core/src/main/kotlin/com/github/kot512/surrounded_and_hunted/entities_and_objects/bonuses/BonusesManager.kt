package com.github.kot512.surrounded_and_hunted.entities_and_objects.bonuses

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surrounded_and_hunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.random.Random

class BonusesManager(private val screen: BaseLocationScreen) {
    private val launchedBonuses: MutableList<Bonus> = mutableListOf()
    private val bonusesAmount: Int
        get() = CONST_AND_VAR.CURRENT_SCORE / 50

    fun draw(batch: SpriteBatch) {
        update()
        launchedBonuses.forEach { it.draw(batch) }
    }

    private fun update() {
        launchedBonuses.removeIf { it.disposable }
        spawnBonuses()
    }

    private fun spawnBonuses() {
        val bonusesToSpawn = bonusesAmount - launchedBonuses.size
        if (bonusesToSpawn > 0) {
            createBonuses(bonusesToSpawn)
        }
    }

    private fun createBonuses(amount: Int) {
        repeat(amount) {
            launchedBonuses += Bonus(randomiseSpawn(), screen.player)
        }
    }

    private fun randomiseSpawn(): Point {
        return Point(
            Random.nextInt(screen.locationWidth.toInt()).toFloat(),
            Random.nextInt(screen.locationHeight.toInt()).toFloat()
        )
    }


}
