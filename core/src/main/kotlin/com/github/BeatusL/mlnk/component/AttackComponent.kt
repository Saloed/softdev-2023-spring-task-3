package com.github.BeatusL.mlnk.component

enum class AttackState {
    ATTACK, PREPARE
}

data class AttackComponent(
    var doAttack: Boolean = false,
    var state: AttackState = AttackState.PREPARE,
    var damage: Int = 0,
    var delay: Float = 0f,
)
