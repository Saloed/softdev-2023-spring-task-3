package com.github.BeatusL.mlnk.component

import com.github.BeatusL.mlnk.system.EntityType

data class LifeComponent(
    var life: Float = 1f,
    var max: Float = 3f,
    var takeDamage: Float = 0f,
    var lType: EntityType = EntityType.Default
) {
    val isDead: Boolean
        get() = life <= 0f
}
