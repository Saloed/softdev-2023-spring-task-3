package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.BOUNDS_X
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.BOUNDS_Y
import com.github.kot512.surrounded_and_hunted.tools.CircleBounds
import com.github.kot512.surrounded_and_hunted.tools.Point

abstract class BaseEntity(
    entityTexture: Texture,
    spawnPosition: Point,
    collisionCoeff: Float = 1f // коэффициент рамера коллизии (0..1)
    ) : Sprite(entityTexture) {
    protected abstract var health: Float
    protected abstract val damage: Float
    protected abstract val movementSpeed: Float

    protected var velocity: Point = Point(0f, 0f)
    protected var viewAngle = 0f

    val originBasedX
        get() = x + originX
    val originBasedY
        get() = y + originY

    //    автоматическая настройка спрайта сущности
    fun setup(spawnPosition: Point) {
        setSize(GameScreen.SCREEN_HEIGHT / 8, GameScreen.SCREEN_HEIGHT / 8)
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
        newX in BOUNDS_X && newX + width in BOUNDS_X
    protected fun allowedToMoveY(newY: Float) =
        newY in BOUNDS_Y && newY + width in BOUNDS_Y

    override fun draw(batch: Batch) {
        update(Gdx.graphics.deltaTime)
        super.draw(batch)
    }

    abstract fun update(delta: Float)

    //    геймплейные функции сущности
    protected abstract fun move(delta: Float)
    protected abstract fun changeViewDirection(delta: Float)
    protected abstract fun attack()
    abstract fun receiveDamage(damage: Float)
    protected abstract fun die()
}

