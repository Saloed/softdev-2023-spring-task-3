package com.github.kot512.surroundedAndHunted.combat.weapons

import com.github.kot512.surroundedAndHunted.combat.ammo.BallProjectile
import com.github.kot512.surroundedAndHunted.combat.ammo.Projectile
import com.github.kot512.surroundedAndHunted.entities_and_objects.Player
import com.github.kot512.surroundedAndHunted.screens.playable_screens.BaseLocationScreen
import com.github.kot512.surroundedAndHunted.tools.Point
import kotlin.math.pow

class SemiAutomaticBalls(
    private val player: Player,
    screen: BaseLocationScreen,
    directionAngle: Float,
    shootPoint: Point)
    : ProjectileManager(screen, directionAngle, shootPoint) {
    private val gunLevel: Int
    get() = player.gunLevel

    override val cooldown: Float = 0.4f * 0.95f.pow(gunLevel)

    override var projectileSpeed: Float = 800f
    override var projMaxDistance: Float = 500f * 1.2f.pow(gunLevel)
    override var projDamage: Float = 20f * 1.2f.pow(gunLevel)

    override val projInShot: Int = gunLevel
    override val angleBetweenShots: Float = 20f
    override val cooldownBetweenProjs: Float = 0f

    override fun createProj(): Projectile =
        BallProjectile(
            screen, directionAngle, projectileSpeed,
            projMaxDistance, projDamage, shootPoint
        )
}
