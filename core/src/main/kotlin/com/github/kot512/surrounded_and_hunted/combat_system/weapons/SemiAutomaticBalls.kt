package com.github.kot512.surrounded_and_hunted.combat_system.weapons

import com.github.kot512.surrounded_and_hunted.combat_system.ammo.BallProjectile
import com.github.kot512.surrounded_and_hunted.combat_system.ammo.Projectile
import com.github.kot512.surrounded_and_hunted.screen.BaseLocationScreen
import com.github.kot512.surrounded_and_hunted.tools.Point
import ktx.app.KtxScreen

class SemiAutomaticBalls(screen: BaseLocationScreen, directionAngle: Float, shootPoint: Point)
    : ProjectileManager(screen, directionAngle, shootPoint) {
    override val cooldown: Float = 0.5f

    override var projectileSpeed: Float = 300f
    override var projMaxDistance: Float = 500f
    override var projDamage: Float = 100f

    override val projInShot: Int = 1
    override val angleBetweenShots: Float = 0f
    override val cooldownBetweenProjs: Float = 0f

    override fun createProj(): Projectile =
        BallProjectile(
            screen, directionAngle, projectileSpeed,
            projMaxDistance, projDamage, shootPoint
        )
}
