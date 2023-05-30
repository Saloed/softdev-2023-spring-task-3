package com.github.kot512.surroundedAndHunted.tools

import com.badlogic.gdx.math.Vector2

typealias Point = Vector2
fun Point.setPoint(point: Point) {
    this.x = point.x
    this.y = point.y
}
