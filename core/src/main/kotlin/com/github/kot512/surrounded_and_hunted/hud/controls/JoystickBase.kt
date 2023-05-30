package com.github.kot512.surrounded_and_hunted.hud.controls

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.CONST_AND_VAR
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.math.pow
import kotlin.math.sqrt

abstract class JoystickBase(
    position: Point
) : Actor() {
    private val listener = JoystickListener()

    private fun setup(listener: JoystickListener, position: Point) {
        setPosition(position.x, position.y)
        this.width = 350f
        this.height = 350f
        addListener(listener)
    }

    init {
        setup(listener, position)
    }

    protected val baseRadius = this.width / 2
    private val knobRadius = baseRadius / 2

    private val basePosition = Point(this.x, this.y)
    private val knobPosition = Point(
        basePosition.x + 2 * knobRadius - baseRadius,
        basePosition.y + 2 * knobRadius - baseRadius
    )

    private val baseSprite = Sprite(CONST_AND_VAR.JOYSTICK_BASE_TXTR)
    private val knobSprite = Sprite(CONST_AND_VAR.JOYSTICK_KNOB_TXTR)

    var isTouched = false // "нажат" ли стик

    open fun resetKnobPosition() {
        knobPosition.setPoint(
            Point(
                basePosition.x + 2 * knobRadius - baseRadius,
                basePosition.y + 2 * knobRadius - baseRadius
            )
        )
    }

    open fun moveKnob(touchPointX: Float, touchPointY: Float): Point {
        var tpX = touchPointX - baseRadius
        var tpY = touchPointY - baseRadius

        val deviation = sqrt(tpX.pow(2) + tpY.pow(2))
        if (deviation > baseRadius) {
            val coeff = baseRadius / deviation
            tpX *= coeff
            tpY *= coeff
        }
        knobPosition.setPoint(Point(tpX + basePosition.x, tpY + basePosition.y))

        return Point(tpX, tpY)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            baseSprite, basePosition.x, basePosition.y,
            baseRadius * 2, baseRadius * 2
        )
        if (isTouched) {
            batch.draw(
                knobSprite,
                knobPosition.x - knobRadius + baseRadius,
                knobPosition.y + baseRadius - knobRadius,
                knobRadius * 2, knobRadius * 2
            )
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor? { // срабатывает при нажатии на актера
//        форма актера - квадрат, сначала проверяем, задели ли его
        if (super.hit(x, y, touchable) == null) return null

        val relToCenterX = x - baseRadius
        val relToCenterY = y - baseRadius

        val touched =
            sqrt(relToCenterX.pow(2) + relToCenterY.pow(2)) <= baseRadius
        return if (!touched) null else this


    }

    //    слушатель действий джойстика
    inner class JoystickListener: InputListener() {

        override fun touchDown(
            event: InputEvent?,
            x: Float,
            y: Float,
            pointer: Int,
            button: Int
        ): Boolean {
            this@JoystickBase.isTouched = true
            moveKnob(x, y)

            return true
        }

        override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
            this@JoystickBase.isTouched = false
            resetKnobPosition()
        }

        override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
            moveKnob(x, y)

        }
    }
}
