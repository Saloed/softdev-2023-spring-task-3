package com.github.kot512.surrounded_and_hunted.objects.combat_system.ammo

import com.github.kot512.surrounded_and_hunted.objects.combat_system.weapons.ProjectileManager
import com.github.kot512.surrounded_and_hunted.screen.GameScreen
import com.github.kot512.surrounded_and_hunted.screen.GameScreen.Companion.BALL_PROJ_TEXT
import com.github.kot512.surrounded_and_hunted.tools.Point

class BallProjectile(
    manager: ProjectileManager,
    directionAngle: Float,
    projSpeed: Float,
    projMaxDistance: Float,
    projDamage: Float,
//    index: Int,
    spawnPoint: Point,
) : Projectile(
    manager,
    directionAngle,
    projSpeed,
    projMaxDistance,
    projDamage,
    spawnPoint,
//    index,
    GameScreen.BALL_PROJ_TEXT) {
    override val rotationAngle: Float = 0f
}
