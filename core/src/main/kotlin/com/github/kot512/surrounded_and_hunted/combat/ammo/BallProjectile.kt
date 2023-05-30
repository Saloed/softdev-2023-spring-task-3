package com.github.kot512.surrounded_and_hunted.combat.ammo

import com.github.kot512.surrounded_and_hunted.screens.playable_screens.BaseLocationScreen
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
    spawnPoint
) {
    override val rotationAngle: Float = 0f
}
