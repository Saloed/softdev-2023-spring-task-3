package com.github.kot512.surrounded_and_hunted.tools

import com.badlogic.gdx.math.Vector2

data class Point(var x: Float, var y: Float): Vector2(x, y) {

    fun setPoint(point: Point) {
        this.x = point.x
        this.y = point.y
    }
}
