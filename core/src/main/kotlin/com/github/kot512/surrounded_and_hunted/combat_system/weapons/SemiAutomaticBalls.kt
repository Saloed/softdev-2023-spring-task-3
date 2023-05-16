package com.github.kot512.surrounded_and_hunted.combat_system.weapons

import com.github.kot512.surrounded_and_hunted.combat_system.ammo.BallProjectile
import com.github.kot512.surrounded_and_hunted.combat_system.ammo.Projectile
import com.github.kot512.surrounded_and_hunted.tools.Point

class SemiAutomaticBalls(directionAngle: Float, shootPoint: Point)
    : ProjectileManager(directionAngle, shootPoint) {
    override val cooldown: Float = 0.5f

    override var projectileSpeed: Float = 300f
    override var projMaxDistance: Float = 300f
    override var projDamage: Float = 10f

    override val projInShot: Int = 1
    override val angleBetweenShots: Float = 0f
    override val cooldownBetweenProjs: Float = 0f

    override fun createProj(): Projectile =
        BallProjectile(
            directionAngle, projectileSpeed,
            projMaxDistance, projDamage, shootPoint
        )
}
