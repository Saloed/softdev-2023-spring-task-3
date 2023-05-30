package com.github.kot512.surroundedAndHunted.hud.controls

import com.github.kot512.surroundedAndHunted.tools.Point
import com.github.kot512.surroundedAndHunted.tools.setPoint

class MovementJoystick(
    position: Point
) : JoystickBase(position) {
    val knobDeviation = Point(0f, 0f)

    override fun resetKnobPosition() {
        super.resetKnobPosition()
        knobDeviation.setPoint(Point(0f, 0f))
    }

    override fun moveKnob(touchPointX: Float, touchPointY: Float): Point {
        val editedCoords = super.moveKnob(touchPointX, touchPointY)
        changeDeviation(editedCoords)

        return editedCoords
    }

    private fun changeDeviation(touchPoint: Point) {
        knobDeviation.setPoint(
            Point(touchPoint.x / baseRadius, touchPoint.y / baseRadius)
        )
    }
}
