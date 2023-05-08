package com.github.kot512.surrounded_and_hunted.controls

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import kotlin.math.pow
import kotlin.math.sqrt

class JoystickBase(
    baseTexture: Texture,
    knobTexture: Texture,
    position: Point
) : Actor() {

    init {
        setPosition(position.x, position.y)
        this.width = GameScreen.SCREEN_HEIGHT / 2.8f
        this.height = GameScreen.SCREEN_HEIGHT / 2.8f
        addListener(JoystickListener())
    }

    private val baseRadius = this.width / 2
    private val knobRadius = baseRadius / 2

    private val basePosition = Point(this.x, this.y)
    private val knobPosition = Point(
        basePosition.x + 2 * knobRadius - baseRadius,
        basePosition.y + 2 * knobRadius - baseRadius
    )

    private val baseSprite = Sprite(baseTexture)
    private val knobSprite = Sprite(knobTexture)

    private var isTouched = false // "нажат" ли стик
    val knobDeviation = Point(0f, 0f)

    fun resetKnobPosition() {
        knobPosition.setPoint(
            Point(
                basePosition.x + 2 * knobRadius - baseRadius,
                basePosition.y + 2 * knobRadius - baseRadius
            )
        )
        knobDeviation.setPoint(Point(0f, 0f))
    }

    fun moveKnob(touchPointX: Float, touchPointY: Float) {
        var tpX = touchPointX - baseRadius
        var tpY = touchPointY - baseRadius

        val deviation = sqrt(tpX.pow(2) + tpY.pow(2))
        if (deviation > baseRadius) {
            val coeff = baseRadius / deviation
            tpX *= coeff
            tpY *= coeff
        }
        knobDeviation.setPoint(Point(tpX / baseRadius, tpY / baseRadius))
        knobPosition.setPoint(Point(tpX + basePosition.x, tpY + basePosition.y))
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

    abstract inner class JoystickListener: InputListener() {

        abstract override fun touchDown(
            event: InputEvent?,
            x: Float,
            y: Float,
            pointer: Int,
            button: Int
        ): Boolean

        abstract override fun touchUp(
            event: InputEvent?,
            x: Float,
            y: Float,
            pointer: Int,
            button: Int
        )

        abstract override fun touchDragged(
            event: InputEvent?,
            x: Float,
            y: Float,
            pointer: Int
        )
    }
}
