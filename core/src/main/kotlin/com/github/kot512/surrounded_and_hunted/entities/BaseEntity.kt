package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.kot512.surrounded_and_hunted.tools.Point

abstract class BaseEntity(
    texture: Texture,
    protected var width: Float,
    protected var height: Float,
    protected val position: Point = Point(0f, 0f),
    ) {
//    обязательные свойства
    val sprite = Sprite(texture)

//    базовые характеристики сущности
    protected var velocityX = 0f
    protected var velocityY = 0f

    protected var health = 100

//    обязательные функции
    fun draw(batch: SpriteBatch) {
        batch.draw(sprite, position.x, position.y, width, height)
    }

    abstract fun update()

    abstract fun dealDamage()

    abstract fun receiveDamage()

    abstract fun die()
}
