package com.github.kot512.surrounded_and_hunted.entities.tools

data class Point(var x: Float, var y: Float) {

    fun setPoint(point: Point) {
        this.x = point.x
        this.y = point.y
    }
}
