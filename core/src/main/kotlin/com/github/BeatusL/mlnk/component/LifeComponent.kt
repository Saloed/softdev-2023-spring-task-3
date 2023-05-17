package com.github.BeatusL.mlnk.component

data class LifeComponent(
    var life: Float = 3f,
    var max: Float = 3f,
    var takeDamage: Float = 0f,
) {
    val isDead: Boolean
        get() = life <= 0f
}
