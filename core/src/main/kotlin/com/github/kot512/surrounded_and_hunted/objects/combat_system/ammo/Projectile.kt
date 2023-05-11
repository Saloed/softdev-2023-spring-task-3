package com.github.kot512.surrounded_and_hunted.objects.combat_system.ammo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.kot512.surrounded_and_hunted.objects.combat_system.weapons.ProjectileManager
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.ENEMY_LIST
import com.github.kot512.surrounded_and_hunted.tools.CircleBounds
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

abstract class Projectile(
    private val manager: ProjectileManager, // менеджер, к которому привязан снаряд
    private val directionAngle: Float, // направление движения в виде угла
    private val projectileSpeed: Float,
    private val projMaxDistance: Float,// макс. расстояние для снаряда, после которого тот удалится
    private val projDamage: Float,
    spawnPoint: Point, // место спавна снаряда
//    private val index: Int, // позиция в списке запущенных снарядов
    texture: Texture = Texture("graphics/test_image/joystick_knob.png") // текстура снаряда
) : Sprite(texture) {
    protected open val rotationAngle = 0f // угол для корректной отрисовки спрайта
    private var passedDistance: Float = 0f
    private var collisionBounds: CircleBounds

    val originBasedX
        get() = x + originX
    val originBasedY
        get() = y + originY

//    автоматическая настройка параметров снаряда
    private fun setup(spawnPosition: Point) {
        setSize(GameScreen.SCREEN_HEIGHT / 16, GameScreen.SCREEN_HEIGHT / 16)
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

    fun update(delta: Float) {
        checkEnemyHit()
        move(delta)
    }

    fun move(delta: Float) {
        val dX = delta * sin(directionAngle) * projectileSpeed
        val dY = delta * cos(directionAngle) * projectileSpeed
        x += dX
        y += dY

        passedDistance += sqrt(dX.pow(2) + dY.pow(2))
        if (passedDistance >= projMaxDistance) dispose()
    }

    fun dispose() {
//        texture.dispose()
//        manager.disposeProjAtIndex(index)
        manager.disposeProj(this)
    }

    fun checkEnemyHit() {
        ENEMY_LIST.forEach { enemy ->
            if (enemy.collisionBounds.overlapsWith(collisionBounds)) {
                enemy.receiveDamage(projDamage)
                dispose()
            }
        }
    }
}
