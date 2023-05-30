package com.github.kot512.surrounded_and_hunted.tools

import com.badlogic.gdx.math.Vector2

open class Point(x: Float, y: Float): Vector2(x, y) {
    var x: Float
        get() = super.x
        set(value: Float) {
            super.x = value
            value
        }
    var y: Float
        get() = super.y
        set(value: Float) {
            super.y = value
            value
        }

    fun setPoint(point: Point) {
        super.x = point.x
        super.y = point.y
    }
}
