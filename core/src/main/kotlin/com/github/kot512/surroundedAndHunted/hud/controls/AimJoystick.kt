package com.github.kot512.surroundedAndHunted.hud.controls

import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.github.kot512.surroundedAndHunted.tools.Point
import kotlin.math.atan2

class AimJoystick(
    position: Point
) : JoystickBase(position) {
    var radAngle: Float = atan2(0f, 0f)
    var degreeAngle = -radAngle * radiansToDegrees

    init {
        println("rad: $radAngle, deg: $degreeAngle")
    }

    override fun moveKnob(touchPointX: Float, touchPointY: Float): Point {
        val editedCoords = super.moveKnob(touchPointX, touchPointY)
        changeAngle(editedCoords)

        return editedCoords
    }

    private fun changeAngle(touchPoint: Point) {
        radAngle = atan2(touchPoint.x, touchPoint.y)
        degreeAngle = -radAngle * radiansToDegrees
    }
}
