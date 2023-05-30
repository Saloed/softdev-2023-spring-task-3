package com.github.kot512.surrounded_and_hunted.combat.ammo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surrounded_and_hunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.CircleBounds
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

abstract class Projectile(
    private val screen: BaseLocationScreen,     // менеджер, к которому привязан снаряд
    private val directionAngle: Float, // направление движения в виде угла
    private val projectileSpeed: Float,
    private val projMaxDistance: Float,// макс. расстояние для снаряда, после которого тот удалится
    val projDamage: Float,
    spawnPoint: Point, // место спавна снаряда
    texture: TextureRegion = CONST_AND_VAR.PROJECTILE_BASE_TXTR // текстура снаряда
) : Sprite(texture) {
    protected open val rotationAngle = 0f // угол для корректной отрисовки спрайта
    private var passedDistance: Float = 0f
    var collisionBounds: CircleBounds

    var disposable: Boolean = false

    private val originBasedX
        get() = x + originX
    private val originBasedY
        get() = y + originY

//    автоматическая настройка параметров снаряда
    private fun setup(spawnPosition: Point) {
        setSize(60f, 60f)
        setOrigin(width / 2, height / 2) // определяет центр объекта модели
        setPosition(spawnPosition.x, spawnPosition.y) // определяет позицию объекта в пространстве
        setCenter(spawnPosition.x, spawnPosition.y)
        rotation = rotationAngle
    }

    init {
        setup(spawnPoint)
        collisionBounds =
            CircleBounds(Point(originBasedX, originBasedY), width / 2)
    }

//    базовые функции
    override fun draw(batch: Batch) {
        update(Gdx.graphics.deltaTime)
        super.draw(batch)
    }

    private fun update(delta: Float) {
        checkEnemyHit()
        move(delta)
    }

    private fun move(delta: Float) {
        val dX = - delta * projectileSpeed * sin(directionAngle / 60)
        val dY = delta * projectileSpeed * cos(directionAngle / 60)
        x += dX
        y += dY

        collisionBounds.set(originBasedX, originBasedY)
        passedDistance += sqrt(dX.pow(2) + dY.pow(2))

//        проверка, пора ли уничтожать снаряд
        if (
            passedDistance >= projMaxDistance ||
            originBasedX !in (0f..screen.locationWidth) ||
            originBasedY !in (0f..screen.locationHeight)
        )
            disposable = true
    }

//    private fun dispose() {
////        texture.dispose()
//        manager.disposeProjAtIndex(index)
//    }

    private fun checkEnemyHit() {
        screen.enemyManager.launchedEnemies.forEach { enemy ->
            if (enemy.collisionBounds.overlapsWith(collisionBounds)) {
                enemy.receiveDamage(projDamage)
                disposable = true
                println("hit___")
            }
        }
    }
}
