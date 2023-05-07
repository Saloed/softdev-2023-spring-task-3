package com.github.kot512.surrounded_and_hunted.controls

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.github.kot512.surrounded_and_hunted.tools.Point

class Joystick(
    baseTexture: Texture,
    knobTexture: Texture
) : Actor() {

    init {
        this.width = 250f
        this.height = 250f
    }

    private val baseRadius = this.width / 2
    private val knobRadius = baseRadius / 2

    private val basePosition = Point(0f + baseRadius, 0f + baseRadius)
    private val knobPosition = Point(0f + baseRadius + knobRadius, 0f + baseRadius + knobRadius)

    private val baseSprite = Sprite(baseTexture)
    private val knobSprite = Sprite(knobTexture)

    private var isTouched = false // "нажат" ли стик

    fun resetKnobPosition() {
        knobPosition.setPoint(
            Point(basePosition.x + knobRadius, basePosition.y + knobRadius)
        )
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            baseSprite, basePosition.x, basePosition.y,
            baseRadius * 2, baseRadius * 2
        )
        if (isTouched) {
            batch.draw(
                knobSprite, knobPosition.x, knobPosition.y,
                knobRadius * 2, knobRadius * 2
            )
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor {
        return super.hit(x, y, touchable)
    }

    inner class JoystickListener: InputListener() {
        override fun touchDown(
            event: InputEvent?,
            x: Float,
            y: Float,
            pointer: Int,
            button: Int
        ): Boolean {
            this@Joystick.isTouched = true

        }

        override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
            super.touchUp(event, x, y, pointer, button)
        }

        override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
            super.touchDragged(event, x, y, pointer)
        }
    }
}
