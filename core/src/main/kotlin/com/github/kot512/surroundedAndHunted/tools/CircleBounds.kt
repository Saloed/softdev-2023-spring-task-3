package com.github.kot512.surroundedAndHunted.tools

import kotlin.math.pow
import kotlin.math.sqrt

class CircleBounds(val center: Point, val radius: Float) {
    fun overlapsWith(entityBounds: CircleBounds): Boolean =
        sqrt(
            (center.x - entityBounds.center.x).pow(2) +
            (center.y - entityBounds.center.y).pow(2)
        ) <= radius + entityBounds.radius

    fun set(x: Float, y: Float) {
        center.set(x, y)
    }
}
