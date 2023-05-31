package com.github.BeatusL.mlnk.component

enum class AttackState {
    DEALING_DMG, //PREPARE unused
}

data class AttackComponent(
    var doAttack: Boolean = false,
    var state: AttackState = AttackState.DEALING_DMG, // unused as well
    var damage: Int = 1,
    var delay: Float = 0f,
    var friendly: Boolean = false,
    var powerUp: Boolean = false
)
