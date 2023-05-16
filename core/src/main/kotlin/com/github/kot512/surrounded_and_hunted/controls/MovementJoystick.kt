package com.github.kot512.surrounded_and_hunted.controls

import com.badlogic.gdx.graphics.Texture
import com.github.kot512.surrounded_and_hunted.tools.Point

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
