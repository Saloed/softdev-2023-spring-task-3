package com.github.BeatusL.mlnk.component

enum class AttackState {
    DEALING_DMG, PREPARE
}

data class AttackComponent(
    var doAttack: Boolean = false,
    var state: AttackState = AttackState.DEALING_DMG, // not useful yet
    var damage: Int = 1,
    var delay: Float = 0f,
    var friendly:Boolean = false
)
