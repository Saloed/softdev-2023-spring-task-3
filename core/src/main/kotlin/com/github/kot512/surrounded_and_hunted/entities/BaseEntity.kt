package com.github.kot512.surrounded_and_hunted.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import java.text.FieldPosition

abstract class BaseEntity(
    texture: Texture,
    protected val spawnPosition: Point = Point(0f, 0f),
    ) {
//    обязательные свойства
    protected var width: Float = GameScreen.LEVEL_HEIGHT / 4
    protected var height: Float = GameScreen.LEVEL_HEIGHT / 4
    val sprite = Sprite(texture)
//    базовые характеристики сущности
    protected var velocityX = 0f
    protected var velocityY = 0f

    protected var health = 100

//    обязательные функции
open fun draw(batch: SpriteBatch) {
        batch.draw(sprite, spawnPosition.x, spawnPosition.y, width, height)
    }

    abstract fun update()

    abstract fun dealDamage()

    abstract fun receiveDamage()

    abstract fun die()
}
