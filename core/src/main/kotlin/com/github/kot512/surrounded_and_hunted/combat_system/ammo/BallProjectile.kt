package com.github.kot512.surrounded_and_hunted.combat_system.ammo

import com.github.kot512.surrounded_and_hunted.SurroundedAndHunted.Companion.JOYSTICK_KNOB_TXTR
import com.github.kot512.surrounded_and_hunted.screen.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point

class BallProjectile(
    screen: BaseLocationScreen,
    directionAngle: Float,
    projSpeed: Float,
    projMaxDistance: Float,
    projDamage: Float,
    spawnPoint: Point,
) : Projectile(
    screen,
    directionAngle,
    projSpeed,
    projMaxDistance,
    projDamage,
    spawnPoint,
    JOYSTICK_KNOB_TXTR
) {
    override val rotationAngle: Float = 0f
}
