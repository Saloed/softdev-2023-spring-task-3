package com.github.kot512.surrounded_and_hunted.combat_system.ammo

import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.JOYSTICK_KNOB_TXTR
import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.RED_BALL
import com.github.kot512.surrounded_and_hunted.combat_system.weapons.ProjectileManager
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.tools.Point

class BallProjectile(
    directionAngle: Float,
    projSpeed: Float,
    projMaxDistance: Float,
    projDamage: Float,
    spawnPoint: Point,
) : Projectile(
    directionAngle,
    projSpeed,
    projMaxDistance,
    projDamage,
    spawnPoint,
    JOYSTICK_KNOB_TXTR
) {
    override val rotationAngle: Float = 0f
}
