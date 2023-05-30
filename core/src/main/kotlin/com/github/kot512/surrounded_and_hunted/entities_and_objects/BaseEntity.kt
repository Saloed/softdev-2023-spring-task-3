package com.github.kot512.surrounded_and_hunted.entities_and_objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.kot512.surrounded_and_hunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.CircleBounds
import com.github.kot512.surrounded_and_hunted.tools.Point

abstract class BaseEntity(
    val screen: BaseLocationScreen,
    entityTexture: TextureRegion,
    spawnPosition: Point,
    private val spriteWidth: Float,
    private val spriteHeight: Float,
    collisionCoeff: Float = 1f, // коэффициент рамера коллизии (0..1)
    ) : Sprite(entityTexture) {
    abstract var health: Float
    protected abstract val movementSpeed: Float

    protected var velocity: Point = Point(0f, 0f)
    protected var viewAngle = 0f

    val originBasedX
        get() = x + originX
    val originBasedY
        get() = y + originY

    //    автоматическая настройка спрайта сущности
    private fun setup(spawnPosition: Point) {
        setSize(spriteWidth, spriteHeight)
        setOrigin(width / 2, height / 2) // определяет центр объекта модели
        setPosition(spawnPosition.x, spawnPosition.y) // определяет позицию объекта в пространстве
        setCenter(spawnPosition.x, spawnPosition.y)
        rotation = viewAngle
    }

    init {
        setup(spawnPosition)
    }

    val collisionBounds =
        CircleBounds(Point(originBasedX, originBasedY), width / 2 * collisionCoeff)

    //    базовые функции спрайта-сущности
    protected fun allowedToMoveX(newX: Float): Boolean =
        newX in (0f..screen.locationWidth) && newX + width in (0f..screen.locationWidth)
    protected fun allowedToMoveY(newY: Float) =
        newY in (0f..screen.locationHeight) && newY + width in (0f..screen.locationHeight)

    override fun draw(batch: Batch) {
        update(Gdx.graphics.deltaTime)
        super.draw(batch)
    }

    abstract fun update(delta: Float)

    //    геймплейные функции сущности
    protected abstract fun move(delta: Float)
    protected abstract fun changeViewDirection(delta: Float)
    abstract fun receiveDamage(damage: Float)
    protected abstract fun die()
}

