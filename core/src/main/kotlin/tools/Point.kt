package tools

class Point2D(private var x: Float, private var y: Float) {

    fun add(x: Float, y: Float) {
        this.x += x
        this.y += y
    }

    fun getX() = this.x
    fun getY() = this.y

}
