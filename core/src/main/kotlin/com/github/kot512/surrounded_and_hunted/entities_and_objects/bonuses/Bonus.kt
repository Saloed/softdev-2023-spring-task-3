package com.github.kot512.surrounded_and_hunted.entities_and_objects.bonuses

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surrounded_and_hunted.entities_and_objects.Player
import com.github.kot512.surrounded_and_hunted.tools.CircleBounds
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.random.Random

class Bonus(spawnPosition: Point, private val player: Player) : Sprite(
    TextureRegion(
        TextureRegion(CONST_AND_VAR.TEXTURE_ATLAS.findRegion("hud_joystick_knob"))
    )
) {
    var disposable: Boolean = false

    //    автоматическая настройка спрайта объекта
    private fun setup(spawnPosition: Point) {
        setSize(90f, 90f)
        setOrigin(width / 2, height / 2) // определяет центр объекта модели
        setPosition(spawnPosition.x, spawnPosition.y) // определяет позицию объекта в пространстве
        setCenter(spawnPosition.x, spawnPosition.y)
    }
    init {
        setup(spawnPosition)
    }

    private val collisionBounds =
        CircleBounds(Point(x + originX, y + originY), width / 2)

    override fun draw(batch: Batch?) {
        checkIfPickedUp()
        super.draw(batch)
    }

    private fun checkIfPickedUp() {
        if (player.collisionBounds.overlapsWith(collisionBounds)) {
            applyBuff()
            disposable = true
        }
    }

    private fun applyBuff() {
        when(Random.nextInt(1, 4)) {
            1 -> player.health = player.maxHealth
            2 -> player.gunLevel++
            3 -> player.speedLevel++
        }
    }
}
